import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Hash {
    public static void main(String[] args) throws Exception {
        Hashtable<Integer, String[]> index = new Hashtable<>();
        Hashtable<String, ArrayList<Integer>> invertedIndex = new Hashtable<>();

        BufferedReader dataReader = new BufferedReader(new FileReader("dataset.csv"));
        dataReader.readLine();

        Set<String> stopWords = new HashSet<>(Arrays.asList(Files.readString(Path.of("stop.txt")).split("\n")));

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
                phrase.append(split[i] + ",");
            }

            String[] words = phrase.toString().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
            words = Arrays.stream(words).map(String::toLowerCase)
                    .filter(word -> !word.isEmpty() && !stopWords.contains(word)).toArray(String[]::new);

            for (String word : words) {
                invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(id);
            }

            index.put(id, words);
        }

        // System.out.println("Index:");
        // index.forEach((id, words) -> {
        // System.out.print(id + ":|");
        // for (String word : words) {
        // System.out.print(word + "|");
        // }
        // System.out.println();
        // });

        // System.out.println("\nInverted Index:");
        // invertedIndex.forEach((word, ids) -> {
        // System.out.print(word + ": ");
        // System.out.println(ids);
        // });

        Set<String> andQuery = Set.of("market", "sports");
        Set<String> orQuery = Set.of("weather", "warming");

        // AND Query
        System.out.println("AND Query:");
        Set<Integer> andResult = null;
        for (String word : andQuery) {
            if (invertedIndex.containsKey(word)) {
                Set<Integer> ids = new HashSet<>(invertedIndex.get(word));
                if (andResult == null) {
                    andResult = ids;
                } else {
                    Set<Integer> intersection = new HashSet<>();
                    for (Integer id : andResult) {
                        if (ids.contains(id)) {
                            intersection.add(id);
                        }
                    }
                    andResult = intersection;
                }
            }
        }
        System.out.println(andResult == null ? new HashSet<>() : andResult);

        // OR Query
        System.out.println("\nOR Query:");
        Set<Integer> orResult = new HashSet<>();
        for (String word : orQuery) {
            if (invertedIndex.containsKey(word)) {
                for (Integer id : invertedIndex.get(word)) {
                    if (!orResult.contains(id)) {
                        orResult.add(id);
                    }
                }
            }
        }
        System.out.println(orResult);
        System.out.println();

        List<String> query = List.of("business", "world", "market");
        Map<Integer, Integer> documentScores = new HashMap<>();

        for (String term : query) {
            if (invertedIndex.containsKey(term)) {
                ArrayList<Integer> docIds = invertedIndex.get(term);
                for (int docId : docIds) {
                    documentScores.put(docId, documentScores.getOrDefault(docId, 0) + 1);
                }
            }
        }

        List<Map.Entry<Integer, Integer>> rankedDocuments = new ArrayList<>(documentScores.entrySet());
        rankedDocuments.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // Sort in descending order

        System.out.println("Ranking Results:");
        for (Map.Entry<Integer, Integer> entry : rankedDocuments) {
            System.out.println("Document " + entry.getKey() + ": Score = " + entry.getValue());
        }
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

    // test frequency
    private static void testFrequency() {
        String content = "hello world hello";
        System.out.println(calculateTermFrequency(content, "hello")); // 2
        System.out.println(calculateTermFrequency(content, "world")); // 1
        System.out.println(calculateTermFrequency(content, "foo")); // 0
    }
}
