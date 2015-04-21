/**
 * Author : Rahul Madhavan
 * File   : TaskBlockingService.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr.slave;

import com.rasp.mr.*;
import org.slf4j.Logger;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import com.rasp.mr.master.JobImpl;
import com.rasp.config.Configuration;
import com.google.protobuf.RpcController;
import com.rasp.config.SlaveConfiguration;
import com.rasp.utils.autodiscovery.Service;
import com.google.protobuf.ServiceException;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

/**
 * Transfer data for a key.
 */
public class TaskBlockingService
    implements STaskProtos.TaskService.BlockingInterface {
    static final Logger LOG = LoggerFactory.getLogger(TaskBlockingService.class);

    private TaskNode taskNode;
    private Service service;
    private SlaveConfiguration configuration;

    /**
     * Constructor 1
     * @param taskNode
     *            The node where a task is to be executed.
     * @param slaveConfiguration
     *            Configuration object.
     */
    public TaskBlockingService(TaskNode taskNode, SlaveConfiguration slaveConfiguration) {
        this.taskNode = taskNode;
        this.configuration = slaveConfiguration;
        this.service = ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                configuration.getLocalServiceAddress().getHostAddress(),
                Configuration.TASK_NODE_PORT);
    }

    @Override
    public STaskProtos.STransferResponse sendTask(RpcController controller, STaskProtos.STask sTask) throws ServiceException {
        try {
            taskNode.sendTask(sTaskToTask(sTask));
        } catch (IOException e) {
            LOG.error("", e);
        }
        return STaskProtos.STransferResponse.newBuilder().setStatus("OK").build();
    }

    /**
     * Converts a sTask object to a Task object.
     * @param sTask
     *            The source Protocol Buffers object.
     * @return
     *            The target Java object.
     * @throws IOException
     */
    private Task sTaskToTask(STaskProtos.STask sTask) throws IOException {
        Task task;
        Job job = new JobImpl(sTask.getJobId());

        if (sTask.getTaskType() == STaskProtos.STask.STaskType.MAPPER) {
            MapperTaskImpl mTask = new MapperTaskImpl(sTask.getId(), job, service);
            mTask.setTaskInputSplit(configuration.getInputSplit(sTask.getInputSplitId()));
            try {
                String className = sTask.getClassName().split(" ")[1];
                mTask.setMapperClass((Class<? extends Mapper>) Class.forName(className));
            } catch (ClassNotFoundException e) {
                LOG.error("", e);
            }
            task = mTask;
        } else if (sTask.getTaskType() == STaskProtos.STask.STaskType.SHUFFLE) {
            // Initialize shuffle task
            Service dataService = ServiceFactory.createService(ServiceType.TASK_TRACKER, sTask.getIp(), Configuration.TASK_NODE_PORT);
            ShuffleTask shuffleTask = new ShuffleTaskImpl(sTask.getId(), job, service, configuration);
            shuffleTask.setKey(sTask.getKey());
            shuffleTask.setDataTargetService(dataService);
            task = shuffleTask;

        } else {
            ReducerTask reducerTask = new ReducerTaskImpl(sTask.getId(), job, service, configuration, sTask.getKey());
            try {
                String className = sTask.getClassName().split(" ")[1];
                reducerTask.setReducerClass((Class<? extends Reducer>) Class.forName(className));
            } catch (ClassNotFoundException e) {
                LOG.error("", e);
            }
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
            taskNode.initiateDataTransferForKey(request.getKey(), request.getJobId(), service);
        } catch (IOException e) {
            LOG.error("", e);
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
            taskNode.transferDataForKey(request.getData().toByteArray(), request.getKey(), request.getJobId(), service);
        } catch (IOException e) {
            LOG.error("", e);
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
            taskNode.terminateTransferDataForKey(request.getKey(), request.getJobId(), service);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return STaskProtos.STransferResponse.newBuilder().setStatus("OK").build();
    }


    @Override
    public STaskProtos.STransferResponse cleanup(RpcController controller, STaskProtos.SJob request) throws ServiceException {
        taskNode.cleanup(new JobImpl(request.getJobId()));
        return STaskProtos.STransferResponse.newBuilder().setStatus("OK").build();
    }
}