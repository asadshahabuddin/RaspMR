package com.rasp.config;

import com.rasp.fs.DataMaster;
import com.rasp.fs.DataNode;
import com.rasp.fs.master.DataNodeClientImpl;
import com.rasp.utils.protobuf.ProtoClient;
import com.rasp.mr.JobTracker;
import com.rasp.mr.master.JobScheduler;
import com.rasp.mr.TaskNode;
import com.rasp.mr.slave.TaskNodeClientImpl;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;

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
    private JobScheduler jobScheduler;

    public MasterConfiguration(int portNo, ServiceType serviceType){
        super(portNo,serviceType);
        protoClient = new ProtoClient();
        dataNodeMap = new HashMap<>();
        taskNodeMap = new HashMap<>();
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

    public JobScheduler getJobScheduler() {
        return jobScheduler;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }
}
