/**
 * Author : Rahul Madhavan
 * File   : JobTrackerImpl.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.interfaces.impl;

/* Import list */
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import com.rasp.fs.InputFormat;
import com.rasp.interfaces.Job;
import com.rasp.interfaces.Task;
import com.rasp.interfaces.InputSplit;
import com.rasp.interfaces.JobTracker;
import com.rasp.interfaces.MapperTask;
import com.rasp.config.MasterConfiguration;
import java.util.concurrent.LinkedBlockingQueue;

public class JobTrackerImpl implements JobTracker
{
	MasterConfiguration conf;
    Map<String, Job> jobMap;
    Map<String, Task> taskMap;
    LinkedBlockingQueue<Job> jobQueue;

    public JobTrackerImpl(MasterConfiguration conf)
    {
    	this.conf = conf;
    	jobMap    = new HashMap<String, Job>();
    	taskMap   = new HashMap<String, Task>();
    	jobQueue  = new LinkedBlockingQueue<Job>();
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
        InputFormat inputFormat = conf.getDataMaster().getInputFormat(job.getInputPath());
        
        for(InputSplit inputSplit : inputFormat.getSplits())
        {
            MapperTask mapperTask = new com.rasp.task.MapperTask();
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
    public void completeMapTask(String taskId)
    {
        Task task = taskMap.get(taskId);
        Job job = task.getJob();

        if(task instanceof MapperTask)
        {
            task.complete();
            if(job.isMapComplete())
            {
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
}