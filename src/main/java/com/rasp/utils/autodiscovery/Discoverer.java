package com.rasp.utils.autodiscovery;

import java.util.List;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 3/30/15
 * Edited :
 */
public interface Discoverer {

    List<Service> getServices(ServiceType serviceType);

    List<Service> getServices();

}
