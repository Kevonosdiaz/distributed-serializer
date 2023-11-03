package Main;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sender {

    public static void send(Document doc) {System.out.println("Sender: initializing");
        XMLOutputter xmlOutputter = new XMLOutputter();
        String data = xmlOutputter.outputString(doc);
        // Use try-with-resources to auto-close
        String addr = "localhost";
        int port = 8765;
        try (Socket socket = new Socket(addr, port);
             ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream()))
        {
            System.out.println("Sender: sending");
            outStream.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
