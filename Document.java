
public class Document {
    int id;
    String tokens;
    LinkedList<String> words;

    public Document(int id, String tokens, LinkedList<String> words) {
        this.id = id;
        this.tokens = tokens;
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public String getTokens() {
        return tokens;
    }

    public LinkedList<String> getWords() {
        return words;
    }
}
