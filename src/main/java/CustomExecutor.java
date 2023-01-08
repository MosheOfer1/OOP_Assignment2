import java.util.Comparator;
import java.util.concurrent.*;

/**
 * A custom thread pool that defines methods for submitting tasks to a priority queue.
 */
public class CustomExecutor{

    private final ThreadPoolExecutor executor;
    private int max;
    public CustomExecutor() {
        this.max = 10;
        int processors = Runtime.getRuntime().availableProcessors();
        BlockingQueue queue = new PriorityBlockingQueue<>(10, Comparator.reverseOrder());
        this.executor = new ThreadPoolExecutor(processors/2,processors-1,
                300,TimeUnit.MILLISECONDS, queue);
    }
    public <T> Future<T> submit(Callable<T> callable) {
       //Creating a Task instance if needed
        if (!(callable instanceof Task))
            callable = new Task<>(callable);
        if (this.max > ((Task<?>)callable).getPriority())
            this.max = ((Task<?>)callable).getPriority();
        return executor.submit(callable);
    }
   public <T> Future<T> submit(Callable<T> callable,TaskType taskType) {
        //Creating a Task instance
       Task<T> task = new Task<>(callable,taskType);
       return submit(task);
    }


    public void gracefullyTerminate() {
        try {
            executor.awaitTermination(300,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentMax() {
        return String.valueOf(this.max);
    }
}
