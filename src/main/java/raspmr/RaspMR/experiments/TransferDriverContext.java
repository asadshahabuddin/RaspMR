package raspmr.RaspMR.experiments;

import com.hazelcast.core.HazelcastInstance;
import raspmr.RaspMR.utils.autodiscovery.Discoverer;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;
import raspmr.RaspMR.utils.autodiscovery.impl.DiscovererHazelCastImpl;
import raspmr.RaspMR.utils.autodiscovery.impl.HazelCastServicePublisher;

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
        hz1 = HazelCastServicePublisher.publishService(ServiceFactory.createService(ServiceType.TASK_TRACKER, portNo));
        discoverer = new DiscovererHazelCastImpl(hz1);
    }

    public List<Service> discover(){
        return discoverer.getServices();
    }

    public InetAddress getLocalServiceAddress(){
        return hz1.getCluster().getLocalMember().getSocketAddress().getAddress();
    }

}
