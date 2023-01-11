package Part_2;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecuterExtended extends ThreadPoolExecutor {
    private Runnable maxTask;

    public ThreadPoolExecuterExtended(int corePoolSize, int maximumPoolSize, long keepAliveTime, @NotNull TimeUnit unit, @NotNull BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        this.maxTask = getQueue().peek();
        //System.out.println(maxTask);
    }

    public Runnable getMaxTask() {
        return maxTask;
    }
}
