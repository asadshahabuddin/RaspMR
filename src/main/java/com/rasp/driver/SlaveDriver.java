/**
 * Author : Rahul Madhavan
 * File   : SlaveDriver.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.driver;

/* Import list */
import com.rasp.fs.DataNode;
import com.rasp.mr.TaskNode;
import com.rasp.fs.STaskProtos;
import com.rasp.mr.TaskTracker;
import com.rasp.fs.SInputSplitProtos;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import com.rasp.mr.slave.TaskScheduler;
import com.rasp.config.SlaveConfiguration;
import com.google.protobuf.BlockingService;
import com.rasp.utils.protobuf.ProtoServer;
import com.rasp.fs.slave.DataNodeServerImpl;
import com.rasp.mr.slave.TaskNodeServerImpl;
import com.rasp.mr.slave.TaskBlockingService;
import com.rasp.fs.slave.DataTransferBlockingService;

public class SlaveDriver
{
    public static void main(String[] args)
            throws UnknownHostException, FileNotFoundException
    {
        SlaveConfiguration configuration = new SlaveConfiguration();
        DataNode dataServer = new DataNodeServerImpl(configuration);
        TaskNode taskServer = new TaskNodeServerImpl(configuration);
        TaskTracker taskTracker = new com.rasp.mr.slave.TaskTracker();
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