/**
 * Author : Rahul Madhavan
 * File   : ServiceFactory.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 30, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.utils.autodiscovery;

/* Import list */
import java.net.InetAddress;
import com.rasp.utils.autodiscovery.impl.ServiceImpl;


public class ServiceFactory {
    public static Service createService(ServiceType serviceType,
                                        String ip,
                                        int port) {
        return new ServiceImpl(serviceType,ip,port);
    }

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