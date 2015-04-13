/**
 * Author : Rahul Madhavan
 * File   : TaskBlockingService.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr.slave;

import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.rasp.config.Configuration;
import com.rasp.config.SlaveConfiguration;
import com.rasp.mr.*;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TaskBlockingService
    implements STaskProtos.TaskService.BlockingInterface {
    private TaskNode taskNode;
    private Service service;

    private SlaveConfiguration configuration;

    public TaskBlockingService(TaskNode taskNode, SlaveConfiguration slaveConfiguration) {
        this.taskNode = taskNode;
        this.configuration = slaveConfiguration;
        this.service =  ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                configuration.getLocalServiceAddress().getHostAddress(),
                Configuration.TASK_NODE_PORT);
    }

    @Override
    public STaskProtos.STransferResponse sendTask(RpcController controller, STaskProtos.STask sTask) throws ServiceException {
        taskNode.sendTask(sTaskToTask(sTask));
        return STaskProtos.STransferResponse.newBuilder().setStatus("OK").build();
    }

    private Task sTaskToTask(STaskProtos.STask sTask) {
        Task task;
        if (sTask.getTaskType() == STaskProtos.STask.STaskType.MAPPER) {
            MapperTaskImpl mTask = new MapperTaskImpl(sTask.getId(),service);
            mTask.setTaskInputSplit(configuration.getInputSplit(sTask.getInputSplitId()));
            try {
                String className = sTask.getClassName().split(" ")[1];
                mTask.setMapperClass((Class<? extends Mapper>) Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            task = mTask;


        } else if (sTask.getTaskType() == STaskProtos.STask.STaskType.SHUFFLE) {
            // initialize shuffle task
            Service dataService = ServiceFactory.createService(ServiceType.TASK_TRACKER,sTask.getIp(),Configuration.TASK_NODE_PORT);
            ShuffleTask shuffleTask = new ShuffleTaskImpl(sTask.getId(),service,configuration);
            shuffleTask.setKey(sTask.getKey());
            shuffleTask.setDataTargetService(dataService);
            task = shuffleTask;

        }else{
            ReducerTask reducerTask = new ReducerTaskImpl(sTask.getId(),service,configuration);
            reducerTask.setKey(sTask.getKey());
            task = reducerTask;
        }
        return task;
    }

    @Override
    public STaskProtos.STransferResponse initiateTransferDataForKey(RpcController controller, STaskProtos.STransferKeyData request) throws ServiceException {
        Service service = ServiceFactory.
                createService(ServiceType.TASK_TRACKER,
                        request.getDataHost().getIp(),
                        request.getDataHost().getPort());

        try {
            taskNode.initiateDataTransferForKey(request.getKey(), service);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return STaskProtos.STransferResponse.newBuilder().setStatus("OK").build();
    }

    @Override
    public STaskProtos.STransferResponse transferDataForKey(RpcController controller, STaskProtos.STransferKeyData request) throws ServiceException {
        Service service = ServiceFactory.
                createService(ServiceType.TASK_TRACKER,
                        request.getDataHost().getIp(),
                        request.getDataHost().getPort());
        try {
            taskNode.transferDataForKey(request.getData().toByteArray(), request.getKey(), service);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return STaskProtos.STransferResponse.newBuilder().setStatus("OK").build();
    }

    @Override
    public STaskProtos.STransferResponse terminateTransferDataForKey(RpcController controller, STaskProtos.STransferKeyData request) throws ServiceException {
        Service service = ServiceFactory.
                createService(ServiceType.TASK_TRACKER,
                        request.getDataHost().getIp(),
                        request.getDataHost().getPort());
        try {
            taskNode.terminateTransferDataForKey(request.getKey(), service);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return STaskProtos.STransferResponse.newBuilder().setStatus("OK").build();
    }


}
