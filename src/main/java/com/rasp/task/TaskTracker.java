/**
 * Author : Pulkit Jain
 * File   : TaskTracker.java
 * Email  : 
 * Created: 
 * Edited : 
 */

package com.rasp.task;

import com.rasp.interfaces.Task;

import java.util.PriorityQueue;
import java.util.Queue;

public class TaskTracker implements com.rasp.interfaces.TaskTracker{
    private static Queue<Task> taskList = new PriorityQueue<Task>();

    public static Queue<Task> getTaskList()
    {
        return taskList;
    }

    @Override
    public void submit(Task task) {
        // adding a task in the queue
        taskList.add(task);
    }

    @Override
    public Task nextTask() {
        return null;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    // TODO

}
/* End of TaskTracker.java */