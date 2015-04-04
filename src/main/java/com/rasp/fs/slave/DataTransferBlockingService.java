package com.rasp.fs.slave;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.rasp.fs.DataNode;
import com.rasp.fs.InputSplit;
import com.rasp.fs.SInputSplitProtos;
import raspmr.RaspMR.experiments.protobuf.TransferDataProtos;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class DataTransferBlockingService implements SInputSplitProtos.DataTransferService.BlockingInterface {


    private DataNode dataNode;

    public DataTransferBlockingService(DataNode dataNode){
        this.dataNode = dataNode;
    }

    @Override
    public SInputSplitProtos.TransferResponse sendInputSplit(RpcController controller, SInputSplitProtos.SInputSplit request) {
        try {
            dataNode.storeInputSplit(new InputSplit(request.getIdx(), request.getOffset(), request.getLength(), request.getLocation()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SInputSplitProtos.TransferResponse.newBuilder().setStatus("200").build();
    }

    @Override
    public SInputSplitProtos.TransferResponse sendChunk(RpcController controller, SInputSplitProtos.SInputChunk request) {
        try {
            dataNode.storeChunk(request.getChunk().toByteArray());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SInputSplitProtos.TransferResponse.newBuilder().setStatus("200").build();

    }



}
