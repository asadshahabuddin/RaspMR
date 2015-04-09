package com.rasp.shuffle;

import com.rasp.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited :
 */
public interface ShuffleSlave {

    void createDataHandlerFor(String key, Service service);

    void storeDataFor(byte[] data, String key, Service service);

    void closeDataHandlerFor(String key, Service service);



}
