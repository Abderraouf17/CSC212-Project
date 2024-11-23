

public class BSTNode<T> {
    int key;
    T data;
    BSTNode<T> left, right;

    public BSTNode(int key, T data) {
        this.key = key;
        this.data = data;
        left = right = null;
    }
}
