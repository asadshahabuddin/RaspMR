/**
 * Author : Pulkit Jain
 * File   : Writable.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Context {

    /**
     * write to file
     */
    public void write(WritableImpl key, WritableImpl value) throws IOException;
}
