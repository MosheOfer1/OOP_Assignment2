package Part_2;

import org.junit.Test;

import java.util.concurrent.*;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);
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
        logger.info(()->String.valueOf("Total Price = " + totalPrice));
        logger.info(()-> "Current maximum priority = " + customExecutor.getCurrentMax());

        Future<?>[] stringFuture = new Future[1000];
        for (int i = 0; i < stringFuture.length; i=i+2) {
            stringFuture[i] = customExecutor.submit(callable2, TaskType.IO);
            stringFuture[i+1] = customExecutor.submit(task);
        }
        for (int i = 0; i < stringFuture.length; i=i+2) {
            String s = null;
            int x = 0;
            try {
                s = (String) stringFuture[i].get();
                x = (int) stringFuture[i+1].get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            String finalS = s;
            logger.info(() -> finalS);
            int finalX = x;
            logger.info(() -> String.valueOf(finalX));

        }
        customExecutor.gracefullyTerminate();
    }
}