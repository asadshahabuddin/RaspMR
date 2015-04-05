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
    private Queue<Task> taskList = new LinkedBlockingQueue<>();

    /**
     * Get the task list.
     * @return
     *            The task list.
     */
    public Queue<Task> getTaskList()
    {
        return taskList;
    }

    @Override
    public void submit(Task task)
    {
        taskList.add(task);
    }

    @Override
    public Task nextTask()
    {
        return taskList.poll();
    }
}
/* End of TaskTracker.java */