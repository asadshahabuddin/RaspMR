package com.rasp.config;

import com.rasp.fs.DataMaster;
import com.rasp.fs.DataNode;
import com.rasp.fs.master.DataNodeClientImpl;
import com.rasp.fs.protobuf.ProtoClient;
import com.rasp.interfaces.JobTracker;
import com.rasp.interfaces.impl.TaskNode;
import com.rasp.interfaces.impl.TaskNodeClientImpl;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/3/15
 * Edited :
 */
public class MasterConfiguration extends Configuration {

    private Map<String,DataNode> dataNodeMap;
    private Map<String,TaskNode> taskNodeMap;
    private ProtoClient protoClient;
    private DataMaster dataMaster;
    private JobTracker jobTracker;

    public MasterConfiguration(int portNo, ServiceType serviceType){
        super(portNo,serviceType);
        protoClient = new ProtoClient();
        dataNodeMap = new HashMap<>();
    }

    public DataNode getDataNode(Service service){
        if(service.getServiceType() == ServiceType.TASK_TRACKER){
            String key = service.getIp()+ ":" + service.getPort();
            if(!dataNodeMap.containsKey(key)){
                dataNodeMap.put(key,new DataNodeClientImpl(protoClient,service));
            }
            return dataNodeMap.get(key);
        }else{
            throw new IllegalArgumentException("Service should be a slave: argument service is for master");
        }

    }

    public TaskNode getTaskNode(Service service) {
        if(service.getServiceType() == ServiceType.TASK_TRACKER){
            String key = service.getIp()+ ":" + service.getPort();
            if(!taskNodeMap.containsKey(key)){
                taskNodeMap.put(key,new TaskNodeClientImpl(protoClient,service));
            }
            return taskNodeMap.get(key);
        }else{
            throw new IllegalArgumentException("Service should be a slave: argument service is for master");
        }
    }

    public DataMaster getDataMaster(){
        return dataMaster;
    }

    public void setDataMaster(DataMaster dataMaster){
        this.dataMaster = dataMaster;
    }

    public JobTracker getJobTracker() {
        return jobTracker;
    }

    public void setJobTracker(JobTracker jobTracker) {
        this.jobTracker = jobTracker;
    }
}
