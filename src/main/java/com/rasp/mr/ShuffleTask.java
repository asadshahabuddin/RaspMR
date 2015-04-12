package com.rasp.mr;

import com.rasp.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan, sourabhsuman
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited : 4/11/2015
 */
public interface ShuffleTask extends Task{

    void setKey(String key);

    String getKey();
    
    void setService(Service service);
    
    Service getDataTargetService();
    
    void setDataTargetService(Service service);

}
