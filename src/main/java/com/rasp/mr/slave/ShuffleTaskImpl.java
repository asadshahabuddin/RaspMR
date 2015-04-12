/**
 * Author : rahulmadhavan, sourabhsuman
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited : 4/11/15
 */

package com.rasp.mr.slave;

import com.rasp.mr.*;
import java.util.UUID;
import java.io.IOException;
import com.rasp.fs.InputSplit;
import com.rasp.config.Configuration;
import com.rasp.utils.autodiscovery.Service;

public class ShuffleTaskImpl implements ShuffleTask {
    private String taskId;
    private Job job;
    private Service service;
    private String key;
    private Service dataTargetService;
    private boolean complete;
    private Configuration conf;

    public ShuffleTaskImpl() {

        taskId = UUID.randomUUID().toString();
    }

    public ShuffleTaskImpl(String taskId, Service service, Configuration conf) {
        this.taskId = taskId;
        this.service = service;
        this.conf = conf;
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
    public boolean execute()
            throws IllegalAccessException, InstantiationException,
            InterruptedException, IOException {
        Service service = conf.getService();
        
        return false;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void complete() {
        complete = true;
    }

    @Override
    public boolean isCompleted() {
        return complete;
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
    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public InputSplit getTaskInputSplit() {
        return null;
    }

    @Override
    public void setTaskInputSplit(InputSplit inputSplit) {
    }

    @Override
    public Service getDataTargetService() {
        return dataTargetService;
    }

    @Override
    public void setDataTargetService(Service dataTargetService) {
        this.dataTargetService = dataTargetService;
    }
}
