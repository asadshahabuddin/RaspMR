package com.rasp.shuffle;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.rasp.utils.autodiscovery.Service;

/**
 * Author : Rahul Madhavan, Sourabh Suman
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited : 4/12/15
 */
public interface ShuffleSlave {

    void createDataHandlerFor(String key, Service service) throws FileNotFoundException;

    void storeDataFor(byte[] data, String key, Service service) throws IOException;

    void closeDataHandlerFor(String key, Service service) throws IOException;

}
