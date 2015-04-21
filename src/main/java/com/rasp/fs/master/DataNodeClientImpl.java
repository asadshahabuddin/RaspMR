/**
 * Author : Asad Shahabuddin
 * File   : DataNodeClientImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.fs.master;

import org.slf4j.Logger;
import java.io.IOException;
import com.rasp.fs.DataNode;
import org.slf4j.LoggerFactory;
import com.rasp.fs.InputSplitImpl;
import com.rasp.fs.SInputSplitProtos;
import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.rasp.utils.protobuf.ProtoClient;
import com.rasp.utils.autodiscovery.Service;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;

public class DataNodeClientImpl implements DataNode {
    static final Logger LOG = LoggerFactory.getLogger(DataNodeClientImpl.class);

    private ProtoClient protoClient;
    private Service service;
    private SInputSplitProtos.DataTransferService.BlockingInterface transferService;
    private RpcController controller;

    /**
     * Constructor.
     *
     * @param protoClient Protocol Buffers client representation.
     * @param service     Wrapper object containing the IP and port of a node.
     */
    public DataNodeClientImpl(ProtoClient protoClient, Service service) {
        this.protoClient = protoClient;
        this.service = service;
        RpcClientChannel channel = protoClient.getConnection(service.getIp(), service.getPort());
        transferService = SInputSplitProtos.DataTransferService.newBlockingStub(channel);
        controller = channel.newRpcController();
    }

    @Override
    public void storeInputSplit(InputSplitImpl inputSplit) throws InterruptedException, IOException {
        SInputSplitProtos.SInputSplit sInputSplit = SInputSplitProtos.SInputSplit
                .newBuilder()
                .setIdx(inputSplit.getIdx())
                .setLength(inputSplit.getLength())
                .setLocation(inputSplit.getLocation())
                .setOffset(inputSplit.getOffset())
                .setLength(inputSplit.getLength())
                .setInputFormatId(inputSplit.getInputFormatId())
                .build();
        try {
            transferService.sendInputSplit(controller, sInputSplit);
        } catch (ServiceException e) {
            LOG.error("", e);
        }
    }

    @Override
    public void storeChunk(byte[] b) throws InterruptedException, IOException {
        SInputSplitProtos.SInputChunk sInputChunk = SInputSplitProtos.SInputChunk
                .newBuilder()
                .setChunk(ByteString.copyFrom(b))
                .build();

        try {
            transferService.sendChunk(controller, sInputChunk);
        } catch (ServiceException e) {
            LOG.error("", e);
        }
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public void closeInputSplit() {
        SInputSplitProtos.Void sVoid = SInputSplitProtos.Void.newBuilder().build();
        try {
            transferService.closeInputSplit(controller, sVoid);
        } catch (ServiceException e) {
            LOG.error("", e);
        }
    }
}