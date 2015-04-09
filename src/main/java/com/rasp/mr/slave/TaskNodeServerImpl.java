/**
 * Author : Rahul Madhavan / Asad Shahabuddin
 * File   : TaskNodeServerImpl.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 5, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import com.rasp.mr.*;
import com.rasp.config.Configuration;
import com.rasp.config.SlaveConfiguration;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class TaskNodeServerImpl
    implements TaskNode {
    private Service service;
    private SlaveConfiguration configuration;

    public TaskNodeServerImpl(SlaveConfiguration configuration) {
        this.configuration = configuration;
        this.service  = ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                this.configuration.getLocalServiceAddress().getHostAddress(),
                Configuration.TASK_NODE_PORT);
    }

    @Override
    public Service getService() {
        return service;
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
        configuration.getShuffleSlave().storeDataFor(data, key, service);
    }

    @Override
    public void sendDataTransferTask(String key, Service service) {
        // TODO
    }

    public void terminateTransferDataForKey(String key, Service service) {
        configuration.getShuffleSlave().closeDataHandlerFor(key, service);
    }
}