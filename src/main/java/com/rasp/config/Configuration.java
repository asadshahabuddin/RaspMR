package com.rasp.config;

import com.hazelcast.core.HazelcastInstance;
import com.rasp.fs.DataNode;
import com.rasp.fs.protobuf.ProtoClient;
import raspmr.RaspMR.utils.autodiscovery.Discoverer;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;
import raspmr.RaspMR.utils.autodiscovery.impl.DiscovererHazelCastImpl;
import raspmr.RaspMR.utils.autodiscovery.impl.HazelCastServicePublisher;

import java.net.InetAddress;
import java.util.HashMap;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class Configuration {

    private HazelcastInstance hz1;
    private Discoverer discoverer;
    private HashMap<String,DataNode> dataNodeHashMap;
    private ProtoClient protoClient = new ProtoClient();
    private Service service;

    public static final int TASK_NODE_PORT = 9797;



    public Configuration(int portNo, ServiceType serviceType){
        hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(serviceType, portNo));
        discoverer = new DiscovererHazelCastImpl(hz1);
        dataNodeHashMap = new HashMap<>();
        protoClient = new ProtoClient();
        service = ServiceFactory.createService(serviceType,getLocalServiceAddress().getHostAddress(),portNo);
    }


    public Discoverer getDiscoverer(){
        return discoverer;
    }

    public InetAddress getLocalServiceAddress(){
        return hz1.getCluster().getLocalMember().getSocketAddress().getAddress();
    }

    public Service getService(){
        return service;
    }


}
