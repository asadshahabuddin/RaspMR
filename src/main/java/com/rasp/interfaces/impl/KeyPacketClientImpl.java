/**
 * Author : Asad Shahabuddin
 * File   : KeyPacketClientImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.interfaces.impl;

/* Import list */
import com.rasp.interfaces.KeyPacket;
import com.rasp.fs.protobuf.ProtoClient;
import com.google.protobuf.RpcController;
import com.rasp.fs.slave.GroupingKeyProtos;
import com.google.protobuf.ServiceException;
import raspmr.RaspMR.utils.autodiscovery.Service;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;

public class KeyPacketClientImpl
    implements KeyPacket
{
    private ProtoClient client;
    private Service service;
    private GroupingKeyProtos.KeyPacketService.BlockingInterface keyService;
    private RpcController controller;

    /**
     * Constructor
     * @param client
     *            Protocol Buffers client.
     * @param service
     *            Auto discovery service object.
     */
    public KeyPacketClientImpl(ProtoClient client, Service service)
    {
        this.client  = client;
        this.service = service;
        RpcClientChannel channel = client.getConnection(service.getIp(), service.getPort());
        keyService = GroupingKeyProtos.KeyPacketService.newBlockingStub(channel);
        controller = channel.newRpcController();
    }

    @Override
    public Service getService()
    {
        return service;
    }

    @Override
    public void storeKey(Object key)
        throws ServiceException
    {
        GroupingKeyProtos.KeyPacket keyPacket = GroupingKeyProtos.KeyPacket
                                                .newBuilder()
                                                .setKey(String.valueOf(key))
                                                .build();
        keyService.sendKey(controller, keyPacket);
    }
}
/* End of KeyPacketClientImpl.java */