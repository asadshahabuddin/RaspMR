/**
 * Author : Rahul Madhavan
 * File   : HazelCastServicePublisher.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery.impl;

/* Import list */
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.rasp.utils.autodiscovery.Service;
import com.hazelcast.config.XmlConfigBuilder;

import java.io.FileNotFoundException;

public enum HazelCastServicePublisher {
    DUMMY;

    public static HazelcastInstance publishService(Service service) throws FileNotFoundException {
        Config config = new XmlConfigBuilder(System.getProperty("hazelcast.config.path")).build();
        config.getMemberAttributeConfig().
                setStringAttribute(
                        HazelCastServiceConstants.SERVICE_ATTRIBUTE, service.getServiceType().toString());
        config.getMemberAttributeConfig().
                setIntAttribute(HazelCastServiceConstants.PORT_ATTRIBUTE, service.getPort());
        // config.setProperty( "hazelcast.logging.type", "none" );
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        return hz;
    }
}
/* End of HazelCastServicePublisher.java */