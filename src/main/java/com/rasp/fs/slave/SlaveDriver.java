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
import com.rasp.interfaces.impl.TaskBlockingService;
import com.rasp.interfaces.impl.TaskNode;
import com.rasp.interfaces.impl.TaskNodeServerImpl;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

public class SlaveDriver
{
    public static void main(String[] args)
            throws UnknownHostException, FileNotFoundException
    {
        SlaveConfiguration configuration = new SlaveConfiguration();
        DataNode dataServer = new DataNodeServerImpl(configuration);
        TaskNode taskServer = new TaskNodeServerImpl(configuration);

        configuration.setDataNode(dataServer);
        configuration.setTaskNode(taskServer);

        SInputSplitProtos.DataTransferService.BlockingInterface dataTransferService
                = new DataTransferBlockingService(configuration.getDataNode());
        STaskProtos.TaskService.BlockingInterface taskTransferService
                = new TaskBlockingService(configuration.getTaskNode(),configuration);

        BlockingService bs = SInputSplitProtos.DataTransferService.newReflectiveBlockingService(dataTransferService);
        BlockingService ts = STaskProtos.TaskService.newReflectiveBlockingService(taskTransferService);

        ProtoServer.startServer(configuration.getDataNode().getService(),bs);
        ProtoServer.startServer(configuration.getTaskNode().getService(),ts);
    }
}
/* End of SlaveDriver.java */