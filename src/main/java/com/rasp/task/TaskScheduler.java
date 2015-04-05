package com.rasp.task;

/* Import list */
import java.util.Queue;
import java.io.IOException;
import com.rasp.interfaces.Task;

/**
 * Created by Pulkit on 4/4/15.
 */
public class TaskScheduler implements com.rasp.interfaces.TaskScheduler
{
    @Override
    public boolean scheduleTask()
		throws IllegalAccessException, InstantiationException,
			   InterruptedException,   IOException
    {
        Queue<Task> t = TaskTracker.getTaskList();
        MapperTask mapperTask = (MapperTask) t.remove();
        mapperTask.setTaskInputSplit(null);  // TODO : Ask Rahul
        // mapperTask.setMapoerClass();      // TODO : Ask Rahul
        mapperTask.execute();
        return true;
    }
}
