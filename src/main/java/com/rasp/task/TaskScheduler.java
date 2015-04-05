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
import com.rasp.interfaces.TaskTracker;

public class TaskScheduler
	implements com.rasp.interfaces.TaskScheduler, Runnable
{
	private TaskTracker taskTracker;

    public TaskScheduler(TaskTracker taskTracker) {
        this.taskTracker = taskTracker;
    }

    @Override
    public boolean schedule()
		throws IllegalAccessException, InstantiationException,
			   InterruptedException,   IOException
    {
    	Task task = taskTracker.nextTask();
    	if(task == null)
    	{
    		return false;
    	}
    	return task.execute();
    }

	@Override
	public void run()
	{
		boolean status = false;
		
		try
		{
			while(true)
			{
				if(status != schedule())
				{
					status = !status;
					if(status)
					{
						System.out.println("   [echo] Task scheduler has resumed scheduling");
					}
					else
					{
						System.out.println("   [echo] There are no tasks to schedule");
					}
				}
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch(InterruptedException intre)
		{
			intre.printStackTrace();
		}
		catch(InstantiationException inste)
		{
			inste.printStackTrace();
		}
		catch(IllegalAccessException iae)
		{
			iae.printStackTrace();
		}
	}
}
/* End of TaskScheduler.java */