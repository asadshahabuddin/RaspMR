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
    Map<String, Task> taskMap;
    LinkedBlockingQueue<Job> jobQueue;
    ShuffleMaster shuffleMaster;

    public JobTrackerImpl(MasterConfiguration conf)
    {
    	this.conf = conf;
    	jobMap    = new HashMap<String, Job>();
    	taskMap   = new HashMap<String, Task>();
    	jobQueue  = new LinkedBlockingQueue<Job>();
        shuffleMaster = new ShuffleMasterImpl(this.conf);
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
    public void createMapperTasksForJob(Job job)
    {
        List<MapperTask> taskList = new ArrayList<>();
        InputFormatImpl inputFormat = conf.getDataMaster().getInputFormat(job.getInputPath());
        
        for(InputSplit inputSplit : inputFormat.getSplits())
        {
            MapperTask mapperTask = new MapperTaskImpl();
            mapperTask.setTaskInputSplit(inputSplit);
            mapperTask.setJob(job);
            mapperTask.setMapperClass(job.getMapperClass());
            taskList.add(mapperTask);
            taskMap.put(mapperTask.getTaskId(),mapperTask);
        }
        job.setMapTasks(taskList);
    }

    @Override
    public void createReducerTasksForJob(Job job)
    {
    	// TODO
    }

    @Override
    public synchronized void completeMapTask(String taskId, Map<String, Long> keyCount)
    {
        Task task = taskMap.get(taskId);
        Job job = task.getJob();

        if(task instanceof MapperTask){

            task.complete();
            for (String key :keyCount.keySet()){
                System.out.println("MAP ::    key   ::"+key+"::    Value   ::"+keyCount.get(key));
            }

            ((MapperTask) task).getMapContext().setKeyCountMap(keyCount);
            if(job.isMapComplete()){
                jobQueue.add(job);
            }
        }
        else
        {
            throw new RuntimeException(" [error] Type of task passed should be MapperTask");
        }
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
}