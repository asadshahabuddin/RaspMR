/**
 * Author : Shivastuti Koul
 * File   : TaskNodeServerImpl.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Apr 5, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import com.rasp.mr.*;
import org.slf4j.Logger;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import com.rasp.utils.file.FSHelpers;
import com.rasp.config.Configuration;
import com.rasp.config.SlaveConfiguration;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class TaskNodeServerImpl
    implements TaskNode {
    static final Logger LOG = LoggerFactory.getLogger(TaskNodeServerImpl.class);

    private Service service;
    private SlaveConfiguration configuration;

    /**
     * Constructor 1
     *
     * @param configuration The configuration object.
     */
    public TaskNodeServerImpl(SlaveConfiguration configuration) {
        this.configuration = configuration;
        this.service = ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                this.configuration.getLocalServiceAddress().getHostAddress(),
                Configuration.TASK_NODE_PORT);
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public void sendTask(Task task) {
        LOG.debug("task " + task.getTaskId() + " : task Type : " + task.getClass());
        configuration.getTaskTracker().submit(task);
    }


    @Override
    public void initiateDataTransferForKey(String key, String jobId, Service service) throws IOException {
        configuration.getShuffleSlave().createDataHandlerFor(key, jobId, service);
    }

    @Override
    public void transferDataForKey(byte[] data, String key, String jobId, Service service) throws IOException {
        configuration.getShuffleSlave().storeDataFor(data, key, jobId, service);
    }

    /**
     * Terminate data transfer.
     *
     * @param key     The key.
     * @param jobId   Job identifier.
     * @param service Wrapper object containing the IP and port of a node.
     * @throws IOException
     */
    public void terminateTransferDataForKey(String key, String jobId, Service service) throws IOException {
        configuration.getShuffleSlave().closeDataHandlerFor(key, jobId, service);
    }

    @Override
    public void cleanup(Job job) {
        FSHelpers.deleteFilesForJob(job);
    }
}