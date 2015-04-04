package com.rasp.fs.slave;

import com.google.protobuf.BlockingService;
import com.rasp.config.SlaveConfiguration;
import com.rasp.fs.SInputSplitProtos;
import com.rasp.fs.protobuf.ProtoServer;

import raspmr.RaspMR.utils.autodiscovery.ServiceType;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/3/15
 * Edited :
 */
public class SlaveDriver {

    public static void main(String[] args)
        throws UnknownHostException, FileNotFoundException {

        SlaveConfiguration configuration = new SlaveConfiguration(9292, ServiceType.TASK_TRACKER);
        SInputSplitProtos.DataTransferService.BlockingInterface dataTransferService
               = new DataTransferBlockingService(configuration.getDataNode());
        BlockingService bs = SInputSplitProtos.DataTransferService.newReflectiveBlockingService(dataTransferService);
        ProtoServer.startServer(configuration.getService(),bs);

    }
}
