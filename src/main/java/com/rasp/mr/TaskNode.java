/**
 * Author : Shivastuti Koul
 * File   : TaskNode.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr;

import java.io.IOException;
import com.rasp.utils.autodiscovery.Service;

public interface TaskNode {
    /**
     * Get the service.
     *
     * @return The service object.
     */
    Service getService();

    /**
     * Send a task.
     *
     * @param task The task to be sent.
     */
    void sendTask(Task task);

    /**
     * Initialize data transfer objects.
     *
     * @param key     The key.
     * @param jobId   The job identifier.
     * @param service Wrapper object containing the IP and port of a node.
     * @throws IOException
     */
    void initiateDataTransferForKey(String key, String jobId, Service service) throws IOException;

    /**
     * Transfer the data.
     *
     * @param data    Data object.
     * @param key     The key.
     * @param jobId   Job identifier.
     * @param service Service object.
     * @throws IOException
     */
    void transferDataForKey(byte[] data, String key, String jobId, Service service) throws IOException;

    /**
     * Terminate data transfer.
     *
     * @param key     The key.
     * @param jobId   Job identifier.
     * @param service Wrapper object containing the IP and port of a node.
     * @throws IOException
     */
    void terminateTransferDataForKey(String key, String jobId, Service service) throws IOException;

    /**
     * Perform cleanup.
     *
     * @param job The job for which cleanup needs to take place.
     */
    void cleanup(Job job);
}