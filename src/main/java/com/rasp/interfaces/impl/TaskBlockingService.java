package com.rasp.interfaces.impl;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.rasp.fs.STaskProtos;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public class TaskBlockingService implements STaskProtos.TaskService.BlockingInterface{

    public TaskBlockingService() {
    }

    @Override
    public STaskProtos.TransferResponse sendTask(RpcController controller, STaskProtos.STask request) throws ServiceException {
        return null;
    }
}
