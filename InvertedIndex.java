
public class InvertedIndex {

    LinkedList<Word> invIndex;

    public InvertedIndex() {
        invIndex = new LinkedList<Word>();
    }

    public void add(String term, int docId) {
        Word word = findWord(term); // Find the Word object for the term
        if (word == null) { // If the word doesn't exist, create a new one
            word = new Word(term);
            invIndex.insert(word); // Insert the new Word object into the inverted index
        }
        word.add_ID(docId); // Add the document ID to the Word's index
    }

    public Word findWord(String term) {
        if (invIndex.empty()) {
            return null;
        }

        invIndex.findFirst();
        while (true) {
            Word w = invIndex.retrieve(); // Retrieve the current Word object
            if (w.word.equals(term)) { // Check if the word matches the term
                return w;
            }
            if (invIndex.last()) {
                break;
            }
            invIndex.findNext();
        }

        return null; // Return null if the word is not found
    }

    public void printInvIndex() {
        System.out.printf("%-20s %s\n", "Word", "Document"); // Print header with alignment
        System.out.println("------------------------------------------------------------");

        if (invIndex.empty()) {
            System.out.println("Inverted index is empty");
            return;
        }

        invIndex.findFirst();
        while (true) {
            Word w = invIndex.retrieve(); // Retrieve the current Word object
            System.out.printf("%-20s [", w.word); // Print the word aligned to the left

            // Format and print the document IDs
            System.out.print("");
            w.indexs.findFirst(); // Move to the start of the linked list of document IDs
            while (true) {
                System.out.print(w.indexs.retrieve()); // Print the current document ID
                if (w.indexs.last())
                    break; // If it's the last ID, break out of the loop
                System.out.print(", "); // Print a comma between IDs
                w.indexs.findNext(); // Move to the next ID
            }

            System.out.println("]"); // Close the brackets
            if (invIndex.last())
                break; // If it's the last word, break out of the loop
            invIndex.findNext(); // Move to the next word
        }
    }

    public boolean search(String w) {
        if (invIndex == null || invIndex.empty()) {
            return false;
        }
        invIndex.findFirst();
        while (!invIndex.last()) {
            if (invIndex.retrieve().word.equals(w)) {
                return true;
            }
            invIndex.findNext();
        }
        if (invIndex.retrieve().word.equals(w)) {
            return true;
        }
        return false;

    }

    public LinkedList<Integer> processAndQuery(String word1, String word2) {
        LinkedList<Integer> results = new LinkedList<>();
        if (search(word1) && search(word2)) {
            if (search(word1) && search(word2)) {
                LinkedList<Integer> list1 = invIndex.retrieve().indexs;
                invIndex.findFirst();
                while (!invIndex.empty() && invIndex.retrieve() != null) {
                    if (invIndex.retrieve().word.equals(word2)) {
                        LinkedList<Integer> list2 = invIndex.retrieve().indexs;
                        results = intersectLists(list1, list2);
                        break;
                    }
                    if (invIndex.last())
                        break;
                    invIndex.findNext();
                }
            }
        }
        return results;
    }

    public LinkedList<Integer> processOrQuery(String word1, String word2) {
        LinkedList<Integer> results = new LinkedList<>();
        if (search(word1)) {
            results = invIndex.retrieve().indexs;
        }
        if (search(word2)) {
            LinkedList<Integer> list2 = invIndex.retrieve().indexs;
            results = unionLists(results, list2);
        }
        return results;
    }

    private LinkedList<Integer> intersectLists(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> result = new LinkedList<>();
        list1.findFirst();
        while (!list1.empty() && list1.retrieve() != null) {
            if (existsInList(list2, list1.retrieve())) {
                result.insert(list1.retrieve());
            }
            if (list1.last())
                break;
            list1.findNext();
        }
        return result;
    }

    private LinkedList<Integer> unionLists(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> result = new LinkedList<>();
        list1.findFirst();
        while (!list1.empty() && list1.retrieve() != null) {
            result.insert(list1.retrieve());
            if (list1.last())
                break;
            list1.findNext();
        }
        list2.findFirst();
        while (!list2.empty() && list2.retrieve() != null) {
            if (!existsInList(result, list2.retrieve())) {
                result.insert(list2.retrieve());
            }
            if (list2.last())
                break;
            list2.findNext();
        }
        return result;
    }

    private boolean existsInList(LinkedList<Integer> list, int value) {
        list.findFirst();
        while (!list.empty() && list.retrieve() != null) {
            if (list.retrieve() == value) {
                return true;
            }
            if (list.last())
                break;
            list.findNext();
        }
        return false;
    }

}
