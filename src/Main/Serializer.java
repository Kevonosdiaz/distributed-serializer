package Main;

import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Serializer {
    private static int id = 0;
    // seen keeps track of objects we have already serialized, using its declaring class name
    private static final HashSet<String> seen = new HashSet<>();
    // objects maps each object name to its ID
    private static final HashMap<String, Integer> objects = new HashMap<>();
    // private static final LinkedList<Object> serializeQueue = new LinkedList<>(); // An option for delaying serialization of referenced objects

    // Serialize an object, by creating a base document and attaching results of serializeBody() to it
    public static Document serialize(Object object) {
        Document doc = new Document();
        // Create an element with "serialized" tag and make it root
        Element root = new Element("serialized");
        doc.setRootElement(root);

        // Add the body of the object to the root, along with any other objects referenced by it
        doc.addContent(serializeBody(object).getRootElement());

        return doc;
    }

    // Serialize the body of an object, as well as any other objects referenced by it
    public static Document serializeBody(Object object) {
        Document doc = new Document();
        // Create an element with "object" tag and add it to root
        Element root = new Element("object");
        doc.setRootElement(root);
        Class <?> objClass = object.getClass();
        seen.add(objClass.getName());
        objects.put(objClass.getName(), id);
        root.setAttribute("class", objClass.getName());
        root.setAttribute("id", Integer.toString(id));
        id++;

        // Reflectively find fields of object
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
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
                } else {
                    // Branch depending on whether we have a primitive or an object
                    if (field.getType().isPrimitive()) { // Create an element with "value" tag and add it to field
                        Element value = new Element("value");
                        fieldElement.addContent(value);
                        // If primitive, just set value of field to its string representation
                        value.setText(fieldValue.toString());
                    } else {
                        // If an object, recursively serialize it and set value of 'reference' to its id
                        Element reference = new Element("reference");
                        fieldElement.addContent(reference);
                        String fieldClassName = fieldValue.getClass().getName();

                        // First check if we have already serialized this object
                        if (seen.contains(fieldClassName)) {
                            // Reference our HashMap to get the id of the object
                            reference.setText(String.valueOf(objects.get(fieldClassName)));
                            continue;
                        }
                        // Recursively serialize the object if we haven't seen it before
                        Document serializedObject = serializeBody(fieldValue);
                        reference.setText(Integer.toString(id));
                        root.addContent(serializedObject.getRootElement());
                    }
                }
            } catch (IllegalAccessException | InaccessibleObjectException e) {
                // Make value of field show that it could not be accessed
                Element value = new Element("value");
                fieldElement.addContent(value);
                value.setText("Could not access field! (May be private)");
            }
        }
        return doc;
    }
}