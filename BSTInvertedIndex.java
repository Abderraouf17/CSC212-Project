

public class BSTInvertedIndex {
    private BST<Word> bst;

    public BSTInvertedIndex() {
        this.bst = new BST<>();
    }
    public void addWord(String word, int docId) {
        Word existingWord = searchWord(word); // Use explicit search by word
        if (existingWord == null) {
            Word newWord = new Word(word);
            newWord.indexs.insert(docId);
            insertWord(newWord); // Insert the word directly into the BST
        } else {
            if (!existingWord.indexs.contains(docId)) {
                existingWord.indexs.insert(docId);
            }
        }
    }

    // Explicit search method to find a word in the BST
    public Word searchWord(String word) {
        BSTNode<Word> current = bst.getRoot();
        while (current != null) {
            int comparison = word.compareTo(current.data.word);
            if (comparison == 0) {
                return current.data; // Word found
            } else if (comparison < 0) {
                current = current.left; // Go left
            } else {
                current = current.right; // Go right
            }
        }
        return null; // Word not found
    }
    public LinkedList<Integer> getDocumentListForWord(String word) {
        Word w = searchWord(word);
        return (w != null) ? w.indexs : new LinkedList<>();
    }

    // Insert method for adding a new word into the BST
    private void insertWord(Word newWord) {
        if (bst.root == null) {
            bst.root = new BSTNode<>(0, newWord); // Root node case
            return;
        }

        BSTNode<Word> parent = null;
        BSTNode<Word> current = bst.root;

        while (current != null) {
            parent = current;
            int comparison = newWord.word.compareTo(current.data.word);
            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                // Word already exists (should not reach here in this logic)
                return;
            }
        }

        // Insert as left or right child
        int comparison = newWord.word.compareTo(parent.data.word);
        if (comparison < 0) {
            parent.left = new BSTNode<>(0, newWord); // Insert on the left
        } else {
            parent.right = new BSTNode<>(0, newWord); // Insert on the right
        }
    }


    public void displayIndex() {
        System.out.println("Word                 Document");
        System.out.println("------------------------------------------------------------");
        displayInOrder(bst.getRoot()); // Start traversal from the root of the BST
    }

    private void displayInOrder(BSTNode<Word> node) {
        if (node == null) return; // Base case: If the node is null, return

        // Traverse the left subtree
        displayInOrder(node.left);

        // Process the current node (display the word and its document IDs)
        Word word = node.data;
        System.out.printf("%-20s [", word.word); // Align the word to the left with a width of 20 characters
        word.indexs.findFirst();
        while (true) {
            System.out.print(word.indexs.retrieve());
            if (word.indexs.last()) break;
            System.out.print(", ");
            word.indexs.findNext();
        }
        System.out.println("]");

        // Traverse the right subtree
        displayInOrder(node.right);
    }




}
