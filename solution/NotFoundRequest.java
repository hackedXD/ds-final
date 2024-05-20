import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class NotFoundRequest extends Request {

    /*
        Process the Not Found Request
        - Return a html page with a 404 message
     */
    @Override
    public void process() {
        System.out.println("Unknown Request Received!");

        try {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<html><body><h1>idk what your looking for!</h1></body></html>";

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            writer.write(response);
            writer.flush();

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
