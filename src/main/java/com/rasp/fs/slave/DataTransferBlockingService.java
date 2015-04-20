/**
 * Author : Rahul Madhavan
 * File   : DataTransferBlockingService.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.fs.slave;

/* Import list */
import java.io.IOException;
import com.rasp.fs.DataNode;
import com.rasp.fs.InputSplitImpl;
import com.rasp.fs.SInputSplitProtos;
import com.google.protobuf.RpcController;
import com.rasp.config.SlaveConfiguration;
import com.google.protobuf.ServiceException;
import com.rasp.utils.file.FSHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTransferBlockingService
    implements SInputSplitProtos.DataTransferService.BlockingInterface {

    static final Logger LOG = LoggerFactory.getLogger(DataTransferBlockingService.class);

    private DataNode dataNode;
    public DataTransferBlockingService(DataNode dataNode) {
        this.dataNode = dataNode;
    }

    @Override
    public SInputSplitProtos.TransferResponse sendInputSplit(RpcController controller,
                                                            SInputSplitProtos.SInputSplit request) {
        try {

            dataNode.storeInputSplit(new InputSplitImpl(request.getIdx(),
                    request.getOffset(),
                    request.getLength(),
                    request.getInputFormatId()+"_"+request.getIdx(),
                    request.getInputFormatId()));

        } catch (InterruptedException e) {
            LOG.error("",e);
        } catch (IOException e) {
            LOG.error("",e);
        }
        return SInputSplitProtos.TransferResponse.newBuilder().setStatus("200").build();
    }

    @Override
    public SInputSplitProtos.TransferResponse sendChunk(RpcController controller,
                                                        SInputSplitProtos.SInputChunk request) {
        try {
            dataNode.storeChunk(request.getChunk().toByteArray());
        } catch (InterruptedException e) {
            LOG.error("",e);
        } catch (IOException e) {
            LOG.error("",e);
        }
        return SInputSplitProtos.TransferResponse.newBuilder().setStatus("200").build();

    }

    @Override
    public SInputSplitProtos.Void closeInputSplit(RpcController controller,
                                                  SInputSplitProtos.Void request)
        throws ServiceException {
        try {
            dataNode.closeInputSplit();
        } catch (IOException e) {
            LOG.error("",e);
        }
        return SInputSplitProtos.Void.newBuilder().build();
    }
}
/* End of DataTransferBlockingService.java */