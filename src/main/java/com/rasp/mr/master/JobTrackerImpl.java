/**
 * Author : Rahul Madhavan
 * File   : JobTrackerImpl.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr.master;

/* Import list */
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import com.rasp.fs.InputFormatImpl;
import com.rasp.mr.*;
import com.rasp.fs.InputSplit;
import com.rasp.config.MasterConfiguration;
import com.rasp.mr.slave.MapperTaskImpl;
import com.rasp.shuffle.ShuffleMaster;
import com.rasp.shuffle.ShuffleMasterImpl;

import java.util.concurrent.LinkedBlockingQueue;

public class JobTrackerImpl implements JobTracker{

    MasterConfiguration conf;
    Map<String, Job> jobMap;
    LinkedBlockingQueue<Job> jobQueue;
    ShuffleMaster shuffleMaster;
    ReducerMaster reducerMaster;
    MapperMaster mapperMaster;

    public JobTrackerImpl(MasterConfiguration conf)
    {
    	this.conf = conf;
    	jobMap    = new HashMap<String, Job>();
    	jobQueue  = new LinkedBlockingQueue<Job>();
        shuffleMaster = new ShuffleMasterImpl(this.conf);
        reducerMaster = new ReducerMasterImpl(this.conf);
        mapperMaster = new MapperMasterImpl(this.conf);
    }

    @Override
    public void submit(Job job)
    {
        jobMap.put(job.getJobId(), job);
        jobQueue.add(job);
    }

    @Override
    public Job nextJob()
    {
        return jobQueue.poll();
    }

    @Override
    public void sendTask(Task task)
		throws IOException, InterruptedException
    {
        conf.getTaskNode(task.getService()).sendTask(task);
    }

    @Override
    public ShuffleMaster getShuffleMaster() {
        return shuffleMaster;
    }

    @Override
    public MapperMaster getMapperMaster() {
        return mapperMaster;
    }

    @Override
    public ReducerMaster getReducerMaster() {
        return reducerMaster;
    }

    @Override
    public void cleanup(Job job) {

    }

    @Override
    public void map(Job job) throws IOException, InterruptedException {

        mapperMaster.createMapperTasksForJob(job);

        for(MapperTask task : job.getMapTasks())
        {
            sendTask(task);
        }
    }

    @Override
    public void reduce(Job job) {
        reducerMaster.createReducerTasksForJob(job);
    }

    @Override
    public void shuffle(Job job) throws IOException, InterruptedException {
        shuffleMaster.run(job);
    }
}