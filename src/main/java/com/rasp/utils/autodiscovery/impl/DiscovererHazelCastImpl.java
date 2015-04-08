package com.rasp.utils.autodiscovery.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.rasp.utils.autodiscovery.Discoverer;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;

import java.util.LinkedList;
import java.util.List;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 3/30/15
 * Edited :
 */
public class DiscovererHazelCastImpl implements Discoverer{

    private HazelcastInstance hz;

    public DiscovererHazelCastImpl(HazelcastInstance hazelcastInstance){
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
        if(null == hz){
            throw new IllegalStateException("Current service is not published as yet");
        }else{
            List<Service> services= new LinkedList<Service>();
            for(Member member: hz.getCluster().getMembers()){
                if(!member.localMember()){
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


