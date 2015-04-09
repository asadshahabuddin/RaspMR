package com.rasp.shuffle;

import com.rasp.config.SlaveConfiguration;
import com.rasp.utils.autodiscovery.Service;

/**
 * Created by Sourabh on 4/8/2015.
 */
public class ShuffleSlaveImpl {

    SlaveConfiguration config;

    public ShuffleSlaveImpl(SlaveConfiguration conf){
        config = conf;
    }

    void copyMapDataAndSendAck(String key, Service target){

    }

    void sendAck(String key){

    }
}
