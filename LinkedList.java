
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

    public boolean exist(T data) {
        Node<T> temp = head;
        while (temp != null) {
            if (temp.data.equals(data)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
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

    public void display() {
        Node<T> temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    public boolean contains(T value) {
        if (empty()) {
            return false;
        }
        findFirst();
        while (true) {
            if (retrieve().equals(value)) {
                return true;
            }
            if (last()) {
                break;
            }
            findNext();
        }
        return false; // Value not found
    }

    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    public int size() {
        int count = 0;
        Node<T> current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

}
