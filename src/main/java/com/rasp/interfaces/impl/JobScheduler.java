package com.rasp.interfaces.impl;

import com.rasp.config.MasterConfiguration;
import com.rasp.interfaces.Job;
import com.rasp.interfaces.JobTracker;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public class JobScheduler {

    private MasterConfiguration configuration;

    public JobScheduler(MasterConfiguration configuration){
        this.configuration = configuration;
    }

    public void startScheduler(){
        JobTracker jobTracker = configuration.getJobTracker();

        while(true){
            Job job = jobTracker.nextJob();
            if(!job.isMapComplete()){
                //create
                jobTracker.createMapperTasksForJob(job);
                //send job tasks to other machines
                // sending should probably be done by job tracker
                job.getMapTasks();
            }else if(!job.isShuffleComplete()){
                // do some shuffle work
            }else if(!job.isReduceComplete()){
                jobTracker.createReducerTasksForJob(job);
            }else{

            }

        }

    }

}
