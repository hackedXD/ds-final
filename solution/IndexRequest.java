import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.List;

/*
    Index Request
    - Displays the current request queue
 */
public class IndexRequest extends Request {
    public static RequestQueue requestQueue; // Static reference to the server RequestQueue

    /*
        Process the Index Request
        - Return a html page with the current request queue
        - Show the method and path of each request
     */
    @Override
    public void process() {
        System.out.println("Index Request Received!");

        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<html><body><h1>Request Queue</h1>");

            List<Request> requests = requestQueue.getQueue();
            if (requests.isEmpty()) {
                response.append("<p>Request Queue is empty!</p>");
            } else {
                response.append("<ul>");
                for (Request r : requests) {
                    response
                            .append("<li>")
                            .append(r.method)
                            .append(" ")
                            .append(r.path)
                            .append("</li>");
                }
                response.append("</ul>");
            }

            response.append("</body></html>");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            writer.write(response.toString());
            writer.flush();

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
