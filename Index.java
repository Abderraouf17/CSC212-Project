

public class Index {
    LinkedList<Document> documents;

    public Index() {
        documents = new LinkedList<Document>();
    }

    public void addDocument(Document document) {
        documents.insert(document);
    }

    public Document getDocument(int id) {
        if (documents.empty()) {
            System.out.println("No documents found");
            return null;
        }
        documents.findFirst();
        while (documents.retrieve() != null) {
            if (documents.retrieve().id == id) {
                return documents.retrieve();
            }
            if (documents.last()) {
                break;

            }
            documents.findNext();
        }

        return null;
    }


    public void displayDoc() {
        if (documents.empty()) {
            System.out.println("No documents found");
            return;
        }

        documents.findFirst();
        while (true) { // Use a loop to iterate over all documents
            Document document = documents.retrieve();
            System.out.print("Doc ID (" + document.id + ") "); // Print the document ID
            System.out.print("["); // Start the word list

            // Iterate over the words in the document
            document.words.findFirst();
            while (true) {
                System.out.print("\"" + document.words.retrieve() + "\"");
                if (document.words.last()) break; // If it's the last word, stop
                System.out.print(", ");
                document.words.findNext();
            }
            System.out.println("]");

            if (documents.last()) break;
            documents.findNext();
        }
    }


}
