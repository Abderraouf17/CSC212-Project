

public class BST<T>{
    private static class BSTNode<T> {
        int key;
        T data;
        BSTNode<T> left, right;

        public BSTNode(int key, T data) {
            this.key = key;
            this.data = data;
            left = right = null;
        }
    }

    private BSTNode<T> root, current;

    public BST() {
        root = current = null;
    }

    public boolean empty() {
        return root == null;
    }

    public boolean full() {
        return false;
    }

    public T retrieve() {
        return current.data;
    }

    public boolean findKey(int k) {
        BSTNode<T> n = root;
        while (n != null) {
            current = n;
            if (k == n.key)
                return true;
            else if (k < n.key)
                n = n.left;
            else
                n = n.right;
        }

        return false;
    }

    public boolean update(int k, T data) {
        if (!findKey(k))
            return false;

        current.data = data;
        return true;
    }

    public boolean insert(int k, T data) {
        if (root == null) {
            current = root = new BSTNode<T>(k, data);
            return true;
        }

        BSTNode<T> n = current;
        if (findKey(k)) {
            current = n;
            return false;
        }

        BSTNode<T> newNode = new BSTNode<T>(k, data);
        if (k < current.key)
            current.left = newNode;
        else
            current.right = newNode;

        current = newNode;

        return true;
    }



    public boolean remove(int k) {
        BSTNode<T> n = root, p = null;
        while (n != null) {
            if (k < n.key) {
                p = n;
                n = n.left;
            }
            else if (k > n.key) {
                p = n;
                n = n.right;
            }
            else {
                // case 3
                if (n.left != null && n.right != null) {
                    BSTNode<T> min = n.right;
                    p = n;
                    while (min.left != null) {
                        p = min;
                        min = min.left;
                    }

                    n.key = min.key;
                    n.data = min.data;
                    n = min;
                }
                // fall into case 1 & 2
                BSTNode<T> c = null;
                if (n.left != null)
                    c = n.left;
                else
                    c = n.right;

                if (p == null)
                    root = c;
                else {
                    if (n.key < p.key)
                        p.left = c;
                    else
                        p.right = c;
                }

                current = root;
                return true;
            }
        }
        return false;
    }

    public void deleteSubtree(){
        if (current == root){
            current = root = null;
        }
        else {
            BSTNode<T> n = root, p = null;
            while (n != current) {
                p = n;
                if (current.key < n.key)
                    n = n.left;
                else
                    n = n.right;
            }

            if (p.left == current)
                p.left = null;
            else
                p.right = null;

            current = root;
        }
    }

    public void traverse(Order o) {
        switch(o) {
            case PREORDER:
                traversePreorder(root);
                break;
            case INORDER:
                traverseInorder(root);
                break;
            case POSTORDER:
                traversePostorder(root);
                break;
        }
    }

    public void traversePreorder(BSTNode<T> n) {
        if (n != null) {
            System.out.print(n.data);
            traversePreorder(n.left);
            traversePreorder(n.right);
        }
    }

    public void traverseInorder(BSTNode<T> n) {
        if (n != null) {
            traverseInorder(n.left);
            System.out.print(n.data);
            traverseInorder(n.right);
        }
    }

    public void traversePostorder(BSTNode<T> n) {
        if (n != null) {
            traversePostorder(n.left);
            traversePostorder(n.right);
            System.out.print(n.data);
        }
    }
}