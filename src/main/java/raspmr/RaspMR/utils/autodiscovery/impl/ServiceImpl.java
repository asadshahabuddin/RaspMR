package raspmr.RaspMR.utils.autodiscovery.impl;

import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 3/30/15
 * Edited :
 */
public class ServiceImpl implements Service {

    private int port;
    private String ip;
    private ServiceType serviceType;

    public ServiceImpl(ServiceType serviceType, String ip, int port){
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
