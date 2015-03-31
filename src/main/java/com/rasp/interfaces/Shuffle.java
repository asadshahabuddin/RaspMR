/**
 * Author : Sourabh Suman
 * File   : Shuffle.java
 * Email  : 
 * Created: Mar 24, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.interfaces;

/* Import list */
import java.io.File;
import java.net.URL;

public interface Shuffle
{
    /**
     * @return ID of the current reduce task
     */
    public int getReduceID();
    
    /**
     * @return current reduce task
     */
    public Task getReduceTask();
    
    /**
     * @return Map output file being processed by this Shuffle instance 
     */
    public File getMapOutputFile();
    
    /**
     * For all map output values, find the reducer to which this
     * record should be sent, and then send it to that reducer
     * @param url of reduce where shuffled data for a reduce
     * task is to be sent
     */
    public void shuffleAndSendOutput(URL url);   
}
