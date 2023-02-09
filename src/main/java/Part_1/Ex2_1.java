package Part_1;

import java.io.*;
import java.util.Random;
import java.util.concurrent.*;
/**
 * A class that provides methods for counting the number of lines in multiple text files.
 * Part one of the Assignment
 */
public class Ex2_1 {
    /**
     * Compares the run times of the different methods for counting the number of lines in multiple text files.
     *
     */
    public static void main(String[] args){
        // The file names to be compared from createTextFiles()
        String[] fileNames = createTextFiles(50000,128,512);
        long startTime, endTime;

        // Measure the run time of getNumOfLines
        startTime = System.currentTimeMillis();
        int numLines = getNumOfLines(fileNames);
        endTime = System.currentTimeMillis();
        System.out.println("Time to count "+ numLines +" lines from "+ fileNames.length +" files using getNumOfLines took: " + (endTime - startTime) + "ms");

        // Measure the run time of getNumOfLinesThreads
        startTime = System.currentTimeMillis();
        numLines = getNumOfLinesThreads(fileNames);
        endTime = System.currentTimeMillis();
        System.out.println("Time to count "+ numLines +" lines from "+ fileNames.length +" files using getNumOfLinesThreads: " + (endTime - startTime) + "ms");

        // Measure the run time of getNumOfLinesThreadPool
        startTime = System.currentTimeMillis();
        numLines = getNumOfLinesThreadPool(fileNames);
        endTime = System.currentTimeMillis();
        System.out.println("Time to count "+ numLines +" lines from "+ fileNames.length +" files using getNumOfLinesThreadPool: " + (endTime - startTime) + "ms");
    }

    /**
     * Creates n text files with random number of lines.
     * @param n the number of files to create
     * @param seed the seed for the random number generator
     * @param bound the upper bound for the number of lines in each file
     * @return an array of the names of the created files
     */
    public static String[] createTextFiles(int n, int seed, int bound) {
        String[] filesNames = new String[n];
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            filesNames[i] = "file_"+ (i+1) +".txt";
            File file = new File(filesNames[i]);
            try {
                file.createNewFile();
                // Write into the file
                int numOfLines = random.nextInt(bound);
                FileWriter myWriter = new FileWriter(filesNames[i]);
                //System.out.println(numOfLines);
                for (int j = 0; j < numOfLines; j++) {
                    myWriter.write("This is line Number " + (j+1) +"\n");
                }
                myWriter.close();

            } catch (IOException e) {
                // handle exception
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return filesNames;
    }
    /**
     * Returns the sum of lines in all the txt files given the names of the files,
     * using the straight forward naive method to count by iterate all the files.
     *
     * @param fileNames the names of the files
     * @return the sum of lines in all the txt files
     */
    public static int getNumOfLines(String[] fileNames) {
        int numLines = 0;
        for (String fileName : fileNames) {
            numLines = getNumLines(numLines, fileName);
        }
        return numLines;
    }

    public static int getNumLines(int numLines, String fileName) {
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

    /**
     * Returns the sum of lines in all the txt files given the names of the files,
     * using a separate thread for each file.
     *
     * @param fileNames the names of the files
     * @return the sum of lines in all the txt files
     */
    public static int getNumOfLinesThreads(String[] fileNames) {
        int numLines = 0;
        LineCounterThread[] threads = new LineCounterThread[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            threads[i] = new LineCounterThread(fileNames[i]);
            threads[i].start();
        }
        for (LineCounterThread thread : threads) {
            try {
                thread.join();
                numLines += thread.getNumLines();
            } catch (InterruptedException e) {
                // handle exception
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return numLines;
    }


    /**
     * Returns the sum of lines in all the txt files given the names of the files,
     * using a thread pool of the size of fileNames.length.
     *
     * @param fileNames the names of the files
     * @return the sum of lines in all the txt files
     */
    public static int getNumOfLinesThreadPool(String[] fileNames) {
        int numLines = 0;
        // Create a fixed-size thread pool with a thread for each file
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(fileNames.length);
        LineCounterCallable[] tasks = new LineCounterCallable[fileNames.length];
        Future<Integer>[] results = new Future[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            tasks[i] = new LineCounterCallable(fileNames[i]);
        }
        try {
            // Invoke all the tasks and wait for them to complete
            for (int i = 0; i < fileNames.length; i++) {
                results[i] = executor.submit(tasks[i]);
            }
            for (Future<Integer> result :results){
                numLines += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            // handle exception
            e.printStackTrace();
        }

        executor.shutdown();
        // Wait for all the tasks to complete
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // handle exception
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return numLines;
    }
}

