import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Hash {
    public static void main(String[] args) throws Exception {
        Hashtable<Integer, String[]> index = new Hashtable<>();
        Hashtable<String, ArrayList<Integer>> invertedIndex = new Hashtable<>();

        BufferedReader dataReader = new BufferedReader(new FileReader("dataset.csv"));
        dataReader.readLine();

        // Load stop words
        Set<String> stopWords = new HashSet<>(Arrays.asList(Files.readString(Path.of("stop.txt")).split("\n")));

        // Build indexes
        while (true) {
            String line = dataReader.readLine();
            if (line == null)
                break;

            String[] split = line.split(",");
            if (line.trim().isEmpty() || split.length < 2 || split[0].trim().isEmpty())
                continue;

            Integer id = Integer.parseInt(split[0].trim());
            StringBuilder phrase = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                phrase.append(split[i]).append(",");
            }

            String[] words = phrase.toString().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
            words = Arrays.stream(words).map(String::toLowerCase)
                    .filter(word -> !word.isEmpty() && !stopWords.contains(word)).toArray(String[]::new);

            for (String word : words) {
                invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(id);
            }

            index.put(id, words);
        }

        // User input for queries
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose Query Type:");
            System.out.println("1. AND Query");
            System.out.println("2. OR Query");
            System.out.println("3. Ranked Query");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 4) {
                System.out.println("Exiting the program...");
                break;
            }

            System.out.print("Enter your query terms (comma-separated): ");
            String[] queryTerms = scanner.nextLine().split(",");

            switch (choice) {
                case 1:
                    System.out.println("\nAND Query:");
                    Set<Integer> andResult = null;
                    for (String word : queryTerms) {
                        word = word.trim().toLowerCase();
                        if (invertedIndex.containsKey(word)) {
                            Set<Integer> ids = new HashSet<>(invertedIndex.get(word));
                            if (andResult == null) {
                                andResult = ids;
                            } else {
                                andResult.retainAll(ids);
                            }
                        }
                    }
                    System.out.println(andResult == null ? new HashSet<>() : andResult);
                    break;

                case 2: // OR Query
                    System.out.println("\nOR Query:");
                    Set<Integer> orResult = new HashSet<>();
                    for (String word : queryTerms) {
                        word = word.trim().toLowerCase();
                        if (invertedIndex.containsKey(word)) {
                            orResult.addAll(invertedIndex.get(word));
                        }
                    }
                    System.out.println(orResult);
                    break;

                case 3: // Ranked Query
                    System.out.println("\nRanked Query:");
                    Map<Integer, Integer> documentScores = new HashMap<>();

                    for (String term : queryTerms) {
                        term = term.trim().toLowerCase();
                        if (invertedIndex.containsKey(term)) {
                            ArrayList<Integer> docIds = invertedIndex.get(term);
                            for (int docId : docIds) {
                                documentScores.put(docId, documentScores.getOrDefault(docId, 0) + 1);
                            }
                        }
                    }

                    List<Map.Entry<Integer, Integer>> rankedDocuments = new ArrayList<>(documentScores.entrySet());
                    rankedDocuments.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // Sort by score

                    for (Map.Entry<Integer, Integer> entry : rankedDocuments) {
                        System.out.println("Document " + entry.getKey() + ": Score = " + entry.getValue());
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static int calculateTermFrequency(String content, String term) {
        int count = 0;
        String[] words = content.split("\\W+");
        for (String word : words) {
            if (word.equals(term)) {
                count++;
            }
        }
        return count;
    }


}
