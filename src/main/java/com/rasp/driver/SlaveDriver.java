/**
 * Author : Rahul Madhavan
 * File   : SlaveDriver.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.driver;

/* Import list */
import com.rasp.config.Configuration;
import com.rasp.fs.DataNode;
import com.rasp.mr.JobNode;
import com.rasp.mr.TaskNode;
import com.rasp.mr.STaskProtos;
import com.rasp.mr.TaskTracker;
import com.rasp.fs.SInputSplitProtos;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;

import com.rasp.mr.slave.JobNodeClientImpl;
import com.rasp.mr.slave.TaskScheduler;
import com.rasp.config.SlaveConfiguration;
import com.google.protobuf.BlockingService;
import com.rasp.shuffle.ShuffleSlave;
import com.rasp.shuffle.ShuffleSlaveImpl;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;
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
        ShuffleSlave shuffleSlave = new ShuffleSlaveImpl(configuration);
        TaskTracker taskTracker = new com.rasp.mr.slave.TaskTracker();
        TaskScheduler taskScheduler = new TaskScheduler(taskTracker,configuration);
        Thread taskSchedulerThread = new Thread(taskScheduler);


        /**
         * set the implementations for the various classes
         */
        configuration.setDataNode(dataServer);
        configuration.setTaskNode(taskServer);
        configuration.setTaskTracker(taskTracker);
        configuration.setShuffleSlave(shuffleSlave);

        /**
         * initialize the task node
         */
        SInputSplitProtos.DataTransferService.BlockingInterface dataTransferService
                = new DataTransferBlockingService(configuration.getDataNode());
        STaskProtos.TaskService.BlockingInterface taskTransferService
                = new TaskBlockingService(configuration.getTaskNode(),configuration);

        BlockingService ds = SInputSplitProtos.DataTransferService.newReflectiveBlockingService(dataTransferService);
        BlockingService ts = STaskProtos.TaskService.newReflectiveBlockingService(taskTransferService);

        /**
         * start the task scheduler
         */
        taskSchedulerThread.start();
        /**
         * start the data node
         */
        ProtoServer.startServer(configuration.getDataNode().getService(),ds);
        /**
         * start the task node
         */
        ProtoServer.startServer(configuration.getTaskNode().getService(),ts);
    }
}
/* End of SlaveDriver.java */