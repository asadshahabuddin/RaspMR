/**
 * Author : Shivastuti Koul
 * File   : TaskNodeClientImpl.java
 * Email  : koul.sh@husky.neu.edu
 * Created: 4/4/15
 * Edited : 4/11/15
 */

package com.rasp.mr.slave;

import com.rasp.mr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.rasp.utils.protobuf.ProtoClient;
import com.rasp.utils.autodiscovery.Service;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;

public class TaskNodeClientImpl implements TaskNode {
    static final Logger LOG = LoggerFactory.getLogger(TaskNodeClientImpl.class);

    private ProtoClient protoClient;
    private Service service;
    private STaskProtos.TaskService.BlockingInterface taskService;
    private RpcController controller;

    /**
     * Constructor 1
     * @param protoClient
     *            Protocol Buffers network client object.
     * @param service
     *            Wrapper object containing the IP and port of a node.
     */
    public TaskNodeClientImpl(ProtoClient protoClient, Service service) {
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
                .setJobId(task.getJob().getJobId());

        if(task instanceof MapperTask){
            sTaskBuilder.setTaskType(STaskProtos.STask.STaskType.MAPPER);
            sTaskBuilder.setClassName(((MapperTask) task).getMapperClass().toString());
            sTaskBuilder.setInputSplitId(((MapperTask) task).getTaskInputSplit().getIdx());
        } else if(task instanceof ShuffleTask){
            sTaskBuilder.setTaskType(STaskProtos.STask.STaskType.SHUFFLE);
            sTaskBuilder.setKey(((ShuffleTask) task).getKey());
            sTaskBuilder.setIp(((ShuffleTask) task).getDataTargetService().getIp());
        } else {
            sTaskBuilder.setTaskType(STaskProtos.STask.STaskType.REDUCER);
            sTaskBuilder.setKey(((ReducerTask) task).getKey());
            sTaskBuilder.setClassName(((ReducerTask) task).getReducerClass().toString());
        }

        try {
            taskService.sendTask(controller,sTaskBuilder.build());
        } catch (ServiceException e) {
            LOG.error("",e);
        }
    }

    /**
     * Setup objects for transfer of data between nodes.
     * @param key
     *            The key.
     * @param jobId
     *            Job identifier.
     * @param service
     *            Wrapper object containing the IP and port of a node.
     */
    public void initiateDataTransferForKey(String key, String jobId, Service service) {
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
            LOG.error("",e);
        }

    }

    @Override
    public void transferDataForKey(byte[] data, String jobId, String key,Service service) {
        STaskProtos.STransferKeyData.SDataHost sDataHost = STaskProtos.STransferKeyData
                .SDataHost
                .newBuilder()
                .setIp(service.getIp())
                .setPort(service.getPort())
                .build();
        STaskProtos.STransferKeyData sTransferKeyData = STaskProtos.STransferKeyData
                .newBuilder()
                .setKey(key)
                .setData(ByteString.copyFrom(data))
                .setDataHost(sDataHost)
                .build();


        try {
            taskService.transferDataForKey(controller, sTransferKeyData);
        } catch (ServiceException e) {
            LOG.error("",e);
        }

    }

    @Override
    public void terminateTransferDataForKey(String key, String jobId, Service service) {
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
            taskService.terminateTransferDataForKey(controller, sTransferKeyData);
        } catch (ServiceException e) {
            LOG.error("",e);
        }
    }

    @Override
    public void cleanup(Job job) {
        try {
            taskService.cleanup(controller, STaskProtos.SJob.newBuilder().setJobId(job.getJobId()).build());
        } catch (ServiceException e) {
            LOG.error("", e);
        }
    }

    @Override
    public Service getService() {
        return service;
    }
}