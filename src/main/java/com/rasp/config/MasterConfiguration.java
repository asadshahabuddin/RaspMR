package com.rasp.config;

import com.rasp.fs.DataNode;
import com.rasp.fs.master.DataNodeClientImpl;
import com.rasp.fs.protobuf.ProtoClient;
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
    private ProtoClient protoClient;

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

}
