/**
 * Author : Pulkit Jain
 * File   : DiscovererHazelCastImpl.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery.impl;

/* Import list */
import java.util.List;
import java.util.LinkedList;
import com.hazelcast.core.Member;
import com.rasp.utils.autodiscovery.Service;
import com.hazelcast.core.HazelcastInstance;
import com.rasp.utils.autodiscovery.Discoverer;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class DiscovererHazelCastImpl
    implements Discoverer {
    private HazelcastInstance hz;

    public DiscovererHazelCastImpl(HazelcastInstance hazelcastInstance) {
        hz = hazelcastInstance;
    }

    @Override
    public List<Service> getServices(ServiceType serviceType) {
        List<Service> allServices = getServices();
        List<Service> services = new LinkedList<Service>();

        for(Service service: allServices){
            if(service.getServiceType() == serviceType){
                services.add(service);
            }
        }
        return services;
    }

    @Override
    public List<Service> getServices() {
        if (null == hz) {
            throw new IllegalStateException("Current service is not published as yet");
        } else {
            List<Service> services = new LinkedList<Service>();
            for (Member member : hz.getCluster().getMembers()) {
                if (!member.localMember()) {
                    ServiceType memberServiceType = ServiceType.valueOf(
                            member.getStringAttribute(HazelCastServiceConstants.SERVICE_ATTRIBUTE));
                    int memberServicePort = member.getIntAttribute(HazelCastServiceConstants.PORT_ATTRIBUTE);
                    String memberServiceIp = member.getSocketAddress().getAddress().getHostAddress();

                    services.add(ServiceFactory.
                            createService(memberServiceType,
                                    memberServiceIp,
                                    memberServicePort));
                }
            }
            return services;
        }
    }
}
/* End of DiscovererHazelCastImpl.java */