/**
 * Author : Asad Shahabuddin, Rahul Madhavan
 * File   : KeyPacketClientImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import com.rasp.mr.STaskProtos;
import com.rasp.mr.JobNode;
import com.rasp.mr.Task;
import com.rasp.utils.protobuf.ProtoClient;
import com.google.protobuf.RpcController;
import com.rasp.fs.GroupingKeyProtos;
import com.google.protobuf.ServiceException;
import com.rasp.utils.autodiscovery.Service;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;

import java.util.Map;

public class JobNodeClientImpl implements JobNode{
    private ProtoClient client;
    private Service service;
    private STaskProtos.JobService.BlockingInterface jobService;
    private RpcController controller;

    /**
     * Constructor
     * @param client
     *            Protocol Buffers client.
     * @param service
     *            Auto discovery service object.
     */
    public JobNodeClientImpl(ProtoClient client, Service service)
    {
        this.client  = client;
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
        for(String key : keyCount.keySet()){
            builder.addMapCount(keyCountBuilder.setKey(key).setCount(keyCount.get(key)).build());
        }

        jobService.sendMapResponse(controller,builder.build());
    }

    @Override
    public Service getService() {
        return service;
    }
}
/* End of KeyPacketClientImpl.java */