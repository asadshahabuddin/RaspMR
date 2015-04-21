/**
 * Author : Pulkit Jain
 * File   : Discoverer.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery;

/* Import list */
import java.util.List;

/**
 *
 * This class helps to discover {@link com.rasp.utils.autodiscovery.Service} on the network
 */
public interface Discoverer {

    /**
     *
     * @param serviceType
     * @return services of the given service type on on the network
     */
    List<Service> getServices(ServiceType serviceType);

    /**
     *
     * @return all the services on teh network
     */
    List<Service> getServices();
}
/* End of Discoverer.java */