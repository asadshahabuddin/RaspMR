/**
 * Author : Asad Shahabuddin
 * File   : TaskTracker.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.task;

/* Import list */
import java.io.IOException;
import com.rasp.interfaces.Task;
import com.rasp.interfaces.ReducerTask;

public class TaskScheduler
	implements com.rasp.interfaces.TaskScheduler
{
	public static int taskIdx = 1;
	public static TaskTracker taskTracker = new TaskTracker();
	
    @Override
    public boolean schedule()
		throws IllegalAccessException, InstantiationException,
			   InterruptedException,   IOException
    {
    	
    	Task task = taskTracker.nextTask();
    	if(task instanceof MapperTask)
    	{
    		return scheduleMapper((MapperTask) task);
    	}
    	else if(task instanceof ReducerTask)
    	{
    		return scheduleReducer((ReducerTask) task);
    	}
    	return false;
    	
    	/*
    	MapperTask mapperTask = (MapperTask) t.remove();
        mapperTask.setTaskInputSplit(null);  // TODO : Ask Rahul
        mapperTask.setMapoerClass();      // TODO : Ask Rahul
        mapperTask.execute();
        */
    }
    
    public boolean scheduleMapper(MapperTask task)
    {
    	
    	return false;
    }
    
    public boolean scheduleReducer(ReducerTask task)
    {
    	// TODO
    	return false;
    }
}
/* End of TaskScheduler.java */