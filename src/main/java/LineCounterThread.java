import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * A thread class that counts the number of lines in a text file.
 */
public class LineCounterThread extends Thread {
    // The file name to be processed by this thread
    private String fileName;

    // The number of lines counted by this thread
    private int numLines;

    /**
     * Constructs a new LineCounterThread for the given file name.
     *
     * @param fileName the file name to be processed by this thread
     */
    public LineCounterThread(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the number of lines counted by this thread.
     *
     * @return the number of lines counted by this thread
     */
    public int getNumLines() {
        return numLines;
    }

    /**
     * Counts the number of lines in the file specified in the constructor.
     * Only processes files that end with the ".txt" extension.
     */
    @Override
    public void run() {
        numLines = Ex2_1.getNumLines(numLines, fileName);
    }
}
