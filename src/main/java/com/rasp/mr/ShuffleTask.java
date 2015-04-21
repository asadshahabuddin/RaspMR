/**
 * Author : Shivastuti Koul
 * File   : ShuffleTask.java
 * Email  : koul.sh@husky.neu.edu
 * Created: 4/9/15
 * Edited : 4/11/2015
 */

package com.rasp.mr;

import com.rasp.utils.autodiscovery.Service;

/**
 * Shuffle task representation.
 */
public interface ShuffleTask extends Task {
    /**
     * Set the key.
     *
     * @param key The key.
     */
    void setKey(String key);

    /**
     * Get the key.
     *
     * @return The key.
     */
    String getKey();

    /**
     * Set the service.
     *
     * @param service The service.
     */
    void setService(Service service);

    /**
     * Set the target service object.
     *
     * @param service The target servuce object.
     */
    void setDataTargetService(Service service);

    /**
     * Get the target service object.
     *
     * @return The target service object.
     */
    Service getDataTargetService();
}