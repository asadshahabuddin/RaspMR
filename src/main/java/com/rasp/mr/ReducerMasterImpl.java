package com.rasp.mr;

import com.rasp.config.MasterConfiguration;
import com.rasp.utils.autodiscovery.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/12/15
 * Edited :
 */
public class ReducerMasterImpl implements ReducerMaster{

    private MasterConfiguration configuration;
    private Map<String, ReducerTask> reducerTaskMap;

    public ReducerMasterImpl(MasterConfiguration configuration) {
        this.configuration = configuration;
        this.reducerTaskMap = new HashMap<>();
    }

    @Override
    public void createReducerTasksForJob(Job job) {
        List<ReducerTask> taskList = new ArrayList<>();

        for(String key : job.getReduceKeyServiceMap().keySet()){
            Service service = job.getReduceKeyServiceMap().get(key);
            ReducerTask reducerTask = new ReducerTaskImpl(service);
            reducerTask.setJob(job);
            reducerTask.setKey(key);
            reducerTask.setReducerClass(job.getReducerClass());
            reducerTaskMap.put(reducerTask.getTaskId(),reducerTask);
            taskList.add(reducerTask);
        }

        job.setReduceTasks(taskList);
    }

    @Override
    synchronized public void completeReduceTask(String taskId) {
        ReducerTask task = reducerTaskMap.get(taskId);
        Job job = task.getJob();
        task.complete();
        System.out.println("Reduce Task Completed : " + taskId);
        checkReduceComplete(job);
    }

    @Override
    public void cleanup(Job job) {
        reducerTaskMap.clear();
        reducerTaskMap = null;
    }

    @Override
    public void checkReduceComplete(Job job) {
        if(job.isReduceComplete()){
            configuration.getJobTracker().submit(job);
        }
    }
}
