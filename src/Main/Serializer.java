package Main;

import org.jdom2.Document;
import org.jdom2.Element;

public class Serializer {
    public static Document serialize(Object object) {
        Document doc = new Document();
        // Create an element with "serialized" tag and make it root
        Element root = new Element("serialized");
        doc.setRootElement(root);

        // Create an element with "object" tag and add it to root
        Element obj = new Element("object");
        root.addContent(obj);

        return doc;
    }
}
