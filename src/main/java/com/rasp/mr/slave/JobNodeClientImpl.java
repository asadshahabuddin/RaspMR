/**
 * Author : Rahul Madhavan
 * File   : JobNodeClientImpl.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import java.util.Map;
import com.rasp.mr.JobNode;
import com.rasp.mr.STaskProtos;
import com.google.protobuf.RpcController;
import com.rasp.utils.protobuf.ProtoClient;
import com.google.protobuf.ServiceException;
import com.rasp.utils.autodiscovery.Service;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;

/**
 * Send the frequency of keys from a slave node to the master.
 */
public class JobNodeClientImpl implements JobNode {
    private Service service;
    private STaskProtos.JobService.BlockingInterface jobService;
    private RpcController controller;

    /**
     * Constructor
     * @param client  Protocol Buffers client.
     * @param service Auto discovery service object.
     */
    public JobNodeClientImpl(ProtoClient client, Service service) {
        this.service = service;
        RpcClientChannel channel = client.getConnection(service.getIp(), service.getPort());
        jobService = STaskProtos.JobService.newBlockingStub(channel);
        controller = channel.newRpcController();
    }

    @Override
    public void mapCompleted(String taskId, Map<String, Long> keyCount) throws ServiceException {
        STaskProtos.SMapResponse.Builder builder = STaskProtos.SMapResponse.newBuilder()
                .setId(taskId);

        STaskProtos.SMapResponse.SKeyCount.Builder keyCountBuilder = STaskProtos.SMapResponse.SKeyCount.newBuilder();
        for (String key : keyCount.keySet()) {
            builder.addMapCount(keyCountBuilder.setKey(key).setCount(keyCount.get(key)).build());
        }

        jobService.sendMapResponse(controller, builder.build());
    }

    @Override
    public void shuffleDataTransferCompleted(String taskId) throws ServiceException {
        STaskProtos.SShuffleResponse shuffleResponse = STaskProtos.SShuffleResponse.newBuilder().setTaskId(taskId).build();
        jobService.shuffleDataTransferCompleted(controller, shuffleResponse);
    }

    @Override
    public void reduceCompleted(String taskId) throws ServiceException {
        STaskProtos.SReduceResponse reduceResponse = STaskProtos.SReduceResponse.newBuilder().setTaskId(taskId).build();
        jobService.reduceCompleted(controller, reduceResponse);
    }

    @Override
    public Service getService() {
        return service;
    }
}