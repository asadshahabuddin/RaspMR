package com.rasp.utils.autodiscovery;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 3/30/15
 * Edited :
 */
public interface Service {

    int getPort();

    String getIp();

    ServiceType getServiceType();

}
