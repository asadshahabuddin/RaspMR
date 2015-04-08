package com.rasp.config;

import com.hazelcast.core.HazelcastInstance;
import com.rasp.utils.autodiscovery.Discoverer;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.impl.DiscovererHazelCastImpl;
import com.rasp.utils.autodiscovery.impl.HazelCastServicePublisher;

import java.net.InetAddress;

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
    private Service service;

    public static final int TASK_NODE_PORT = 9797;
    public static final int DATA_NODE_PORT = 9292;



    public Configuration(int portNo, ServiceType serviceType){
        hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(serviceType, portNo));
        discoverer = new DiscovererHazelCastImpl(hz1);
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
