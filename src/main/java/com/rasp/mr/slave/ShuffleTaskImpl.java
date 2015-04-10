package com.rasp.mr.slave;

import com.rasp.fs.InputSplit;
import com.rasp.mr.*;
import com.rasp.utils.autodiscovery.Service;


import java.io.IOException;
import java.util.UUID;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited :
 */
public class ShuffleTaskImpl implements ShuffleTask {
    private String taskId;
    private Job job;
    private Service service;
    private String key;

    public ShuffleTaskImpl(){

        taskId = UUID.randomUUID().toString();
    }

    public ShuffleTaskImpl(String taskId, Service service){
        this.taskId = taskId;
        this.service = service;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public boolean execute() throws IllegalAccessException, InstantiationException, InterruptedException, IOException {
        return false;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void complete() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public Service getService() throws IOException, InterruptedException {
        return service;
    }

    @Override
    public InputSplit getTaskInputSplit() {
        return null;
    }

    @Override
    public void setTaskInputSplit(InputSplit inputSplit) {
    }
}
