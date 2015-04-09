/**
 * Author : Asad Shahabuddin, Rahul Madhavan
 * File   : KeyPacketServerImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr.master;

/* Import list */
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.google.protobuf.ServiceException;
import com.rasp.config.Configuration;
import com.rasp.config.MasterConfiguration;
import com.rasp.mr.JobNode;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class JobNodeServerImpl implements JobNode{
    private Service service;
    private FileOutputStream f;
    private MasterConfiguration configuration;


    public JobNodeServerImpl(MasterConfiguration configuration)
        throws FileNotFoundException
    {
        this.service  = ServiceFactory.createService(
                        ServiceType.TASK_TRACKER,
                        Configuration.DATA_NODE_PORT);
        this.configuration = configuration;
    }

    @Override
    public void mapCompleted(String taskId, Map<String, Long> keyCount) throws ServiceException {

        configuration.getJobTracker().completeMapTask(taskId,keyCount);
    }

    @Override
    public Service getService() {
        return service;
    }
}
/* End of KeyPacketServerImpl.java */