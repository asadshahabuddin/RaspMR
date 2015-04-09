/**
 * Author : Rahul Madhavan
 * File   : Service.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery;

public interface Service {
    int getPort();

    String getIp();

    ServiceType getServiceType();
}
/* End of Service.java */