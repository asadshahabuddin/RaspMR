/**
 * Author : Pulkit Jain / Asad Shahabuddin
 * File   : TaskTracker.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.task;

/* Import list */
import java.util.Queue;
import com.rasp.interfaces.Task;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskTracker
	implements com.rasp.interfaces.TaskTracker
{
    private Queue<Task> taskQueue = new LinkedBlockingQueue<>();

    @Override
    public void submit(Task task)
    {
        taskQueue.add(task);
    }

    @Override
    public Task nextTask()
    {
        return taskQueue.poll();
    }
}
/* End of TaskTracker.java */