import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
/**
 * A callable class that counts the number of lines in a text file.
 */
public class LineCounterCallable implements Callable<Integer> {
    // The file name to be processed by this callable
    private String fileName;

    /**
     * Constructs a new LineCounterCallable for the given file name.
     *
     * @param fileName the file name to be processed by this callable
     */
    public LineCounterCallable(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Counts the number of lines in the file specified in the constructor.
     * Only processes files that end with the ".txt" extension.
     *
     * @return the number of lines in the file
     */
    @Override
    public Integer call() {
        int numLines = 0;
        if (fileName.endsWith(".txt")) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                while (reader.readLine() != null) {
                    numLines++;
                }
            } catch (IOException e) {
                // handle exception
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return numLines;
    }
}
