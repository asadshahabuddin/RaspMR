/**
 * Author : Asad Shahabuddin, Rahul Madhavan
 * File   : TaskTracker.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import java.io.IOException;

import com.google.protobuf.ServiceException;
import com.rasp.config.MasterConfiguration;
import com.rasp.config.SlaveConfiguration;
import com.rasp.mr.MapperTask;
import com.rasp.mr.ShuffleTask;
import com.rasp.mr.Task;
import com.rasp.mr.TaskTracker;
import com.rasp.mr.ReducerTask;

public class TaskScheduler implements com.rasp.mr.TaskScheduler, Runnable{
	private TaskTracker taskTracker;
    private SlaveConfiguration configuration;

    public TaskScheduler(TaskTracker taskTracker, SlaveConfiguration configuration) {
        this.taskTracker = taskTracker;
        this.configuration = configuration;

    }

    @Override
    public boolean schedule()
            throws IllegalAccessException, InstantiationException,
            InterruptedException, IOException, ServiceException {

    	Task task = taskTracker.nextTask();
    	if(task == null)
    	{
    		return false;
    	}
        if(task instanceof MapperTask){
            task.execute();
            try {
                configuration.getJobNode().
                        mapCompleted(task.getTaskId(),
                                ((MapperTask) task).getMapContext().getKeyCountMap());
            } catch (ServiceException e) {
                e.printStackTrace();
            }

        }else if(task instanceof ShuffleTask){
            task.execute();
            try {
                configuration.getJobNode().
                        shuffleDataTransferCompleted(task.getTaskId());

            } catch (ServiceException e) {
                e.printStackTrace();
            }

        }else if(task instanceof ReducerTask){

        }else{
            throw new RuntimeException("unknown task type given to task scheduler: "+task.getClass());
        }


    	return true;
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
		} catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
/* End of TaskScheduler.java */