package com.rasp.mr.slave;

import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.rasp.config.SlaveConfiguration;
import com.rasp.mr.STaskProtos;
import com.rasp.mr.Mapper;
import com.rasp.mr.Task;
import com.rasp.mr.TaskNode;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;

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

    @Override
    public STaskProtos.TransferResponse initiateTransferDataForKey(RpcController controller, STaskProtos.STransferKeyData request) throws ServiceException {
        Service service = ServiceFactory.
                createService(ServiceType.TASK_TRACKER,
                        request.getDataHost().getIp(),
                        request.getDataHost().getPort());
        taskNode.initiateDataTransferForKey(request.getKey(),service);
        return STaskProtos.TransferResponse.newBuilder().setStatus("OK").build();
    }

    @Override
    public STaskProtos.TransferResponse transferDataForKey(RpcController controller, STaskProtos.STransferKeyData request) throws ServiceException {
        Service service = ServiceFactory.
                createService(ServiceType.TASK_TRACKER,
                        request.getDataHost().getIp(),
                        request.getDataHost().getPort());
        taskNode.transferDataForKey(request.getData().toByteArray(), request.getKey(),service);
        return STaskProtos.TransferResponse.newBuilder().setStatus("OK").build();
    }

    @Override
    public STaskProtos.TransferResponse terminateTransferDataForKey(RpcController controller, STaskProtos.STransferKeyData request) throws ServiceException {
        Service service = ServiceFactory.
                createService(ServiceType.TASK_TRACKER,
                        request.getDataHost().getIp(),
                        request.getDataHost().getPort());
        taskNode.terminateTransferDataForKey(request.getKey(),service);
        return STaskProtos.TransferResponse.newBuilder().setStatus("OK").build();
    }
}
