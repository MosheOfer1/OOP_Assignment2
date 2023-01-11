package Part_2;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * This is an Adapter class, which extends the FutureTask class,
 * and implements the Comparable interface.
 * The main purpose of this class is to allow the Task objects,
 * which are passed to the submit method of the CustomExecutor class,
 * to be used as elements in the priority queue that is used by the thread pool.
 *
 * The Adapter class has a single constructor,
 * which takes a Callable object, and wraps it in a Task object,
 * and assigns the taskType to priority.
 * The getPriority method returns the priority of the task,
 * which is set during the instantiation of the Adapter object
 * @param <T>
 */
public class Adapter<T> extends FutureTask<T> implements Comparable<T>{
    private Task<?> task;
    private final int priority;
    public Adapter(@NotNull Callable callable, int taskType) {
        super(callable);
        this.task = new Task<>(callable);
        this.priority = taskType;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(@NotNull T o) {
        return Integer.compare(((Adapter<T>) o).getPriority(),this.priority);
    }
}
