/**
 * Author : Asad Shahabuddin
 * File   : KeyPacket.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr;

/* Import list */
import com.google.protobuf.ServiceException;
import com.rasp.utils.autodiscovery.Service;

import java.util.Map;

public interface JobNode
{

    public void mapCompleted(String taskId, Map<String,Long> keyCount)
        throws ServiceException;

    public void shuffleDataTransferCompleted(String taskId) throws ServiceException;

    public void reduceCompleted(String taskId) throws ServiceException;

    public Service getService();

}
/* End of KeyPacket.java */