/**
 * Author : Rahul Madhavan
 * File   : TaskBlockingService.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr.slave;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.rasp.config.SlaveConfiguration;
import com.rasp.mr.STaskProtos;
import com.rasp.mr.Mapper;
import com.rasp.mr.Task;
import com.rasp.mr.TaskNode;

public class TaskBlockingService
    implements STaskProtos.TaskService.BlockingInterface{
    private TaskNode taskNode;
    private SlaveConfiguration configuration;
    public TaskBlockingService(TaskNode taskNode, SlaveConfiguration slaveConfiguration) {
        this.taskNode = taskNode;
        this.configuration = slaveConfiguration;
    }

    @Override
    public STaskProtos.TransferResponse sendTask(RpcController controller, STaskProtos.STask sTask) throws ServiceException {
        taskNode.sendTask(sTaskToTask(sTask));
        return STaskProtos.TransferResponse.newBuilder().setStatus("OK").build();
    }

    private Task sTaskToTask(STaskProtos.STask sTask){
        Task task;
        if(sTask.getTaskType() == STaskProtos.STask.STaskType.MAPPER){
            MapperTaskImpl mTask = new MapperTaskImpl(sTask.getId());
            mTask.setTaskInputSplit(configuration.getInputSplit(sTask.getInputSplitId()));
            try {
                String className = sTask.getClassName().split(" ")[1];
                mTask.setMapperClass((Class<? extends Mapper>)Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            task = mTask;


        }else{
            // initialize reducer task
            task = null;
        }
        return task;
    }
}
