package com.rasp.interfaces.impl;

import com.rasp.config.Configuration;
import com.rasp.config.SlaveConfiguration;
import com.rasp.interfaces.Task;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/5/15
 * Edited :
 */
public class TaskNodeServerImpl implements TaskNode {

    private Service service;
    private SlaveConfiguration configuration;

    public TaskNodeServerImpl(SlaveConfiguration configuration){
        this.configuration = configuration;
        this.service  = ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                this.configuration.getLocalServiceAddress().getHostAddress(),
                Configuration.TASK_NODE_PORT);
    }

    @Override
    public void sendTask(Task task) {
        System.out.println("task " + task.getTaskId() + " : input split : "+ task.getTaskInputSplit().getIdx());
        configuration.getTaskTracker().submit(task);
    }

    @Override
    public Service getService() {
        return service;
    }

}
