package com.rasp.mr.slave;

import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.rasp.config.MasterConfiguration;
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
    public void sendDataTransferTask(String key, Service service)
        throws ServiceException {
        STaskProtos.SDataTransferTask.SReducerLocation location =
                STaskProtos.SDataTransferTask.SReducerLocation.newBuilder()
                        .setIp(service.getIp())
                        .setPort(service.getPort()).build();

        STaskProtos.SDataTransferTask task = STaskProtos.SDataTransferTask
                .newBuilder()
                .setKey(key)
                .setLocation(location).build();

        taskService.sendDataTransferTask(controller, task);
    }

    public void initiateDataTransferForKey(String key,Service service) {
        STaskProtos.STransferKeyData.SDataHost sDataHost = STaskProtos.STransferKeyData
                .SDataHost
                .newBuilder()
                .setIp(service.getIp())
                .setPort(service.getPort())
                .build();
        STaskProtos.STransferKeyData sTransferKeyData = STaskProtos.STransferKeyData
                .newBuilder()
                .setKey(key)
                .setDataHost(sDataHost)
                .build();

        try {
            taskService.initiateTransferDataForKey(controller,sTransferKeyData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void transferDataForKey(byte[] data, String key,Service service) {
        STaskProtos.STransferKeyData sTransferKeyData = STaskProtos.
                STransferKeyData.newBuilder()
                .setKey(key)
                .setData(ByteString.copyFrom(data))
                .build();

        try {
            taskService.transferDataForKey(controller, sTransferKeyData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void terminateTransferDataForKey(String key,Service service) {
        STaskProtos.STransferKeyData sTransferKeyData = STaskProtos.STransferKeyData.newBuilder().setKey(key).build();
        try {
            taskService.terminateTransferDataForKey(controller, sTransferKeyData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Service getService() {
        return service;
    }

    public void sendDataTransferTask(Task task) {
        // TODO
    }
}
/* End of TaskNodeClientImpl.java */