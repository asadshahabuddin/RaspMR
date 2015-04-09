/**
 * Author : rahulmadhavan
 * File   :
 * Author : Rahul Madhavan
 * File   : TaskNode.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr;

import com.google.protobuf.ServiceException;
import com.rasp.utils.autodiscovery.Service;

public interface TaskNode {
    /**
     * Get the service.
     * @return
     *            The service object.
     */
    Service getService();

    /**
     * Send a task.
     * @param task
     *            The task to be sent.
     */
    void sendTask(Task task);

    /**
     * Send a data transfer task from the server to a worker node.
     * @param task
     *            The data transfer task requested from a worker node.
     */
    void sendDataTransferTask(String key, Service service)
        throws ServiceException;

    void initiateDataTransferForKey(String key,Service service);

    void transferDataForKey(byte[] data, String key, Service service);

    void terminateTransferDataForKey(String key,Service service);
}
/* End of TaskNode.java */
