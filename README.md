# OOP_Assignment2 Part 1 - Line Counter
This part contains three methods for counting the number of lines in a set of text files: 
- getNumOfLines 
- getNumOfLinesThreads
- getNumOfLinesThreadPool.

## getNumOfLines
The getNumOfLines method processes the files sequentially, reading each file one by one and counting the number of lines in each file. It is the simplest and most straightforward of the three methods, but it may take a long time to process a large number of files.

## getNumOfLinesThreads
The getNumOfLinesThreads method uses a separate thread for each file. It creates an array of Part_1.LineCounterThread objects, one for each file, and starts each thread. It then waits for all the threads to complete by calling the join method on each of them. 
Finally, it adds up the number of lines counted by each thread and returns the total.

Using separate threads allows the files to be processed concurrently, potentially speeding up the process. However, creating and starting a large number of threads can be resource-intensive, and the overhead of managing multiple threads may outweigh the benefits of concurrent processing.

## getNumOfLinesThreadPool
The getNumOfLinesThreadPool method uses a thread pool to process the files concurrently. It creates a fixed-size thread pool with a thread for each file, and a separate Part_1.LineCounterCallable for each file. It then invokes all the tasks and waits for them to complete. 
Finally, it shuts down the thread pool and waits for all the tasks to complete by calling the awaitTermination method. It then returns the total number of lines counted by all the tasks.

Using a thread pool allows the files to be processed concurrently while minimizing the overhead of managing multiple threads. However, creating a thread pool requires some setup and may involve additional overhead compared to using separate threads.
## Class diagram
![OOP_Assignment2](https://user-images.githubusercontent.com/107894139/211216831-3562525f-10c8-4e2f-958f-e77c5df05a60.png)

<div style="display: flex; flex-wrap: wrap; width: 1300px">
  <table style="width: 400px">
    <tr>
      <th>Class</th>
      <td>Part_1.LineCounterThread</td>
    </tr>
    <tr>
      <th>Attributes</th>
      <td>
        <ul>
          <li>String fileName</li>
          <li>int numLines</li>
        </ul>
      </td>
    </tr>
    <tr>
      <th>Methods</th>
      <td>
        <ul>
          <li>+ Part_1.LineCounterThread(String)</li>
          <li>+ void run()</li>
          <li>+ int getNumLines()</li>
        </ul>
      </td>
    </tr>
  </table>

  <table style="width: 400px">
    <tr>
      <th>Class</th>
      <td>Part_1.LineCounterCallable</td>
    </tr>
    <tr>
      <th>Attributes</th>
      <td>
        <ul>
          <li>String fileName</li>
        </ul>
      </td>
    </tr>
    <tr>
      <th>Methods</th>
      <td>
        <ul>
          <li>+ Part_1.LineCounterCallable(String)</li>
          <li>+ Integer call()</li>
        </ul>
      </td>
    </tr>
  </table>

<table style="width: 400px">
  <tr>
    <th>Class</th>
    <td>LineCounter</td>
  </tr>
  <tr>
    <th>Attributes</th>
    <td></td>
  </tr>
  <tr>
    <th>Methods</th>
    <td>
      <ul>
        <li>static int getNumOfLines(String[])</li>
        <li>static int getNumOfLinesThreads(String[])</li>
        <li>static int getNumOfLinesThreadPool(String[])</li>
      </ul>
    </td>
  </tr>
</table>

</div>






### Conclusions
In general, using a thread pool is a good choice for concurrent processing when you have a large number of tasks to be processed. It allows you to take advantage of concurrent processing while minimizing the overhead of managing multiple threads. However, for small numbers of tasks, the overhead of creating a thread pool may outweigh the benefits of concurrent processing. In such cases, using separate threads or processing the tasks sequentially may be more efficient.


<table>
  <tr>
    <th>Method</th>
    <th>Run Time (ms)</th>
    <th>Seed, Bound</th>
    <th>Num of Files</th>
    <th>Total Num of Lines</th>
  </tr>
  <tr>
    <td>getNumOfLines</td>
    <td>3735</td>
    <td>2, 1500</td>
    <td>1000</td>
    <td>733592</td>
  </tr>
  <tr style="color:#ff0000">
    <td>getNumOfLinesThreads</td>
    <td>148</td>
    <td>2, 1500</td>
    <td>1000</td>
    <td>733592</td>
  </tr>
  <tr>
    <td>getNumOfLinesThreadPool</td>
    <td>280</td>
    <td>2, 1500</td>
    <td>1000</td>
    <td>733592</td>
  </tr>
</table>

The table above shows the run times of our local experiment for counting the number of lines in a specific set of text files.
The run times may vary depending on the resources available on the machine running the code,the input data, and other factors.

# OOP_Assignment2 Part 2 - ThreadPool
In order to create an option to control the priority of tasks in a threadPool in java, we created 
* The Task class that represent a Callable task that return a value and may throw an Exception, wite a priority enum.
* The CustomExecutor that control as Threadpool Executor with the option to control the priority of all tasks.
* The Adapter

## **SOLID principles**

The Adapter design pattern reflects the Dependency Inversion Principle in the provided code by decoupling the Task class from the CustomExecutor class and the priority queue.

The CustomExecutor class uses the Adapter class to submit tasks to the priority queue. The Adapter class wraps the Task object and implements the compareTo method, which allows the task object to be added to the PriorityBlockingQueue and be sorted based on their priority. This way the CustomExecutor does not need to know the implementation details of how the Task class is implemented and it does not depend on it. The CustomExecutor only needs to know that the task submitted is of Adapter type, which implements the Comparable interface.

## Task
This is a generic Task class that represents a task with a type that returns a result and may throw an exception. Each task has a priority used for scheduling, based on the TaskType enum.
## CustomExecutor
This is a custom thread pool class that defines methods for submitting tasks to a priority queue.
The queue is a PriorityBlockingQueue object that stores the tasks in the priority queue. The queue is sorted according to the not natural ordering of the tasks or by the TaskType comparator.
## Adapter
The Adapter class, extends the FutureTask class, and implements the Comparable interface. The main purpose of this class is to allow the Task objects, which are passed to the submit method of the CustomExecutor class, to be used as elements in the priority queue that is used by the thread pool.

The Adapter class has a single constructor, which takes a Callable object, and wraps it in a Task object, and assigns the taskType to priority. The getPriority method returns the priority of the task, which is set during the instantiation of the Adapter object.

The compareTo method is used to compare Adapter objects based on their priority.

This class wraps the task object and implements the compareTo method to allow the task object to be added to the PriorityBlockingQueue and be sorted based on their priority.
