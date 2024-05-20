import java.io.IOException;
import java.net.ServerSocket;

/*
    Server Class:
    - This class is responsible for creating a server socket and listening for incoming connections.
    - It also creates a thread to handle incoming requests and a pool of worker threads to process the requests.
    - It deals with the request queue that you will have to implement
 */
public class Server {
    private final ServerSocket serverSocket; // ServerSocket object to listen for incoming connections
    private Thread serverThread; // Thread to handle incoming requests
    private Thread[] workerThreads; // Fixed pool of worker threads to process the requests
    private final RequestQueue requestQueue = new RequestQueue(); // Request queue to store incoming requests

    /*
        Constructor
        - Create a server socket on the specified port
        - Set the request queue for the IndexRequest

        @param port: Port number to listen for incoming connections
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port); // Create a server socket on the specified port
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IndexRequest.requestQueue = requestQueue; // Set the request queue for the IndexRequest (which uses it for sorted display)
    }


    /*
        Start the server
        - Create a thread to handle incoming requests
        - Create a pool of worker threads to process the requests

        The receiver thread
        - Listens for incoming connections
        - Parses the incoming request
        - Adds the request to the queue

        The worker threads
        - Get the next request from the queue
        - Process the request
     */
    public void start() {
        System.out.println("Server is running on port " + serverSocket.getLocalPort());

         this.serverThread = new Thread(() -> {
            try {
                while (true) {
                    Request r = Request.parse(serverSocket.accept()); // Parse the incoming request
                    if (r != null) requestQueue.put(r); // Add the request to the queue
                }
            } catch (Exception ignored) {}
        });
        serverThread.start();

        workerThreads = new Thread[4];
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new Thread(() -> {
                try {
                    while (true) {
                        Request r = requestQueue.take(); // Get the next request from the queue
                        if (r == null) break; // Break the loop if the request is null (queue is closed)
                        System.out.println("Processing request");
                        r.process(); // Process the request
                    }
                } catch (Exception ignored) {}
            });
            workerThreads[i].start();
        }
    }


    /*
        Stop the server
        - Uses the interrupt method to stop the server thread and worker threads
     */
    public void stop() {
        serverThread.interrupt();
        for (Thread t : workerThreads) {
            t.interrupt();
        }

        try {
            serverSocket.close();
        } catch (IOException ignored) {}
    }
}
