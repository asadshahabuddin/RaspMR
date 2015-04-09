package com.rasp.shuffle;

import com.rasp.config.SlaveConfiguration;
import com.rasp.utils.autodiscovery.Service;

/**
 * Author : Sourabh Suman, Rahul Madhavan
 * File   : ReducerTask.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

public class ShuffleSlaveImpl implements ShuffleSlave {

    SlaveConfiguration config;

    public ShuffleSlaveImpl(SlaveConfiguration conf){
        config = conf;
    }

    @Override
    public void createDataHandlerFor(String key, Service service) {
        //create a key K using key and service data
        //create a file for K
    }

    @Override
    public void storeDataFor(byte[] data, String key, Service service) {
        //create a key K using key and service data
        //fetch the file for K and append data to it
    }

    @Override
    public void closeDataHandlerFor(String key, Service service) {
        //create a key using key and service data
        //fetch the file for K and close it
    }

}
