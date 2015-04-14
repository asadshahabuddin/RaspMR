package com.rasp.utils.autodiscovery.impl;

import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;

import static org.junit.Assert.assertEquals;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 3/30/15
 * Edited :
 */
public class DiscovererHazelCastImplTest {

//    @Test
//    public void testServiceDiscoveryGetServices(){
//
//        HazelcastInstance hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.JOB_TRACKER,9200));
//        HazelcastInstance hz2 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.TASK_TRACKER,9100));
//        HazelcastInstance hz3 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.TASK_TRACKER,9100));
//
//        DiscovererHazelCastImpl discovererHazelCast1 = new DiscovererHazelCastImpl(hz1);
//
//        assertEquals(discovererHazelCast1.getServices(ServiceType.TASK_TRACKER).size(),2);
//
//    }
//
//    @Test
//    public void testServiceDiscovery(){
//        HazelcastInstance hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.JOB_TRACKER,9200));
//        HazelcastInstance hz2 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.TASK_TRACKER,9100));
//        HazelcastInstance hz3 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.TASK_TRACKER,9100));
//
//        DiscovererHazelCastImpl discovererHazelCast1 = new DiscovererHazelCastImpl(hz1);
//
//        for(Service service : discovererHazelCast1.getServices()){
//            System.out.println(service.getIp() + "---" + service.getPort() +"---" + service.getServiceType());
//        }
//
//    }


}
