import java.util.ArrayList;
import java.util.List;

/*
    Request Queue
    - This class is responsible for storing incoming requests in a priority queue.
    - It should have a method to add a request to the queue.
    - It should have a method to get the next request from the queue. This method should block until a request is available.
    - It should have a method to get the sorted list of requests in the queue.
 */
public class RequestQueue {
    private final ArrayList<Request> heap = new ArrayList<>(); // Heap to store the requests

    /*
        Add a request to the queue
        - Add the request to the heap
        - Reorder the heap to maintain the priority queue property
        - Notify the waiting threads (if any w/ take())

        @param request: Request object to be added to the queue
        Post-condition: The request is added to the queue
     */
    public void put(Request request) {
        synchronized (heap) {
            // Implement the put() method
        }
    }


    /*
        Get the next request from the queue
        - Wait until a request is available (if the queue is empty)
        - Remove the request from the heap
        - Reorder the heap to maintain the priority queue property
        - Return the request

        @return: The next request from the queue. This method should block until a request is available. Return null if interrupted.
     */
    public Request take() {
        synchronized (heap) {
            // Implement the take() method
        }
    }

    /*
        Get the sorted list of requests in the queue
        - Create a copy of the heap
        - Perform a heap sort on the copy
        - Return the sorted list

        @return: A sorted list of requests in the queue
     */
    public List<Request> getQueue() {
        synchronized (heap) {
            // Implement the getQueue() method
        }
    }
}
