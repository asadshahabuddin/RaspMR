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

//    void initiateTransferDataForKey(String key);
//
//    void transferDataForKey(byte[] data, String key);
//
//    void terminateTransferDataForKey(String key);


}
