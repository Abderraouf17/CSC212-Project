
public class Query {
    private InvertedIndex inv; // Instance variable for InvertedIndex

    public Query(InvertedIndex inv) {
        this.inv = inv;
    }

    // Main entry point for Boolean queries
    public LinkedList<Integer> CheckQuery(String Q) {
        LinkedList<Integer> result = new LinkedList<>();

        if (Q.contains("AND") && !Q.contains("OR")) {

            result = ANDBoleean(Q);
        } else if (Q.contains("OR") && !Q.contains("AND")) {

            result = ORBoleean(Q);
        } else if (Q.contains("AND") && Q.contains("OR")) {

            result = ComplexBoleean(Q);
        }
        return result;
    }

    // AND OR logic: AND has higher precedence than OR
    public LinkedList<Integer> ComplexBoleean(String Q) {
        LinkedList<Integer> result = new LinkedList<>();

        // First, handle OR splits
        String[] orParts = Q.split("OR");

        // For each OR part, process the AND logic within it
        for (int i = 0; i < orParts.length; i++) {
            String part = orParts[i].trim();
            LinkedList<Integer> andResult = ADDSingle(part);  // Process AND parts
            result = ORBoleeanHelper(result, andResult);  // Combine the AND results using OR
        }

        return result;
    }

    // Process AND inside a single query part (like 'sports AND warming')
    private LinkedList<Integer> ADDSingle(String part) {
        LinkedList<Integer> result = new LinkedList<>();

        // If the part contains AND, split it further and process each AND term
        if (part.contains("AND")) {
            String[] andParts = part.split("AND");
            result = processSingleWordQuery(andParts[0].trim());  // Process the first AND term

            // Loop through remaining AND terms and combine the results
            for (int i = 1; i < andParts.length; i++) {
                LinkedList<Integer> tempResult = processSingleWordQuery(andParts[i].trim());
                result = ANDHelper(result, tempResult);  // Perform AND operation on the results
            }
        } else {
            // If there's no AND, just process the single word
            result = processSingleWordQuery(part.trim());
        }
        return result;
    }

    // Process a single word query (find the document IDs for the word)
    private LinkedList<Integer> processSingleWordQuery(String word) {
        boolean found = inv.search(word);  // Use the search method from the InvertedIndex
        if (found) {
            return inv.invIndex.retrieve().indexs;  // Return the list of document IDs
        }
        return new LinkedList<>();  // Return an empty list if the word isn't found
    }

    // Perform AND operation on two lists
    public LinkedList<Integer> ANDBoleean(String Q) {
        LinkedList<Integer> A = new LinkedList<>();
        LinkedList<Integer> B = new LinkedList<>();
        String[] words = Q.split("AND");

        if (words.length == 0) return A;  // Return an empty list if no words to process

        boolean found = inv.search(words[0].trim().toLowerCase());
        if (found) {
            A = inv.invIndex.retrieve().indexs;
        }

        for (int i = 1; i < words.length; i++) {
            found = inv.search(words[i].trim().toLowerCase());
            if (found) {
                B = inv.invIndex.retrieve().indexs;
            }

            A = ANDHelper(A, B);  // Perform the AND operation
        }
        return A;
    }

    // Perform OR operation on two lists
    public LinkedList<Integer> ORBoleean(String Q) {
        LinkedList<Integer> A = new LinkedList<>();
        LinkedList<Integer> B = new LinkedList<>();
        String[] words = Q.split("OR");

        if (words.length == 0) return A;  // Return empty if no words to process

        boolean found = inv.search(words[0].trim().toLowerCase());
        if (found) {
            A = inv.invIndex.retrieve().indexs;
        }

        for (int i = 1; i < words.length; i++) {
            found = inv.search(words[i].trim().toLowerCase());
            if (found) {
                B = inv.invIndex.retrieve().indexs;
            }
            A = ORBoleeanHelper(A, B);  // Perform the OR operation
        }
        return A;
    }

    // Helper for OR operation
    public LinkedList<Integer> ORBoleeanHelper(LinkedList<Integer> A, LinkedList<Integer> B) {
        LinkedList<Integer> C = new LinkedList<>();

        if (A.empty() && B.empty()) return C;  // Return empty if both lists are empty
        if (A.empty()) return B;  // If A is empty, return B
        if (B.empty()) return A;  // If B is empty, return A

        A.findFirst();
        while (!A.empty()) {
            if (!Exsist(C, A.retrieve())) {
                C.insert(A.retrieve());  // Insert unique items from A
            }
            if (A.last()) break;
            A.findNext();
        }

        B.findFirst();
        while (!B.empty()) {
            if (!Exsist(C, B.retrieve())) {
                C.insert(B.retrieve());  // Insert unique items from B
            }
            if (B.last()) break;
            B.findNext();
        }
        return C;
    }

    // Helper for AND operation
    public LinkedList<Integer> ANDHelper(LinkedList<Integer> A, LinkedList<Integer> B) {
        LinkedList<Integer> C = new LinkedList<>();
        if (A.empty() || B.empty()) return C;  // Return empty if either list is empty

        A.findFirst();
        while (!A.empty()) {
            if (Exsist(B, A.retrieve())) {
                C.insert(A.retrieve());  // Insert common items between A and B
            }
            if (A.last()) break;
            A.findNext();
        }
        return C;
    }

    // Helper method to check if an element exists in a list
    public boolean Exsist(LinkedList<Integer> A, int id) {
        A.findFirst();
        while (!A.empty()) {
            if (A.retrieve() == id) return true;  // Return true if the id exists
            if (A.last()) break;
            A.findNext();
        }
        return false;
    }

    // Example: Display query results
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
