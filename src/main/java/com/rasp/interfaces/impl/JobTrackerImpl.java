package com.rasp.interfaces.impl;


import com.rasp.config.MasterConfiguration;
import com.rasp.fs.InputFormat;
import com.rasp.interfaces.*;
import com.rasp.interfaces.MapperTask;
import com.rasp.task.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public class JobTrackerImpl implements JobTracker {

    LinkedBlockingDeque<Job> queue;
    Map<String,Job> jobMap;
    Map<String,Task> taskMap; // taskId -> jobId : Map
    MasterConfiguration configuration;


    public JobTrackerImpl(MasterConfiguration configuration){
        this.configuration = configuration;
        this.jobMap = new HashMap<>();
        this.taskMap = new HashMap<>();
        this.queue = new LinkedBlockingDeque<>();
    }

    @Override
    public void submit(Job job) {
        jobMap.put(job.getJobId(),job);
        queue.add(job);
    }

    @Override
    public Job nextJob() {
        return queue.remove();
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public void createMapperTasksForJob(Job job) {
        List<MapperTask>  taskList = new ArrayList<>();

        InputFormat inputFormat = configuration.getDataMaster().getInputFormat(job.getInputPath());
        for(InputSplit inputSplit : inputFormat.getSplits()){
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
    public void createReducerTasksForJob(Job job) {

    }


    @Override
    public void completeMapTask(String taskId) {
        Task task = taskMap.get(taskId);
        Job job = task.getJob();

        if(MapperTask.class.isInstance(task)){
            task.complete();
            if(job.isMapComplete()){
                queue.add(job);
            }
        }else{
            throw new RuntimeException("type of task passed should be MapperTask");
        }
    }


    @Override
    public void sendTask(Task task) throws IOException, InterruptedException {

        configuration.getTaskNode(task.getService()).sendTask(task);


    }
}
