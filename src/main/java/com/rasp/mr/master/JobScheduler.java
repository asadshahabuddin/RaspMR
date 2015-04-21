/**
 * Author : Rahul Madhavan
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobScheduler implements Runnable{

    static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

	private JobTracker jobTracker;

    public JobScheduler(JobTracker jobTracker){
        this.jobTracker = jobTracker;
    }

    /**
     * schedule is responsible for deciding which job is to be executed and then also executes it
     *
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public void schedule() throws InterruptedException, IOException{
        Job job = jobTracker.nextJob();
        jobTracker.execute(job);
    }

    @Override
	public void run(){
		try{
			while(true){
                schedule();
			}
		}
		catch(IOException ioe){
            LOG.error("",ioe);
		}catch(InterruptedException intre){
            LOG.error("",intre);
		}
	}
}