/**
 * Author : Pulkit Jain
 * File   : Service.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery;

/**
 * this class is a representation for a service
 */
public interface Service {

    /**
     *
     * @return the port on which the service is listening
     */
    int getPort();

    /**
     *
     * @return the ip address to of the machine on which the service is runnning
     */
    String getIp();

    /**
     *
     * @return the {@link com.rasp.utils.autodiscovery.ServiceType} of the service
     */
    ServiceType getServiceType();
}
/* End of Service.java */