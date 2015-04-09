package com.rasp.mr.slave;

import com.rasp.config.Configuration;
import com.rasp.config.SlaveConfiguration;
import com.rasp.mr.Task;
import com.rasp.mr.TaskNode;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;

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
    public void initiateDataTransferForKey(String key, Service service) {
        configuration.getShuffleSlave().createDataHandlerFor(key,service);
    }

    @Override
    public void transferDataForKey(byte[] data, String key, Service service) {
        configuration.getShuffleSlave().storeDataFor(data,key,service);
    }

    @Override
    public void terminateTransferDataForKey(String key, Service service) {
        configuration.getShuffleSlave().closeDataHandlerFor(key,service);
    }

    @Override
    public Service getService() {
        return service;
    }


}
