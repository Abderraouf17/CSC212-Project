
public class Word {
    String word;
    LinkedList<Integer> indexs;

    public Word(String word) {
        indexs = new LinkedList<Integer>();
        this.word = word;
    }

    public void display() {
        System.out.println("\n-----------------");
        System.out.print("Word: " + word);
        System.out.print("[");
        indexs.display();
        System.out.println("]");

    }

    public void add_ID(int docId) {
        if (!indexs.exist(docId)) { // Ensure no duplicate document IDs
            indexs.insert(docId);
        }
    }

    public boolean is_exist(Integer var) {
        if (indexs.empty())
            return false;

        indexs.findFirst();
        while (!indexs.last()) {
            if (indexs.retrieve().equals(var))
                return true;
            indexs.findNext();
        }
        if (indexs.retrieve().equals(var)) {
            return true;
        }
        return false;
    }

}
