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


import java.io.IOException;
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

    void initiateDataTransferForKey(String key, String jobId,Service service) throws IOException;

    void transferDataForKey(byte[] data, String key, String jobId, Service service) throws IOException;

    void terminateTransferDataForKey(String key, String jobId, Service service) throws IOException;

    void cleanup(Job job);
}
/* End of TaskNode.java */
