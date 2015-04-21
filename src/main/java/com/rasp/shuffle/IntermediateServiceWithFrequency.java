package com.rasp.shuffle;

import com.rasp.utils.autodiscovery.Service;

/**
 * Author : Sourabh Suman
 * File   : IntermediateServiceWithFrequency.java
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/9/15
 * Edited : 
 * Description: Class to hold intermediate data while processing shuffle.
 * It stores machine name and a frequency count.
 */

public class IntermediateServiceWithFrequency {

    private Service service;
    private long frequency;

    public IntermediateServiceWithFrequency(){}

    public IntermediateServiceWithFrequency(Service service, long frequency){
        this.service = service;
        this.frequency = frequency;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }
}
