/**
 * Author : Rahul Madhavan
 * File   : JobTrackerImpl.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr.master;

/* Import list */
import java.io.*;
import java.util.Map;
import java.util.HashMap;

import com.rasp.mr.*;
import com.rasp.config.MasterConfiguration;
import com.rasp.shuffle.ShuffleMaster;
import com.rasp.shuffle.ShuffleMasterImpl;

import java.util.concurrent.LinkedBlockingQueue;

public class JobTrackerImpl implements JobTracker{

    MasterConfiguration conf;
    Map<String, Job> jobMap;
    Map<String,InputStream> completedJobMap;

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
        completedJobMap = new HashMap<>();
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
    public void execute(Job job) throws IOException, InterruptedException {

        if(!job.isMapComplete()){
            System.out.println("map is not complete");
            this.map(job);
        }else if(!job.isShuffleComplete()){
            System.out.println("shuffle is not complete");
            this.shuffle(job);
        }else if(!job.isReduceComplete()){
            System.out.println("reduce is not complete");
            this.reduce(job);
        }else{
            System.out.println("about to begin cleanup");
            this.cleanup(job);
        }
    }

    private void cleanup(Job job) {

        mapperMaster.cleanup(job);
        shuffleMaster.cleanup(job);
        reducerMaster.cleanup(job);
        jobMap.remove(job.getJobId());

        //TODO persist keyToServiceMap to fileSystem
//        try {
//            //completedJobMap.put(job.getJobId(),new FileInputStream(job.getJobId()));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        job.cleanup();

    }


    private void map(Job job) throws IOException, InterruptedException {

        mapperMaster.createMapperTasksForJob(job);

        for(MapperTask task : job.getMapTasks())
        {
            sendTask(task);
        }
    }

    private void reduce(Job job) throws IOException, InterruptedException {

        reducerMaster.createReducerTasksForJob(job);

        for(ReducerTask task : job.getReduceTasks())
        {
            sendTask(task);
        }
    }


    private void shuffle(Job job) throws IOException, InterruptedException {

        shuffleMaster.createShuffleTasks(job);

        if(job.getShuffleTasks().size() == 0){
            job.shuffleComplete();
            shuffleMaster.checkShuffleComplete(job);
        }else{
            for(ShuffleTask task : job.getShuffleTasks())
            {
                sendTask(task);
            }
        }

    }


}