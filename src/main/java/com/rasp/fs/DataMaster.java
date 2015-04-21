/**
 * Author : Asad Shahabuddin
 * File   : DataMaster.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.fs;

import java.io.IOException;

/**
 * Master class wrapping input format and splits.
 */
public interface DataMaster {
    /**
     * Create an input format object.
     *
     * @param inputFile   The input file.
     * @param workerCount Number of slave nodes.
     * @return The input format object.
     * @throws InterruptedException
     * @throws IOException
     */
    public InputFormatImpl createInputFormat(String inputFile, int workerCount)
            throws InterruptedException, IOException;

    /**
     * Extract data for the logical splits and send to worker nodes.
     *
     * @param inputFile The input file.
     * @throws IOException
     * @throws InterruptedException
     */
    public void splitAndSend(String inputFile)
            throws IOException, InterruptedException;

    /**
     * Get the input format object.
     *
     * @param path Path of the input file.
     * @return The input format object.
     */
    public InputFormatImpl getInputFormat(String path);
}