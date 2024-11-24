public class Ranking {

    class Doc_Rank {
        int doc_id;
        int rank;

        public Doc_Rank(int doc_id, int rank) {
            this.doc_id = doc_id;
            this.rank = rank;
        }

        public void display() {
            System.out.println("Document ID: " + doc_id + " Rank: " + rank);
        }
    }

    private InvertedIndex invertedIndex;
    private BSTInvertedIndex bstInvertedIndex;
    private Index index;
    private boolean useBST; // Flag to determine which index type is used
    private LinkedList<Integer> doc_ids;
    private LinkedList<Doc_Rank> doc_ranks;

    // Constructor for InvertedIndex
    public Ranking(InvertedIndex invertedIndex, Index index) {
        this.invertedIndex = invertedIndex;
        this.index = index;
        this.useBST = false; // Use List-based index
        this.doc_ranks = new LinkedList<Doc_Rank>();
        this.doc_ids = new LinkedList<Integer>();
    }

    // Constructor for BSTInvertedIndex
    public Ranking(BSTInvertedIndex bstInvertedIndex, Index index) {
        this.bstInvertedIndex = bstInvertedIndex;
        this.index = index;
        this.useBST = true; // Use BST-based index
        this.doc_ranks = new LinkedList<Doc_Rank>();
        this.doc_ids = new LinkedList<Integer>();
    }

    public void display() {
        if (doc_ranks.empty()) {
            System.out.println("No documents found");
            return;
        }
        System.out.println();

        doc_ranks.findFirst();
        while (!doc_ranks.last()) {
            doc_ranks.retrieve().display();
            doc_ranks.findNext();
        }
        doc_ranks.retrieve().display();
    }

    public Document getDocument(int id) {
        return index.getDocument(id);
    }

    public int word_freq(Document d, String word) {
        int count = 0;
        LinkedList<String> tokens = d.words;
        if (tokens.empty()) {
            return 0;
        }
        tokens.findFirst();
        while (!tokens.last()) {
            if (tokens.retrieve().equalsIgnoreCase(word)) {
                count++;
            }
            tokens.findNext();
        }
        if (tokens.retrieve().equalsIgnoreCase(word)) {
            count++;
        }
        return count;
    }

    public int getScore(Document d, String Q) {
        if (Q.length() == 0) {
            return 0;
        }
        String[] words = Q.split(" ");
        int score = 0;
        for (int i = 0; i < words.length; i++) {
            score += word_freq(d, words[i].trim().toLowerCase());
        }
        return score;
    }

    public void RankQ(String Q) {

        if (Q.length() == 0) {
            return;
        }
        String[] words = Q.split("\\s+");
        doc_ids = new LinkedList<>();

        for (String word : words) {
            LinkedList<Integer> A;
            if (useBST) {
                Word w = bstInvertedIndex.searchWord(word.trim().toLowerCase());
                A = (w != null) ? w.indexs : new LinkedList<Integer>();
            } else {
                boolean found = invertedIndex.search(word.trim().toLowerCase());
                A = (found) ? invertedIndex.invIndex.retrieve().indexs : new LinkedList<Integer>();

            }

            Sort(A);
        }
    }

    public void Sort(LinkedList<Integer> A) {
        if (A.empty()) {
            return;
        }
        A.findFirst();
        while (!A.empty()) {
            boolean found = existsIn(doc_ids, A.retrieve());
            if (!found) {
                doc_ids.insert(A.retrieve());
            }
            if (!A.last()) {
                A.findNext();
            } else {
                break;
            }

        }
    }

    public boolean existsIn(LinkedList<Integer> list, int var) {
        if (list.empty()) {
            return false;
        }
        list.findFirst();
        while (!list.last()) {
            if (list.retrieve() == var) {
                return true;
            }
            list.findNext();
        }
        if (list.retrieve() == var) {
            return true;
        }
        return false;
    }

    public void insertId(int id) {
        if (doc_ids.empty()) {
            doc_ids.insert(id);
            return;
        }
        doc_ids.findFirst();
        while (!doc_ids.last()) {

            if (id < doc_ids.retrieve()) {
                Integer tempid = doc_ids.retrieve();
                doc_ids.update(id);
                doc_ids.insert(tempid);
                return;
            } else {
                doc_ids.findNext();
            }
        }
        if (id < doc_ids.retrieve()) {
            Integer tempid = doc_ids.retrieve();
            doc_ids.update(id);
            doc_ids.insert(tempid);
            return;
        } else {
            doc_ids.insert(id);
        }
    }

    public void Insert_Sorted_list(Doc_Rank doc) {
        if (doc_ranks.empty()) {
            doc_ranks.insert(doc);
            return;
        }
        doc_ranks.findFirst();
        while (!doc_ranks.last()) {
            if (doc_ranks.retrieve().doc_id == doc.doc_id) {
                return;
            }
            if (doc.rank > doc_ranks.retrieve().rank) {
                Doc_Rank temp = doc_ranks.retrieve();
                doc_ranks.update(doc);
                doc_ranks.insert(temp);
                return;
            } else {
                doc_ranks.findNext();
            }
        }
        if (doc.rank > doc_ranks.retrieve().rank) {
            Doc_Rank temp = doc_ranks.retrieve();
            doc_ranks.update(doc);
            doc_ranks.insert(temp);
            return;
        } else {
            doc_ranks.insert(doc);
        }

    }

    public void insertSorted_inlist(String query) {
        RankQ(query);
        doc_ranks = new LinkedList<>();
        if (doc_ids.empty()) {
            return;
        }

        doc_ids.findFirst();
        while (!doc_ids.last()) {
            Document d = index.getDocument(doc_ids.retrieve());
            int score = getScore(d, query);
            Insert_Sorted_list(new Doc_Rank(doc_ids.retrieve(), score));
            doc_ids.findNext();
        }
        Document d = index.getDocument(doc_ids.retrieve());
        int score = getScore(d, query);
        Insert_Sorted_list(new Doc_Rank(doc_ids.retrieve(), score));

    }
}
