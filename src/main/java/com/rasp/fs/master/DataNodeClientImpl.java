package com.rasp.fs.master;

import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.pro.duplex.RpcClientChannel;
import com.rasp.fs.DataNode;
import com.rasp.fs.InputSplit;
import com.rasp.fs.SInputSplitProtos;
import com.rasp.fs.protobuf.ProtoClient;
import raspmr.RaspMR.utils.autodiscovery.Service;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/3/15
 * Edited :
 */
public class DataNodeClientImpl implements DataNode {

    private ProtoClient protoClient;
    private Service service;
    private SInputSplitProtos.DataTransferService.BlockingInterface transferService;
    private RpcController controller;


    public DataNodeClientImpl(ProtoClient protoClient, Service service){
        this.protoClient = protoClient;
        this.service = service;
        initalizeServiceAndController();
    }

    private void initalizeServiceAndController(){
        RpcClientChannel channel = protoClient.getConnection(service.getIp(),service.getPort());
        transferService = SInputSplitProtos.DataTransferService.newBlockingStub(channel);
        controller = channel.newRpcController();
    }

    @Override
    public void storeInputSplit(InputSplit inputSplit) throws InterruptedException, IOException{
        SInputSplitProtos.SInputSplit sInputSplit = SInputSplitProtos.SInputSplit
                .newBuilder()
                .setIdx(inputSplit.getIdx())
                .setLength(inputSplit.getLength())
                .setLocation(inputSplit.getLocation())
                .setLength(inputSplit.getLength())
                .build();
        try {
            transferService.sendInputSplit(controller,sInputSplit);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeChunk(byte[] b) throws InterruptedException, IOException{
        SInputSplitProtos.SInputChunk sInputChunk = SInputSplitProtos.SInputChunk
                .newBuilder()
                .setChunk(ByteString.copyFrom(b))
                .build();

        try {
            transferService.sendChunk(controller, sInputChunk);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Service getService() {
        return service;
    }
}
