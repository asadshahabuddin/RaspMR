/**
 * Author : Rahul Madhavan
 * File   : MasterConfiguration.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited :
 */

package com.rasp.config;

/* Import list */
import java.util.Map;
import java.util.HashMap;
import com.rasp.fs.DataNode;
import com.rasp.mr.JobNode;
import com.rasp.mr.TaskNode;
import com.rasp.fs.DataMaster;
import com.rasp.mr.JobTracker;
import com.rasp.mr.master.JobScheduler;
import com.rasp.utils.protobuf.ProtoClient;
import com.rasp.mr.slave.TaskNodeClientImpl;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.fs.master.DataNodeClientImpl;
import com.rasp.utils.autodiscovery.ServiceType;

public class MasterConfiguration extends Configuration {
    private Map<String,DataNode> dataNodeMap;


    private DataMaster dataMaster;
    private JobTracker jobTracker;
    private JobScheduler jobScheduler;
    private JobNode jobServer;

    public MasterConfiguration(int portNo, ServiceType serviceType) {
        super(portNo,serviceType);
        dataNodeMap = new HashMap<>();
    }

    public DataNode getDataNode(Service service) {
        if(service.getServiceType() == ServiceType.TASK_TRACKER){
            String key = service.getIp()+ ":" + service.getPort();
            if(!dataNodeMap.containsKey(key)){
                dataNodeMap.put(key,new DataNodeClientImpl(protoClient,service));
            }
            return dataNodeMap.get(key);
        } else {
            throw new IllegalArgumentException("Service should be a slave: argument service is for master");
        }
    }


    public DataMaster getDataMaster() {
        return dataMaster;
    }

    public void setDataMaster(DataMaster dataMaster) {
        this.dataMaster = dataMaster;
    }

    public JobTracker getJobTracker() {
        return jobTracker;
    }

    public void setJobTracker(JobTracker jobTracker) {
        this.jobTracker = jobTracker;
    }

    public JobScheduler getJobScheduler() {
        return jobScheduler;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    public JobNode getJobServer() {
        return jobServer;
    }

    public void setJobServer(JobNode jobServer) {
        this.jobServer = jobServer;
    }
}
/* End of MasterConfiguration.java */