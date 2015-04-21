/**
 * Author : Asad Shahabuddin
 * File   : DataNode.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited : Apr 2, 2015
 */

package com.rasp.fs;

import java.io.IOException;
import com.rasp.utils.autodiscovery.Service;

/**
 * Worker class wrapping input splits.
 */
public interface DataNode {
    /**
     * Store/send split metadata depending upon its implementation.
     *
     * @param inputSplit Meta data for the split.
     * @throws InterruptedException
     * @throws IOException
     */
    void storeInputSplit(InputSplitImpl inputSplit)
            throws InterruptedException, IOException;

    /**
     * Store/send split data depending upon its implementation.
     *
     * @param b Data for the split as a sequence of bytes.
     * @throws InterruptedException
     * @throws IOException
     */
    void storeChunk(byte[] b)
            throws InterruptedException, IOException;

    /**
     * Close the byte stream.
     *
     * @throws IOException
     */
    void closeInputSplit()
            throws IOException;

    /**
     * Get the service object.
     *
     * @return The service object.
     */
    Service getService();
}