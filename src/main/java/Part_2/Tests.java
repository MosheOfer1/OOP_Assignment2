package Part_2;

import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

    /**
     * This test method creates a new instance of the CustomExecutor class and creates
     * two Callable objects that simulate long-running tasks.
     * The test then submits half of the tasks with a TaskType.OTHER priority,
     * and the other half with a TaskType.COMPUTATIONAL priority.
     * This test is checking that the CustomExecutor is able to schedule
     * the task based on their priority with TaskType.OTHER being less important,
     * and TaskType.COMPUTATIONAL more important, and this can be observed by
     * checking the priority value of each element in the queue.
     *
     * In short the more important tasks overtook the lees important tasks!
     */
    @Test
    public void testPriority(){
        CustomExecutor customExecutor = new CustomExecutor();

        Callable<String> callable1 = ()-> {
            //simulating long task
            Thread.sleep(1000);
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };

        Callable<Integer> callable2 = ()-> {
            //simulating long task
            Thread.sleep(1000);
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        };

        // Create an array to store the Future of the tasks in a const size
        Future<?>[] stringFuture = new Future[20];
        for (int i = 0; i < stringFuture.length; i++) {
            // Insert the less important tasks first
            if (i <= stringFuture.length/2)
                stringFuture[i] = customExecutor.submit(callable1, TaskType.OTHER);
            if (i == 2) {
                logger.info(()-> "At the beginning the first in the queue has priority of = " + customExecutor.getCurrentMax());
            }
            // Insert the more important tasks in the end
            else
                stringFuture[i] = customExecutor.submit(callable2, TaskType.COMPUTATIONAL);
        }
        //log the priority of each task in the queue
        Object[] array = customExecutor.getQueue().toArray();
        for (int i = 0; i < customExecutor.getQueue().size(); i++) {
            int finalI = i;
            Object[] finalArray1 = array;
            logger.info(()-> "The "+ finalI +"t'h element in the queue has priority of "+((Adapter<?>) finalArray1[finalI]).getPriority());
        }
        logger.info(()-> "Current maximum priority = " + customExecutor.getCurrentMax());

        // Sleep for 2 sec and check again the queue status
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        array = customExecutor.getQueue().toArray();
        for (int i = 0; i < customExecutor.getQueue().size(); i++) {
            int finalI = i;
            Object[] finalArray = array;
            logger.info(()-> "The "+ finalI +"t'h element in the queue after sleep, has priority of "+((Adapter<?>) finalArray[finalI]).getPriority());
        }
        logger.info(()-> "Current maximum priority = " + customExecutor.getCurrentMax());

        customExecutor.gracefullyTerminate();
        logger.info(()-> "Current maximum priority = " + customExecutor.getCurrentMax());

    }

    @Test
    public void partialTest(){
       CustomExecutor customExecutor = new CustomExecutor();
        var task = Task.createTask(()->{
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        var sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = (int) sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(()-> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = ()-> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = ()-> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };

        var priceTask = customExecutor.submit(()-> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(()-> "Reversed String = " + reversed);
        logger.info(()-> String.valueOf("Total Price = " + totalPrice));
        logger.info(()-> "Current maximum priority = " + customExecutor.getCurrentMax());

        customExecutor.gracefullyTerminate();
    }

}