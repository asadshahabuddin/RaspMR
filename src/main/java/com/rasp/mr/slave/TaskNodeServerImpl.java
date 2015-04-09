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
    public void sendDataTransferTask(Task task) {
        /*
        STaskProtos.STask.STaskType sTaskType;
        String taskClass;

        if(task instanceof MapperTask){
            sTaskType = STaskProtos.STask.STaskType.MAPPER;
            taskClass = ((MapperTask) task).getMapperClass().toString();
        } else {
            sTaskType = STaskProtos.STask.STaskType.REDUCER;
            taskClass = ((ReducerTask) task).getReducerClass().toString();
        }

        STaskProtos.STask sTask = STaskProtos.STask.newBuilder()
                .setClassName(taskClass)
                .setId(task.getTaskId())
                .setJobId(task.getJob().getJobId())
                .setTaskType(sTaskType)
                .setInputSplitId(task.getTaskInputSplit().getIdx())
                .build();

        try {
            taskService.sendTask(controller,sTask);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    */
}