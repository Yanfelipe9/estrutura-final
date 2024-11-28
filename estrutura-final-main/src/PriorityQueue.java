public class PriorityQueue {
    private Node[] heap;
    public int size;

    public PriorityQueue(int capacity) {
        this.heap = new Node[capacity];
        this.size = 0;
    }

    public void insert(Node node) {
        heap[size] = node;
        int current = size;
        while (current > 0 && heap[parent(current)].frequency > heap[current].frequency) {
            swap(current, parent(current));
            current = parent(current);
        }
        size++;
    }

    public Node extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty!");
        }
        Node min = heap[0];
        heap[0] = heap[--size];
        heapify(0);
        return min;
    }

    private void heapify(int index) {
        int left = leftChild(index);
        int right = rightChild(index);
        int smallest = index;

        if (left < size && heap[left].frequency < heap[smallest].frequency) {
            smallest = left;
        }
        if (right < size && heap[right].frequency < heap[smallest].frequency) {
            smallest = right;
        }
        if (smallest != index) {
            swap(index, smallest);
            heapify(smallest);
        }
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private void swap(int i, int j) {
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
