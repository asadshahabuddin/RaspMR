package com.rasp.shuffle;

import java.io.IOException;

import com.rasp.mr.Job;

/**
 * Author : Sourabh Suman
 * File   : ShuffleMaster.java
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/9/15
 * Edited : 4/11/2015
 */

public interface ShuffleMaster {

	/**
	 * This method is called when data transfer is completed between two machines.
	 * @param taskId
	 */
    void shuffleDataTransferCompleted(String taskId);

    /**
     * creates shuffle tasks - one for each key 
     * @param job
     * @throws IOException
     * @throws InterruptedException
     */
    void createShuffleTasks(Job job) throws IOException, InterruptedException;

    /**
     * checks if given job's shuffle is completed or not
     * @param job
     */
    public void checkShuffleComplete(Job job);

    /**
     * removes all temp data and files from fs and clears all objects and data structures
     * @param job
     */
    void cleanup(Job job);
}
