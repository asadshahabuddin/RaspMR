/**
 * Author : Rahul Madhavan
 * File   : SlaveDriver.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.fs.slave;

/* Import list */
import com.rasp.fs.DataNode;
import com.rasp.fs.SInputSplitProtos;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;

import com.rasp.fs.STaskProtos;
import com.rasp.fs.protobuf.ProtoServer;
import com.rasp.config.SlaveConfiguration;
import com.google.protobuf.BlockingService;
import com.rasp.interfaces.TaskTracker;
import com.rasp.interfaces.impl.TaskBlockingService;
import com.rasp.interfaces.impl.TaskNode;
import com.rasp.interfaces.impl.TaskNodeServerImpl;
import com.rasp.task.TaskScheduler;


public class SlaveDriver
{
    public static void main(String[] args)
            throws UnknownHostException, FileNotFoundException
    {
        SlaveConfiguration configuration = new SlaveConfiguration();
        DataNode dataServer = new DataNodeServerImpl(configuration);
        TaskNode taskServer = new TaskNodeServerImpl(configuration);
        TaskTracker taskTracker = new com.rasp.task.TaskTracker();
        TaskScheduler taskScheduler = new TaskScheduler(taskTracker);
        Thread taskSchedulerThread = new Thread(taskScheduler);

        configuration.setDataNode(dataServer);
        configuration.setTaskNode(taskServer);
        configuration.setTaskTracker(taskTracker);

        SInputSplitProtos.DataTransferService.BlockingInterface dataTransferService
                = new DataTransferBlockingService(configuration.getDataNode());
        STaskProtos.TaskService.BlockingInterface taskTransferService
                = new TaskBlockingService(configuration.getTaskNode(),configuration);

        BlockingService bs = SInputSplitProtos.DataTransferService.newReflectiveBlockingService(dataTransferService);
        BlockingService ts = STaskProtos.TaskService.newReflectiveBlockingService(taskTransferService);

        taskSchedulerThread.start();
        ProtoServer.startServer(configuration.getDataNode().getService(),bs);
        ProtoServer.startServer(configuration.getTaskNode().getService(),ts);
    }
}
/* End of SlaveDriver.java */