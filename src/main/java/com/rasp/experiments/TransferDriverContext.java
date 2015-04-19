package com.rasp.experiments;

import com.hazelcast.core.HazelcastInstance;
import com.rasp.utils.autodiscovery.Discoverer;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.impl.DiscovererHazelCastImpl;
import com.rasp.utils.autodiscovery.impl.HazelCastServicePublisher;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.util.List;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public final class TransferDriverContext {

    private HazelcastInstance hz1;
    private Discoverer discoverer;

    public TransferDriverContext(int portNo){
        try {
            hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.TASK_TRACKER, portNo));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        discoverer = new DiscovererHazelCastImpl(hz1);
    }

    public List<Service> discover(){
        return discoverer.getServices();
    }

    public InetAddress getLocalServiceAddress(){
        return hz1.getCluster().getLocalMember().getSocketAddress().getAddress();
    }

}
