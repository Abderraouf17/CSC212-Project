public class LinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            next = null;
        }
    }

    // Representation
    private Node<T> head;
    private Node<T> current;

    // Implementation
    public LinkedList() {
        head = current = null;
    }

    public boolean empty() {
        return head == null;
    }

    public boolean full() {
        return false;
    }

    public boolean last() {
        return current.next == null;
    }

    public void findFirst() {
        current = head;
    }

    public void findNext() {
        current = current.next;
    }

    public T retrieve() {
        return current.data;
    }

    public void update(T data) {
        current.data = data;
    }

    public void insert(T data) {
        Node<T> newNode = new Node<T>(data);

        if (head == null) {
            head = current = newNode;
        } else {
            newNode.next = current.next;
            current.next = newNode;
            current = newNode;
        }
    }

    public void remove() {
        if (current == head) {
            head = head.next;
        } else {
            Node<T> prev = head;
            while (prev.next != current) {
                prev = prev.next;
            }

            prev.next = current.next;
        }

        current = (current.next == null) ? head : current.next;
    }
}