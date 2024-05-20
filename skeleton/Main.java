import java.io.IOException;
import java.util.List;
import java.util.Map;

/*
    Driver Class
    - Starts the server
    - Tests the RequestQueue
    - Tests the RequestQueue's Heap Sort
    - Tests the RequestQueue's take() method
 */
public class Main {
    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();

        // ------------- Tests -------------
        System.out.println();

        RequestQueue requestQueue = new RequestQueue();
        Map<String, String> authHeaders = Map.of("Authorization", "imagine-this-was-a-real-auth-token");

        Request shoppingRequest1 = new ShoppingRequest();
        Request notFoundRequest1 = new NotFoundRequest();
        Request indexRequest1 = new IndexRequest();
        Request authIndexRequest = new IndexRequest();
        Request authShoppingRequest = new ShoppingRequest();

        authIndexRequest.setHeaders(authHeaders);
        authShoppingRequest.setHeaders(authHeaders);

        requestQueue.put(shoppingRequest1);
        requestQueue.put(notFoundRequest1);
        requestQueue.put(indexRequest1);
        requestQueue.put(authIndexRequest);
        requestQueue.put(authShoppingRequest);

        /*
            Should be in the Order
            - Auth-Based Shopping Request
            - Shopping Request
            - Auth-Based Index Request
            - Not Found Request
            - Index Request
         */

        System.out.println("------------- compareTo Tests -------------");

        System.out.println("Auth-Based Shopping Request > Shopping Request: " + (authShoppingRequest.compareTo(shoppingRequest1) > 0)); // Should be true
        System.out.println("Shopping Request < Auth-Based Shopping Request: " + (shoppingRequest1.compareTo(authShoppingRequest) < 0)); // Should be true
        System.out.println("Auth-Based Index Request > Not Found Request: " + (authIndexRequest.compareTo(notFoundRequest1) > 0)); // Should be true
        System.out.println("Not Found Request < Auth-Based Index Request: " + (notFoundRequest1.compareTo(authIndexRequest) < 0)); // Should be true
        System.out.println("Not Found Request > Index Request: " + (notFoundRequest1.compareTo(indexRequest1) > 0)); // Should be true
        System.out.println();

        System.out.println("------------- Heap Sort Tests -------------");

        List<Request> sortedHeap = requestQueue.getQueue();
        System.out.println("Sorted Heap Size: " + sortedHeap.size()); // Should be 5
        System.out.println("First Sorted Index is Auth-Based Shopping Request: " + (sortedHeap.get(0) == authShoppingRequest)); // Should be true
        System.out.println("Second Sorted Index is Shopping Request: " + (sortedHeap.get(1) == shoppingRequest1)); // Should be true
        System.out.println("Third Sorted Index is Auth-Based Index Request: " + (sortedHeap.get(2) == authIndexRequest)); // Should be true
        System.out.println("Fourth Sorted Index is Not Found Request: " + (sortedHeap.get(3) == notFoundRequest1)); // Should be true
        System.out.println("Fifth Sorted Index is Index Request: " + (sortedHeap.get(4) == indexRequest1)); // Should be true
        System.out.println();

        System.out.println("------------- Request Queue Tests -------------");

        System.out.println("First request is Auth-Based Shopping Request: " + (requestQueue.take() == authShoppingRequest)); // Should be true
        System.out.println("Second request is Shopping Request: " + (requestQueue.take() == shoppingRequest1)); // Should be true
        System.out.println("Third request is Auth-Based Index Request: " + (requestQueue.take() == authIndexRequest)); // Should be true
        System.out.println("Fourth request is Not Found Request: " + (requestQueue.take() == notFoundRequest1)); // Should be true
        System.out.println("Fifth request is Index Request: " + (requestQueue.take() == indexRequest1)); // Should be true
        System.out.println();

        System.out.println("-----------------------------------------------");

        System.out.println("Press enter to stop the server: ");
        try {
            System.in.read();
        } catch (Exception ignored) {}

        server.stop();
    }
}
