import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Read {
    private Array<String[]> documents;
    private LinkedList<String> stopWords;
    public Read() {
        stopWords = new LinkedList<>();
    }

    private int countDocuments(String csvPath) {
        int count = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvPath));
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) { // we shouldn't forget to close the buffer .
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }
}
