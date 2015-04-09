/**
 * Author : Rahul Madhavan
 * File   : Discoverer.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery;

/* Import list */
import java.util.List;

public interface Discoverer {
    List<Service> getServices(ServiceType serviceType);

    List<Service> getServices();
}
/* End of Discoverer.java */