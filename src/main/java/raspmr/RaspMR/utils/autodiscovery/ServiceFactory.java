package raspmr.RaspMR.utils.autodiscovery;

import raspmr.RaspMR.utils.autodiscovery.impl.ServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 3/30/15
 * Edited :
 */
public class ServiceFactory {

    public static Service createService(ServiceType serviceType, String ip, int port){
        return new ServiceImpl(serviceType,ip,port);
    }

    public static Service createService(ServiceType serviceType, int port){
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return new ServiceImpl(serviceType,ip,port);
    }

}
