/**
 * Author : Rahul Madhavan
 * File   : SlaveDriver.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.fs.slave;

/* Import list */
import com.rasp.fs.SInputSplitProtos;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import com.rasp.fs.protobuf.ProtoServer;
import com.rasp.config.SlaveConfiguration;
import com.google.protobuf.BlockingService;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

public class SlaveDriver
{
    public static void main(String[] args)
        throws UnknownHostException, FileNotFoundException
    {
        SlaveConfiguration configuration = new SlaveConfiguration(9292, ServiceType.TASK_TRACKER);
        SInputSplitProtos.DataTransferService.BlockingInterface dataTransferService
       		= new DataTransferBlockingService(configuration.getDataNode());
        BlockingService bs = SInputSplitProtos.DataTransferService.newReflectiveBlockingService(dataTransferService);
        ProtoServer.startServer(configuration.getService(), bs);
    }
}
/* End of SlaveDriver.java */