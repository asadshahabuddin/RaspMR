/**
 * Author : Pulkit Jain / Asad Shahabuddin
 * File   : TaskTracker.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import java.util.Map;
import java.util.Queue;

import com.rasp.mr.MapContext;
import com.rasp.mr.Task;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskTracker implements com.rasp.mr.TaskTracker{
    private Queue<Task> taskQueue = new LinkedBlockingQueue<>();
    private Map<String,Task> taskMap;

    @Override
    public void submit(Task task){
        taskMap.put(task.getTaskId(),task);
        taskQueue.add(task);
    }

    @Override
    public Task nextTask()
    {
        return taskQueue.poll();
    }

}
/* End of TaskTracker.java */