package Main;

import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;

public class Serializer {
    private static int id = 0;
//    // seen keeps track of objects we have already serialized, using its declaring class name
//    private static final HashSet<Object> seen = new HashSet<>();
    // objects maps each object name to its ID
    private static final IdentityHashMap<Object, Integer> objects = new IdentityHashMap<>();
    public static final HashMap<Integer, Object> objectIDs = new HashMap<>();
    // private static final LinkedList<Object> serializeQueue = new LinkedList<>(); // An option for delaying serialization of referenced objects

    // Serialize an object, by creating a base document and attaching results of serializeBody() to it
    public static org.jdom2.Document serialize(Object object) {
        Document doc = new Document();
        // Create an element with "serialized" tag and make it root
        Element root = new Element("serialized");
        doc.setRootElement(root);

        // Add the body of the object to the root, along with any other objects referenced by it
        root.addContent(serializeBody(object));
        return doc;
    }

    // Serialize the body of an object, as well as any other objects referenced by it
    public static Element serializeBody(Object object) {
        Element root = new Element("object");
        Class <?> objClass = object.getClass();
        objects.put(object, id);
        objectIDs.put(id, object); // Store this so that it can be referenced in other objects easily
        root.setAttribute("class", objClass.getName());
        root.setAttribute("id", Integer.toString(id));
        id++;

        // Separate case for array
        if (object.getClass().isArray()) {
            // Don't bother with fields at all then
            int length = Array.getLength(object);
            root.setAttribute("length", String.valueOf(length));
            for (int i = 0; i < length; i++) {
                Object arrayElement = Array.get(object, i);
                if (arrayElement == null) {
                    Element value = new Element("value");
                    root.addContent(value);
                    value.setText("null");
                    continue;
                }
                // Branch depending on whether we have a primitive or an array, or an object
                if (arrayElement.getClass().isPrimitive() || isWrapperType(arrayElement.getClass())) { // Create an element with "value" tag and add it to field
                    Element value = new Element("value");
                    root.addContent(value);
                    // If primitive, just set value of field to its string representation
                    value.setText(arrayElement.toString());
                    continue;
                }
                // If an object, recursively serialize it and set value of 'reference' to its id
                Element reference = new Element("reference");
                root.addContent(reference);

                // Reference our HashMap to get the id of the object
                if (objects.containsKey(arrayElement)) {
                    // Reference our HashMap to get the id of the object
                    reference.setText(String.valueOf(objects.get(arrayElement)));
                    continue;
                }
                reference.setText(Integer.toString(id));
            }
            return root;
        }

        // Separate case for HashSet
        if (object instanceof HashSet<?> hashSet) {
            // Don't bother with fields at all then
            Class<?> componentType = null;
            root.setAttribute("set-length", String.valueOf(hashSet.size()));
            for (Object hashSetElement : hashSet) {
                if (hashSetElement == null) {
                    Element value = new Element("value");
                    root.addContent(value);
                    value.setText("null");
                    continue;
                }
                // Guaranteed to have int, double, or char
                componentType = hashSetElement.getClass();
                Element value = new Element("value");
                root.addContent(value);
                // If primitive, just set value of field to its string representation
                value.setText(hashSetElement.toString());
            }
            assert componentType != null;
            root.setAttribute("component-type", componentType.getName());
            return root;
        }

        // Reflectively find fields of object
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            // Skip static fields entirely
            if (java.lang.reflect.Modifier.isStatic(modifiers)) continue;

            // Create an element with "field" tag and add it to object
            Element fieldElement = new Element("field");
            root.addContent(fieldElement);
            fieldElement.setAttribute("name", field.getName());
            fieldElement.setAttribute("declaringclass", field.getDeclaringClass().getName());

            // Get value of field
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(object);
                if (fieldValue == null) {
                    Element value = new Element("value");
                    fieldElement.addContent(value);
                    value.setText("null");
                    continue;
                }
                // Branch depending on whether we have a primitive or an array, or an object
                if (field.getType().isPrimitive() || fieldValue instanceof String || isWrapperType(field.getType())) { // Create an element with "value" tag and add it to field
                    Element value = new Element("value");
                    fieldElement.addContent(value);
                    // If primitive, just set value of field to its string representation
                    value.setText(fieldValue.toString());
                    continue;
                }
                // If an object, recursively serialize it and set value of 'reference' to its id
                Element reference = new Element("reference");
                fieldElement.addContent(reference);

                // First check if we have already serialized this object
                if (objects.containsKey(fieldValue)) {
                    // Reference our HashMap to get the id of the object
                    reference.setText(String.valueOf(objects.get(fieldValue)));
                    continue;
                }
                // Recursively serialize the object if we haven't seen it before
                reference.setText(Integer.toString(id));
            } catch (IllegalAccessException | InaccessibleObjectException e) {
                // Make value of field show that it could not be accessed
                Element value = new Element("value");
                fieldElement.addContent(value);
                value.setText("Could not access field! (May be private)");
            }
        }
        return root;
    }

    // Properly check if we have a primitive class that is wrapped (in case of auto-boxing)
    // From https://stackoverflow.com/questions/709961/determining-if-an-object-is-of-primitive-type
    public static boolean isWrapperType(Class<?> clazz) {
        return clazz.equals(Boolean.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class);
    }
}
