/**
 * Author : Pulkit Jain
 * File   : Writable.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr;

import com.rasp.mr.slave.WritableImpl;

import java.io.IOException;
import java.util.Map;

public interface MapContext {

    /**
     * write to file
     */
    public void write(Writable key, Writable value) throws IOException;

    /**
     * Closing all the open connections
     */
    public void close() throws IOException;

    public Map<String,Long> getKeyCountMap();

    public void setKeyCountMap(Map<String,Long> keyCountMap);
}
