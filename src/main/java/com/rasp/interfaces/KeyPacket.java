/**
 * Author : Asad Shahabuddin
 * File   : KeyPacket.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.interfaces;

/* Import list */
import com.google.protobuf.ServiceException;
import raspmr.RaspMR.utils.autodiscovery.Service;

public interface KeyPacket
{
    /**
     * TODO - Write a proper description.
     * @return
     *            The auto discovery service object.
     */
    public Service getService();

    /**
     * TODO - Write a proper description.
     * @param key
     *            Object representing the key.
     * @throws ServiceException
     */
    public void storeKey(Object key)
        throws ServiceException;
}
/* End of KeyPacket.java */