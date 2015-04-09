package com.rasp.mr;

import com.rasp.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public interface TaskNode {

    void sendTask(Task task);

    Service getService();

    void initiateDataTransferForKey(String key,Service service);

    void transferDataForKey(byte[] data, String key, Service service);

    void terminateTransferDataForKey(String key,Service service);


}
