import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize necessary components
        Read reader = new Read();
        InvertedIndex invertedIndex = new InvertedIndex();
        BSTInvertedIndex bstInvertedIndex = new BSTInvertedIndex();
        Ranking rankingWithList = new Ranking(invertedIndex, reader.index); // Ranking for List-based index
        Ranking rankingWithBST = new Ranking(bstInvertedIndex, reader.index); // Ranking for BST-based index
        Query queryProcessorWithList = new Query(invertedIndex); // Query with InvertedIndex
        Query queryProcessorWithBST = new Query(bstInvertedIndex); // Query with BSTInvertedIndex

        // Load stop words and documents
        reader.readStopWords("stop.txt");
        reader.readDocuments("dataset.csv");

        // Process documents and build both inverted indexes
        processDocuments(reader, invertedIndex, bstInvertedIndex);

        // Display indices
        displayIndices(reader, invertedIndex, bstInvertedIndex);

        // Display menu and handle user input
        displayMenu(queryProcessorWithList, queryProcessorWithBST, rankingWithList, rankingWithBST);
    }

    private static void processDocuments(Read reader, InvertedIndex invertedIndex, BSTInvertedIndex bstInvertedIndex) {
        reader.index.documents.findFirst();
        while (!reader.index.documents.empty() && reader.index.documents.retrieve() != null) {
            Document doc = reader.index.documents.retrieve();
            LinkedList<String> processedWords = reader.MakeInverted(doc.tokens, doc.id);
            doc.words = processedWords;
            addWordsToIndexes(processedWords, doc.id, invertedIndex, bstInvertedIndex);
            if (reader.index.documents.last())
                break;
            reader.index.documents.findNext();
        }
    }

    private static void addWordsToIndexes(LinkedList<String> words, int docId, InvertedIndex invertedIndex, BSTInvertedIndex bstInvertedIndex) {
        words.findFirst();
        while (!words.empty() && words.retrieve() != null) {
            String word = words.retrieve();
            invertedIndex.add(word, docId);
            bstInvertedIndex.addWord(word, docId);
            if (words.last())
                break;
            words.findNext();
        }
    }

    private static void displayIndices(Read reader, InvertedIndex invertedIndex, BSTInvertedIndex bstInvertedIndex) {
        System.out.println("\n====== Document Index ======");
        reader.index.displayDoc();

        System.out.println("\n====== LinkedList-based Inverted Index ======");
        invertedIndex.printInvIndex();

        System.out.println("\n====== BST-based Inverted Index ======");
        bstInvertedIndex.displayIndex();
    }

    private static void displayMenu(Query queryProcessorWithList, Query queryProcessorWithBST, Ranking rankingWithList, Ranking rankingWithBST) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n====== Search Engine Menu ======");
            System.out.println("1. AND Query (List)");
            System.out.println("2. OR Query (List)");
            System.out.println("3. AND Query (BST)");
            System.out.println("4. OR Query (BST)");
            System.out.println("5. Complex Query (List)");
            System.out.println("6. Complex Query (BST)");
            System.out.println("7. Ranked Query (List)");
            System.out.println("8. Ranked Query (BST)");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the remaining newline

            switch (choice) {
                case 1:
                    handleQuery(queryProcessorWithList, scanner, "AND");
                    break;
                case 2:
                    handleQuery(queryProcessorWithList, scanner, "OR");
                    break;
                case 3:
                    handleQuery(queryProcessorWithBST, scanner, "AND");
                    break;
                case 4:
                    handleQuery(queryProcessorWithBST, scanner, "OR");
                    break;
                case 5:
                    handleComplexQuery(queryProcessorWithList, scanner);
                    break;
                case 6:
                    handleComplexQuery(queryProcessorWithBST, scanner);
                    break;
                case 7:
                    handleRankingQuery(rankingWithList, scanner);
                    break;
                case 8:
                    handleRankingQuery(rankingWithBST, scanner);
                    break;
                case 9:
                    running = false;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void handleQuery(Query queryProcessor, Scanner scanner, String type) {
        System.out.println("Enter the first word:");
        String word1 = scanner.nextLine();
        System.out.println("Enter the second word:");
        String word2 = scanner.nextLine();

        String query = word1 + " " + type + " " + word2;
        LinkedList<Integer> results = queryProcessor.CheckQuery(query);
        queryProcessor.DisplayQuery(results);
    }

    private static void handleComplexQuery(Query queryProcessor, Scanner scanner) {
        System.out.println("Enter a complex query (e.g., 'data AND (structures OR algorithm)'):");
        String complexQuery = scanner.nextLine();

        LinkedList<Integer> results = queryProcessor.CheckQuery(complexQuery);
        System.out.println("\n====== Complex Query Results ======");
        queryProcessor.DisplayQuery(results);
    }

    private static void handleRankingQuery(Ranking rankingProcessor, Scanner scanner) {
        System.out.println("Enter the query:");
        String query = scanner.nextLine();

        rankingProcessor.insertSorted_inlist(query); // Process ranking
        System.out.println("\n====== Ranked Query Results ======");
        rankingProcessor.display(); // Display ranked results
    }
}
