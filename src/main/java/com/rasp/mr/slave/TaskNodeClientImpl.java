package com.rasp.mr.slave;

import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.rasp.mr.*;
import com.rasp.utils.protobuf.ProtoClient;
import com.rasp.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan, sourabhsuman
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited : 4/11/15
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

        STaskProtos.STask.Builder sTaskBuilder = STaskProtos.STask.newBuilder()
                .setId(task.getTaskId())
                .setJobId(task.getJob().getJobId())
                .setInputSplitId(task.getTaskInputSplit().getIdx());

        if(task instanceof MapperTask){
            sTaskBuilder.setTaskType(STaskProtos.STask.STaskType.MAPPER);
            sTaskBuilder.setClassName(MapperTask.class.toString());
        } else if(task instanceof ShuffleTask){
            sTaskBuilder.setTaskType(STaskProtos.STask.STaskType.SHUFFLE);
            sTaskBuilder.setClassName(ShuffleTask.class.toString());
        } else {
            sTaskBuilder.setTaskType(STaskProtos.STask.STaskType.REDUCER);
            sTaskBuilder.setClassName(ReducerTask.class.toString());
        }

        try {
            taskService.sendTask(controller,sTaskBuilder.build());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
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
            taskService.initiateTransferDataForKey(controller, sTransferKeyData);
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
}
/* End of TaskNodeClientImpl.java */