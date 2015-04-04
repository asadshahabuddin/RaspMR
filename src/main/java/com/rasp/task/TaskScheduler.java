package com.rasp.task;

import com.rasp.interfaces.Task;

import java.util.Queue;

/**
 * Created by Pulkit on 4/4/15.
 */
public class TaskScheduler implements com.rasp.interfaces.TaskScheduler {

    @Override
    public boolean scheduleTask() {
        Queue t = TaskTracker.getTaskList();
        MapperTask mapperTask = (MapperTask) t.remove();
        // mapperTask.setTaskInputSplit(...);
        // mapperTask.setMapoerClass(...);
        // mapperTask.execute(...);
        return true;
    }
}
