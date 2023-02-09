package Part_2;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecuteExtended extends ThreadPoolExecutor {
    private volatile Runnable maxTask;

    public ThreadPoolExecuteExtended(int corePoolSize, int maximumPoolSize, long keepAliveTime, @NotNull TimeUnit unit, @NotNull BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        synchronized (ThreadPoolExecuteExtended.class){
            this.maxTask = getQueue().peek();
        }
    }


    public Runnable getMaxTask() {
        return maxTask;
    }
}
