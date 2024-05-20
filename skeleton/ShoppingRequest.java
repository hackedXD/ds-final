import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

/*
    Shopping Request
    - Displays a shopping page
 */
public class ShoppingRequest extends Request {

    /*
        Process the Shopping Request
        - Return a html page with a shopping message
     */
    @Override
    public void process() {
        System.out.println("Shopping Request Received!");

        try {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<html><body><h1>Shopping!</h1></body></html>";

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            writer.write(response);
            writer.flush();

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
