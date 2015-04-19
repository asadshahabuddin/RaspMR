/**
 * Author : Rahul Madhavan
 * File   : Configuration.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited :
 */

package com.rasp.config;

/* Import list */
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.rasp.mr.TaskNode;
import com.rasp.mr.slave.TaskNodeClientImpl;
import com.rasp.utils.autodiscovery.Service;
import com.hazelcast.core.HazelcastInstance;
import com.rasp.utils.autodiscovery.Discoverer;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.impl.DiscovererHazelCastImpl;
import com.rasp.utils.autodiscovery.impl.HazelCastServicePublisher;
import com.rasp.utils.protobuf.ProtoClient;

public class Configuration {
    /* Constants */
    public static final int TASK_NODE_PORT = 9797;
    public static final int DATA_NODE_PORT = 9292;
    public static final int JOB_NODE_PORT = 9393;

    protected ProtoClient protoClient;
    private HazelcastInstance hz1;
    private Discoverer discoverer;
    private Service service;
    private Map<String,TaskNode> taskNodeMap;

    public Configuration(int portNo, ServiceType serviceType) throws FileNotFoundException {
        protoClient = new ProtoClient();
        taskNodeMap = new HashMap<>();
        hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(serviceType, portNo));
        discoverer = new DiscovererHazelCastImpl(hz1);
        service = ServiceFactory.createService(serviceType,getLocalServiceAddress().getHostAddress(),portNo);
    }

    public TaskNode getTaskNode(Service service) {
        if(service.getServiceType() == ServiceType.TASK_TRACKER){
            String key = service.getIp()+ ":" + service.getPort();
            if(!taskNodeMap.containsKey(key)) {
                taskNodeMap.put(key,new TaskNodeClientImpl(protoClient,service));
            }
            return taskNodeMap.get(key);
        } else {
            throw new IllegalArgumentException("Service should be a slave: argument service is for master");
        }
    }

    public Discoverer getDiscoverer() {
        return discoverer;
    }

    public InetAddress getLocalServiceAddress() {
        return hz1.getCluster().getLocalMember().getSocketAddress().getAddress();
    }

    public Service getService() {
        return service;
    }
}
/* End of Configuration.java */