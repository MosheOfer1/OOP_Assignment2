import java.util.Comparator;
import java.util.concurrent.*;

/**
 * A custom thread pool that defines methods for submitting tasks to a priority queue.
 */
public class CustomExecutor{

    private final ThreadPoolExecutor executor;
    private final BlockingQueue queue = new PriorityBlockingQueue<>(11, Comparator.reverseOrder());
    private int max;
    public CustomExecutor() {
        this.max = 10;
        int processors = Runtime.getRuntime().availableProcessors();
        this.executor = new ThreadPoolExecutor(processors/2,processors-1,
                300,TimeUnit.MILLISECONDS, queue);
    }
    public <T> FutureTask<T> submit(Task<T> task){
        Adapter<T> adapter = new Adapter<>(task,task.getPriority());
        executor.execute(adapter);
        return adapter;
    }
    public <T> FutureTask<T> submit(Callable<T> callable){
        return submit(new Task<>(callable));
    }

    public <T> FutureTask<T> submit(Callable<T> callable,TaskType taskType){
        return submit(new Task<>(callable,taskType));
    }


    public void gracefullyTerminate() {
        try {
            // Wait for the queue to be empty
            while (!queue.isEmpty());
            executor.awaitTermination(300,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentMax() {
        return String.valueOf(this.max);
    }

}
