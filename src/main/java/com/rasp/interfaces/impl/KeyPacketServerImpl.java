/**
 * Author : Asad Shahabuddin
 * File   : KeyPacketServerImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.interfaces.impl;

/* Import list */
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import com.rasp.config.Configuration;
import com.rasp.interfaces.KeyPacket;
import com.rasp.config.SlaveConfiguration;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;

public class KeyPacketServerImpl
    implements KeyPacket
{
    private Service service;
    private FileOutputStream f;
    private SlaveConfiguration conf;

    /**
     * Constructor
     * @param conf
     *            Slave configuration object.
     * @throws FileNotFoundException
     */
    public KeyPacketServerImpl(SlaveConfiguration conf)
        throws FileNotFoundException
    {
        this.service  = ServiceFactory.createService(
                        ServiceType.TASK_TRACKER,
                        Configuration.DATA_NODE_PORT);
        this.conf = conf;
    }

    @Override
    public Service getService()
    {
        return service;
    }

    @Override
    public void storeKey(Object key)
    {
        // TODO
    }
}
/* End of KeyPacketServerImpl.java */