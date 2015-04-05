package com.rasp.interfaces.impl;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.rasp.config.SlaveConfiguration;
import com.rasp.fs.SInputSplitProtos;
import com.rasp.fs.STaskProtos;
import com.rasp.interfaces.Mapper;
import com.rasp.interfaces.Task;
import com.rasp.task.MapperTask;

import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public class TaskBlockingService implements STaskProtos.TaskService.BlockingInterface{

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
            MapperTask mTask = new MapperTask(sTask.getId());
            mTask.setTaskInputSplit(configuration.getInputSplit(sTask.getInputSplitId()));
            try {
                String className = sTask.getClassName().split(" ")[1];
                System.out.println(className);
                mTask.setMapperClass((Class<? extends Mapper<?,?>>)Class.forName(className));
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
