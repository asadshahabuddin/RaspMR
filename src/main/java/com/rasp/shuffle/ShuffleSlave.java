package com.rasp.shuffle;

import java.io.IOException;

import com.rasp.utils.autodiscovery.Service;

/**
 * Author : Sourabh Suman
 * File   : ShuffleSlave.java
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/9/15
 * Edited : 4/12/15
 */
public interface ShuffleSlave {

	/**
	 * create file handle for storing data received from different machines in fs 
	 * @param key
	 * @param jobId
	 * @param service
	 * @throws IOException
	 */
    void createDataHandlerFor(String key, String jobId, Service service) throws IOException;

    /**
     * store data received from different machines in fs
     * @param data
     * @param key
     * @param jobId
     * @param service
     * @throws IOException
     */
    void storeDataFor(byte[] data, String key, String jobId, Service service) throws IOException;

    /**
     * create file handle opened for storing data received from different machines in fs 
     * @param key
     * @param jobId
     * @param service
     * @throws IOException
     */
    void closeDataHandlerFor(String key, String jobId, Service service) throws IOException;

}
