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
            heap.add(request);
            int index = heap.size() - 1;

            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                Request parent = heap.get(parentIndex);
                if (request.compareTo(parent) <= 0) {
                    break;
                }
                heap.set(index, parent);
                index = parentIndex;
            }

            heap.set(index, request);

            heap.notify();
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
            try {
                while (heap.isEmpty()) heap.wait();
            } catch (InterruptedException e) { return null; }

            Request result = heap.get(0);
            Request last = heap.remove(heap.size() - 1);

            if (!heap.isEmpty()) {
                heap.set(0, last);
                int index = 0;

                while (true) {
                    int leftChildIndex = 2 * index + 1;
                    if (leftChildIndex >= heap.size()) break;

                    int rightChildIndex = leftChildIndex + 1;
                    int minChildIndex = rightChildIndex >= heap.size() || heap.get(leftChildIndex).compareTo(heap.get(rightChildIndex)) > 0 ? leftChildIndex : rightChildIndex;

                    if (last.compareTo(heap.get(minChildIndex)) >= 0) break;

                    heap.set(index, heap.get(minChildIndex));
                    index = minChildIndex;
                }

                heap.set(index, last);
            }

            return result;
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
            List<Request> sortedHeap = new ArrayList<>(heap);

            for (int i = sortedHeap.size() / 2 - 1; i >= 0; i--) {
                heapify(sortedHeap, sortedHeap.size(), i);
            }

            for (int i = sortedHeap.size() - 1; i > 0; i--) {
                Request temp = sortedHeap.get(0);
                sortedHeap.set(0, sortedHeap.get(i));
                sortedHeap.set(i, temp);

                heapify(sortedHeap, i, 0);
            }

            return sortedHeap;
        }
    }

    private void heapify(List<Request> arr, int n, int i) {
        int leftChildIndex = 2 * i + 1;
        int rightChildIndex = 2 * i + 2;

        int smallest;
        smallest = leftChildIndex < n && arr.get(leftChildIndex).compareTo(arr.get(i)) < 0 ? leftChildIndex : i;
        smallest = rightChildIndex < n && arr.get(rightChildIndex).compareTo(arr.get(smallest)) < 0 ? rightChildIndex : smallest;

        if (smallest != i) {
            Request temp = arr.get(i);
            arr.set(i, arr.get(smallest));
            arr.set(smallest, temp);

            heapify(arr, n, smallest);
        }
    }
}
