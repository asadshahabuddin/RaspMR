package com.rasp.mr;

import com.rasp.config.MasterConfiguration;
import com.rasp.utils.autodiscovery.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    static final Logger LOG = LoggerFactory.getLogger(ReducerMasterImpl.class);

    private MasterConfiguration configuration;
    private Map<String, ReducerTask> taskMap;

    public ReducerMasterImpl(MasterConfiguration configuration) {
        this.configuration = configuration;
        this.taskMap = new HashMap<>();
    }

    @Override
    public void createReducerTasksForJob(Job job) {
        List<ReducerTask> taskList = new ArrayList<>();

        for(String key : job.getReduceKeyServiceMap().keySet()){
            Service service = job.getReduceKeyServiceMap().get(key);
            ReducerTask reducerTask = new ReducerTaskImpl(job,service);
            reducerTask.setKey(key);
            reducerTask.setReducerClass(job.getReducerClass());
            taskMap.put(reducerTask.getTaskId(), reducerTask);
            taskList.add(reducerTask);
        }

        job.setReduceTasks(taskList);
    }

    @Override
    synchronized public void completeReduceTask(String taskId) {
        ReducerTask task = taskMap.get(taskId);
        Job job = task.getJob();
        task.complete();
        LOG.info("Reduce Task Completed : " + taskId);
        whenReduceComplete(job);
    }

    @Override
    public void cleanup(Job job) {
        for(ReducerTask reducerTask : job.getReduceTasks()){
            taskMap.remove(reducerTask.getTaskId());
        }
    }

    @Override
    public void whenReduceComplete(Job job) {
        if(job.isReduceComplete()){
            configuration.getJobTracker().submit(job);
        }
    }
}
