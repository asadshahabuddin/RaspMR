/**
 * Author : Rahul Madhavan
 * File   : Configuration.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited :
 */

package com.rasp.config;

/* Import list */
import java.net.InetAddress;
import com.rasp.utils.autodiscovery.Service;
import com.hazelcast.core.HazelcastInstance;
import com.rasp.utils.autodiscovery.Discoverer;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.impl.DiscovererHazelCastImpl;
import com.rasp.utils.autodiscovery.impl.HazelCastServicePublisher;

public class Configuration {
    /* Constants */
    public static final int TASK_NODE_PORT = 9797;
    public static final int DATA_NODE_PORT = 9292;
    public static final int JOB_NODE_PORT = 9595;

    private HazelcastInstance hz1;
    private Discoverer discoverer;
    private Service service;

    public Configuration(int portNo, ServiceType serviceType) {
        hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(serviceType, portNo));
        discoverer = new DiscovererHazelCastImpl(hz1);
        service = ServiceFactory.createService(serviceType,getLocalServiceAddress().getHostAddress(),portNo);
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