/**
 * Author : Asad Shahabuddin
 * File   : KeyTransferBlockingService.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr.master;

/* Import list */
import com.rasp.fs.GroupingKeyProtos;
import com.rasp.mr.KeyPacket;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class KeyTransferBlockingService
{
    private KeyPacket packet;

    /**
     * Constructor
     * @param packet
     *            The key packet to be transmitted.
     */
    public KeyTransferBlockingService(KeyPacket packet)
    {
        this.packet = packet;
    }

    /**
     * TODO - Write a proper description.
     * @param controller
     *            The RPC controller.
     * @param req
     *            Request object.
     * @return
     *            Response object.
     */
    public GroupingKeyProtos.TransferResponse sendKey(RpcController controller,
                                                      GroupingKeyProtos.KeyPacket req)
        throws ServiceException
    {
        packet.storeKey(req.getKey());
        return GroupingKeyProtos.TransferResponse.newBuilder().setStatus("200").build();
    }
}
/* End of KeyTransferBlockingService.java */