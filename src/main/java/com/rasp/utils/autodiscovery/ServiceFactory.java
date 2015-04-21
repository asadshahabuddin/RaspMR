/**
 * Author : Pulkit Jain
 * File   : ServiceFactory.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery;

/* Import list */
import java.net.InetAddress;
import com.rasp.utils.autodiscovery.impl.ServiceImpl;

/**
 *
 *  This class is a Factory for creating objects of type {@link com.rasp.utils.autodiscovery.Service}
 */
public class ServiceFactory {
    public static Service createService(ServiceType serviceType,
                                        String ip,
                                        int port) {
        return new ServiceImpl(serviceType,ip,port);
    }

    /**
     * creates {@link com.rasp.utils.autodiscovery.Service} for given {@link com.rasp.utils.autodiscovery.ServiceType}
     * and port
     *
     * @param serviceType
     * @param port
     * @return
     */
    public static Service createService(ServiceType serviceType, int port) {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return new ServiceImpl(serviceType,ip,port);
    }
}
/* End of ServiceFactory.java */