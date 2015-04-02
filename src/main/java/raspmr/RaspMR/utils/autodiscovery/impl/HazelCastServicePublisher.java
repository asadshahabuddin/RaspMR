package raspmr.RaspMR.utils.autodiscovery.impl;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import raspmr.RaspMR.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 3/30/15
 * Edited :
 */
public enum HazelCastServicePublisher{
    DUMMY;

    public static HazelcastInstance publishService(Service service) {

        Config config = new XmlConfigBuilder().build();
        config.getMemberAttributeConfig().
                setStringAttribute(
                        HazelCastServiceConstants.SERVICE_ATTRIBUTE, service.getServiceType().toString());
        config.getMemberAttributeConfig().
                setIntAttribute(HazelCastServiceConstants.PORT_ATTRIBUTE, service.getPort());
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        return hz;
    }


}
