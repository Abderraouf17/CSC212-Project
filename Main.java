import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize necessary components
        Read reader = new Read();
        InvertedIndex invertedIndex = new InvertedIndex();
        BSTInvertedIndex bstInvertedIndex = new BSTInvertedIndex();
        Ranking ranking = new Ranking(invertedIndex, reader.index);

        // Load stop words and documents
        reader.readStopWords("stop.txt");
        reader.readDocuments("dataset.csv");

        // Process documents and build both inverted indexes
        processDocuments(reader, invertedIndex, bstInvertedIndex);

        // Initialize the Query object
        Query queryProcessor = new Query(invertedIndex); // Use the desired index for querying

        // Display indices
        displayIndices(reader, invertedIndex, bstInvertedIndex);

        // Display menu and handle user input
        displayMenu(queryProcessor, ranking);
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

    private static void addWordsToIndexes(LinkedList<String> words, int docId, InvertedIndex invertedIndex,
                                          BSTInvertedIndex bstInvertedIndex) {
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

    private static void displayMenu(Query queryProcessor, Ranking ranking) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n====== Search Engine Menu ======");
            System.out.println("1. AND Query");
            System.out.println("2. OR Query");
            System.out.println("3. Complex Query");
            System.out.println("4. Ranking Query");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the remaining newline

            switch (choice) {
                case 1:
                case 2:
                    handleQuery(choice, queryProcessor, scanner);
                    break;
                case 3:
                    handleComplexQuery(queryProcessor, scanner);
                    break;
                case 4:
                    handleRankingQuery(ranking, scanner);
                    break;

                case 5:
                    running = false;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void handleQuery(int type, Query queryProcessor, Scanner scanner) {
        System.out.println("Enter the first word:");
        String word1 = scanner.nextLine();
        System.out.println("Enter the second word:");
        String word2 = scanner.nextLine();
        String query = word1 + (type == 1 ? " AND " : " OR ") + word2;
        LinkedList<Integer> results = queryProcessor.CheckQuery(query);
        queryProcessor.DisplayQuery(results);
    }

    public static void handleRankingQuery(Ranking ranking, Scanner scanner) {
        System.out.println("Enter the query:");
        String query = scanner.nextLine();
        ranking.insertSorted_inlist(query);
        ranking.display();
    }

    private static void handleComplexQuery(Query queryProcessor, Scanner scanner) {
        System.out.println("Enter a complex query (e.g., 'data AND (structures OR algorithm)'):");
        String complexQuery = scanner.nextLine();
        LinkedList<Integer> results = queryProcessor.CheckQuery(complexQuery);
        queryProcessor.DisplayQuery(results);
    }
}