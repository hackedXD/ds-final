import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
    Request (abstract)
    - Represents a request from a client
    - Has a
        - clientSocket - the socket the request came from
        - method - the HTTP method of the request
        - path - the path of the request
        - headers - a map of headers (key-value pairs)
        - receiveTime - the time the request was received (nanoTime)
 */
public abstract class Request implements Comparable<Request> {
    protected Socket clientSocket; // The socket the request came from
    protected String method, path; // The HTTP method and path of the request
    protected Map<String, String> headers = new HashMap<>(); // The headers of the request
    protected long receiveTime = System.nanoTime(); // The time the request was received

    /*
        Parses a request from a client socket
        - Reads the request line by line
        - Parses the method, path, and headers
        - Returns a Request object based on the path

        Sample HTTP Request:
        GET /shopping HTTP/1.1
        Host: localhost:8080
        User-Agent: curl/7.64.1

        Sample Request Object:
        Request {
            clientSocket: Socket
            method: "GET"
            path: "/shopping"
            headers: {
                "Host": "localhost:8080",
                "User-Agent": "curl/7.64.1"
            }
        }

        Pre-condition: clientSocket is a valid socket, providing a valid HTTP request
        Post-condition: Returns a Request object based on the path. Return null if the request is invalid
     */
    public static Request parse(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            String method; String path;

            line = reader.readLine(); // In the format: "METHOD /path HTTP/1.1"
            if (line == null) return null;

            String[] parts = line.split(" ");
            if (parts.length != 3) return null;

            method = parts[0];
            path = parts[1];

            if (!parts[2].equals("HTTP/1.1")) return null;

            Request request;
            switch (path) {
                case "/":
                    request = new IndexRequest();
                    break;
                case "/shopping":
                    request = new ShoppingRequest();
                    break;
                default:
                    request = new NotFoundRequest();
                    break;
            }

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                String[] header = line.split(": "); // In the format: "Header: Value"
                if (header.length != 2) return null;

                request.headers.put(header[0], header[1]);
            }

            request.clientSocket = clientSocket;
            request.method = method;
            request.path = path;

            return request; // Return the parsed request

        } catch (Exception e) {
            return null;
        }
    }

    /*
        Processes the request
        - Abstract method to be implemented by subclasses
        - Processes the request and sends a response
     */
    abstract public void process();

    /*
        Compares two requests
        - Compares based on the following criteria:
            - ShoppingRequest > NotFoundRequest
            - Auth-Based Request > Non-Auth-Based Request
            - Receive Time (FIFO)
        - Returns 1 if this request is greater, 0 if they are equal, -1 if this request is less

        Pre-condition: other is a valid Request object
     */
    public int compareTo(Request other) {
        // Implement the compareTo method
    }

    // Getters and Setters
    public Socket getClientSocket() { return clientSocket; }
    public void setClientSocket(Socket clientSocket) { this.clientSocket = clientSocket; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

}
