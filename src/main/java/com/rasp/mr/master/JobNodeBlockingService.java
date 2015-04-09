/**
 * Author : Asad Shahabuddin, Rahul Madhavan
 * File   : JobNodeBlockingService.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr.master;

/* Import list */
import com.rasp.config.MasterConfiguration;
import com.rasp.fs.GroupingKeyProtos;
import com.rasp.mr.JobNode;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.rasp.mr.STaskProtos;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobNodeBlockingService implements STaskProtos.JobService.BlockingInterface{

    private JobNode jobNode;

    public JobNodeBlockingService(JobNode jobNode){
        this.jobNode = jobNode;
    }

    @Override
    public STaskProtos.TransferResponse sendMapResponse(RpcController controller, STaskProtos.SMapResponse request) throws ServiceException {
        jobNode.mapCompleted(request.getId(),getKeyCount(request.getMapCountList()));
        return STaskProtos.TransferResponse.newBuilder().setStatus("OK").build();
    }

    private Map<String,Long> getKeyCount(List<STaskProtos.SMapResponse.SKeyCount> sKeyCountList){
        Map<String,Long> keyCount = new HashMap<>();
        for(STaskProtos.SMapResponse.SKeyCount sKeyCount : sKeyCountList){
            keyCount.put(sKeyCount.getKey(),sKeyCount.getCount());
        }
        return keyCount;
    }

    @Override
    public STaskProtos.TransferResponse shuffleDataTransferCompleted(RpcController controller, STaskProtos.SDataTransferTask request) throws ServiceException {
        Service service = ServiceFactory.
                createService(ServiceType.TASK_TRACKER,
                        request.getLocation().getIp(),
                        request.getLocation().getPort());
        jobNode.shuffleDataTransferCompleted(request.getKey(),service);
        return STaskProtos.TransferResponse.newBuilder().setStatus("OK").build();
    }

}
/* End of JobNodeBlockingService.java */