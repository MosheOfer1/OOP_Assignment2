import java.io.*;
import java.util.Random;
import java.util.concurrent.*;

public class Ex2_1 {
    public static void main(String[] args) {
        // The file names to be compared from createTextFiles()
        String[] fileNames = createTextFiles(1000,2,1500);
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
    public static int getNumOfLines(String[] fileNames) {
        int numLines = 0;
        for (String fileName : fileNames) {
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

