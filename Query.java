public class Query {
    private InvertedIndex inv; // Instance variable for InvertedIndex
    private BSTInvertedIndex bstInv; // Instance variable for BSTInvertedIndex
    private boolean useBST; // Determines whether to use BST-based or List-based index

    // Constructor for InvertedIndex
    public Query(InvertedIndex inv) {
        this.inv = inv;
        this.useBST = false;
    }

    // Constructor for BSTInvertedIndex
    public Query(BSTInvertedIndex bstInv) {
        this.bstInv = bstInv;
        this.useBST = true;
    }

    // Main entry point for Boolean and Complex queries
    public LinkedList<Integer> CheckQuery(String Q) {
        LinkedList<Integer> result = new LinkedList<>();
        if (Q.contains("AND") && !Q.contains("OR")) {
            result = ANDBoolean(Q);
        } else if (Q.contains("OR") && !Q.contains("AND")) {
            result = ORBoolean(Q);
        } else if (Q.contains("AND") && Q.contains("OR")) {
            result = ComplexBoolean(Q);
        }
        return result;
    }

    // Process AND queries
    public LinkedList<Integer> ANDBoolean(String Q) {
        LinkedList<Integer> result = new LinkedList<>();
        String[] words = Q.split("AND");
        for (String word : words) {
            LinkedList<Integer> currentList = getDocumentList(word.trim());
            if (result.empty()) {
                result = currentList;
            } else {
                result = ANDHelper(result, currentList);
            }
        }
        return result;
    }

    // Process OR queries
    public LinkedList<Integer> ORBoolean(String Q) {
        LinkedList<Integer> result = new LinkedList<>();
        String[] words = Q.split("OR");
        for (String word : words) {
            LinkedList<Integer> currentList = getDocumentList(word.trim());
            result = ORHelper(result, currentList);
        }
        return result;
    }

    // Process Complex Queries
    public LinkedList<Integer> ComplexBoolean(String Q) {
        LinkedList<Integer> result = new LinkedList<>();
        String[] orParts = Q.split("OR");
        for (String part : orParts) {
            LinkedList<Integer> andResult = ANDBoolean(part.trim());
            result = ORHelper(result, andResult);
        }
        return result;
    }

    // Retrieve document list for a term
    private LinkedList<Integer> getDocumentList(String word) {
        if (useBST) {
            Word w = bstInv.searchWord(word);
            return (w != null) ? w.indexs : new LinkedList<>();
        } else {
            boolean found = inv.search(word);
            return (found) ? inv.invIndex.retrieve().indexs : new LinkedList<>();
        }
    }

    // Helper for AND (Intersection)
    private LinkedList<Integer> ANDHelper(LinkedList<Integer> A, LinkedList<Integer> B) {
        LinkedList<Integer> result = new LinkedList<>();
        A.findFirst();
        while (!A.empty()) {
            if (existsIn(B, A.retrieve())) {
                result.insert(A.retrieve());
            }
            if (A.last()) break;
            A.findNext();
        }
        return result;
    }

    // Helper for OR (Union)
    private LinkedList<Integer> ORHelper(LinkedList<Integer> A, LinkedList<Integer> B) {
        LinkedList<Integer> result = new LinkedList<>();
        A.findFirst();
        while (!A.empty()) {
            result.insert(A.retrieve());
            if (A.last()) break;
            A.findNext();
        }
        B.findFirst();
        while (!B.empty()) {
            if (!existsIn(result, B.retrieve())) {
                result.insert(B.retrieve());
            }
            if (B.last()) break;
            B.findNext();
        }
        return result;
    }

    // Helper to check if an element exists in a list
    private boolean existsIn(LinkedList<Integer> list, int value) {
        list.findFirst();
        while (!list.empty()) {
            if (list.retrieve() == value) return true;
            if (list.last()) break;
            list.findNext();
        }
        return false;
    }

    // Display the query results
    public void DisplayQuery(LinkedList<Integer> results) {
        results.findFirst();
        System.out.print("Query Result: [");
        while (!results.empty() && results.retrieve() != null) {
            System.out.print(results.retrieve());
            if (results.last()) break;
            System.out.print(", ");
            results.findNext();
        }
        System.out.println("]");
    }
}
