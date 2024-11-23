
import java.io.*;
import java.util.*;

public class Read {

    LinkedList<String> stopWords;
    LinkedList<String> words = new LinkedList<>();
    Index index;
    InvertedIndex invIndex;

    int num = 0;

    public Read() {

        stopWords = new LinkedList<>();
        index = new Index();
        invIndex = new InvertedIndex();
    }

    public void readStopWords(String filename) {
        try {
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                stopWords.insert(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readDocuments(String filename) {
        String line;
        try {
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            sc.nextLine();
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.trim().length() < 3) {

                    break;
                }
                String x = line.substring(0, line.indexOf(","));
                int y = Integer.parseInt(x.trim());
                System.out.println("Document ID: " + y);
                String z = line.substring(line.indexOf(",") + 1).trim();
                System.out.println("Content : " + z);

                LinkedList<String> words = new LinkedList<>();

                index.addDocument(new Document(y, z, words));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<String> MakeInverted(String content, int id) {
        LinkedList<String> words_inDoc = new LinkedList<>();

        // Tokenize and clean content
        content = content.replaceAll("[^a-zA-Z0-9\\s]", "");
        content = content.toLowerCase();
        String[] tokens = content.split("\\s+");

        for (String token : tokens) {
            if (!stopWords.exist(token)) {
                if (!words.exist(token)) {
                    words.insert(token);
                }
                if (!words_inDoc.exist(token)) {
                    words_inDoc.insert(token);
                    invIndex.add(token, id);
                }
            }
        }

        return words_inDoc;
    }


    public boolean is_exist(String var) {
        if (!stopWords.empty() && stopWords != null) {
            stopWords.findFirst();
            while (!stopWords.last()) {
                if (stopWords.retrieve().equals(var))
                    return true;
                stopWords.findNext();
            }
            if (stopWords.retrieve().equals(var))
                return true;
        }
        return false;
    }

    public void LoadAll(String Stop, String filename) {
        readStopWords(Stop);
        readDocuments(filename);
    }

    public void display(LinkedList<Integer> list) {
        if (list.empty()) {
            System.out.println("No doc here");
            return;
        }
        list.findFirst();
        while (!list.last()) {
            Document d = index.getDocument(list.retrieve());
            if (d != null) {
                System.out.println("Document ID: " + d.id + " : " + d.tokens + " ");
            }
            list.findNext();
        }

        Document d = index.getDocument(list.retrieve());
        if (d != null) {
            System.out.print("Document ID: " + d.getId() + " : " + d.getTokens() + " ");
        }
        System.out.println("");
    }



}
