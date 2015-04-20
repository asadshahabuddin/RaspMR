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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobScheduler
	implements Runnable
{
    static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

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
						LOG.info("   [echo] Job scheduler has resumed scheduling");
					}
					else
					{
                        LOG.info("   [echo] Job are no tasks to schedule");
					}
				}
			}
		}
		catch(IOException ioe)
		{
            LOG.error("",ioe);
		}
		catch(InterruptedException intre)
		{
            LOG.error("",intre);
		}
	}
}