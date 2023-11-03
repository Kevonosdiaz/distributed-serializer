package Main;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Formatter;

public class Sender {
    public Sender(String addr, int port) {
        System.out.println("Sender: initializing");
        String data = "I'm sending this data";
        // Use try-with-resources to auto-close
        try (Socket socket = new Socket(addr, port);
             OutputStream outStream = socket.getOutputStream();
             Formatter fmt = new Formatter(outStream);)
        {
            System.out.println("Sender: connecting");
            fmt.format(data);
            fmt.flush(); // Make sure our stuff gets sent
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
