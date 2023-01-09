import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * A generic task with a type that returns a result and may throw an exception.
 * Each task has a priority used for scheduling, based on the TaskType enum.
 */
public class Task<T> extends FutureTask<T> implements Callable<T>,Comparable<T>{
    private final Callable<T> task;
    private final TaskType priority;


    /**
     * Constructs a new prioritized task.
     *
     * @param task the Callable representing the task
     */
    public Task(Callable<T> task) {
        super(task);
        if(!(task instanceof Task<T>))
            this.priority = (TaskType.COMPUTATIONAL);
        else
            this.priority = ((Task<Object>) task).priority;

        this.task = task;
    }
    /**
     * Constructs a new prioritized task.
     *
     * @param task the Callable representing the task
     * @param priority the priority of the task
     */
    public Task(Callable<T> task, TaskType priority) {
        super(task);
        this.task = task;
        this.priority = priority;
    }

    public static Task<?> createTask(Callable<?> c, TaskType taskType) {
        return new Task(c,taskType);
    }

    /**
     * Performs the task and returns the result.
     *
     * @return the result of the task
     * @throws Exception if an exception occurs while performing the task
     */
    public T call() throws Exception {
        return this.task.call();
    }

    /**
     In order the compare between two tasks, compare their priority value
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task<?> task)) return false;
        return this.task == task && priority.getPriorityValue() == task.priority.getPriorityValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, priority);
    }

    /**
     * Returns the priority of the task.
     *
     * @return the priority of the task in integer
     */
    public int getPriority() {
        return priority.getPriorityValue();
    }

    @Override
    public int compareTo(@NotNull T o) {
        return Integer.compare(((Task<?>) o).getPriority(),this.priority.getPriorityValue());
    }
}
