/**
 * Author : Pulkit Jain
 * File   : ServiceImpl.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery.impl;

/* Import list */
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;

public class ServiceImpl implements Service {
    private int port;
    private String ip;
    private ServiceType serviceType;

    public ServiceImpl(ServiceType serviceType, String ip, int port) {
        this.port = port;
        this.ip = ip;
        this.serviceType = serviceType;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public ServiceType getServiceType() {
        return serviceType;
    }
}
/* End of ServiceImpl.java */