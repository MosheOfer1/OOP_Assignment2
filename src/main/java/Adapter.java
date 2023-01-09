import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
