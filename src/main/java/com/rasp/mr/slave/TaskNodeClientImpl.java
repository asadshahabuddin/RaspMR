package com.rasp.mr.slave;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.rasp.mr.STaskProtos;
import com.rasp.utils.protobuf.ProtoClient;
import com.rasp.mr.MapperTask;
import com.rasp.mr.ReducerTask;
import com.rasp.mr.Task;
import com.rasp.mr.TaskNode;
import com.rasp.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public class TaskNodeClientImpl implements TaskNode {
    private ProtoClient protoClient;
    private Service service;
    STaskProtos.TaskService.BlockingInterface taskService;
    private RpcController controller;

    public TaskNodeClientImpl(ProtoClient protoClient,Service service) {
        this.protoClient = protoClient;
        this.service = service;
        RpcClientChannel channel = protoClient.getConnection(service.getIp(),service.getPort());
        taskService = STaskProtos.TaskService.newBlockingStub(channel);
        controller = channel.newRpcController();
    }

    @Override
    public Service getService() {
        return null;
    }

    @Override
    public void sendTask(Task task) {
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

    @Override
    public void sendDataTransferTask(Task task) {
        // TODO
    }
}
/* End of TaskNodeClientImpl.java */