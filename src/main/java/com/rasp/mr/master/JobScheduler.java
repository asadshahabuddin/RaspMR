/**
 * Author : Rahul Madhavan / Asad Shahabuddin
 * File   : JobScheduler.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr.master;

/* Import list */
import java.io.IOException;
import com.rasp.mr.Job;
import com.rasp.mr.JobTracker;
import com.rasp.mr.MapperTask;

public class JobScheduler
	implements Runnable
{
	private JobTracker jobTracker;

    public JobScheduler(JobTracker jobTracker){
        this.jobTracker = jobTracker;
    }

    public boolean schedule()
    	throws InterruptedException, IOException
    {
        Job job = jobTracker.nextJob();
        if(job == null)
        {
        	return false;
        }
        
        jobTracker.execute(job);

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
						System.out.println("   [echo] Job scheduler has resumed scheduling");
					}
					else
					{
						System.out.println("   [echo] Job are no tasks to schedule");
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
	}
}