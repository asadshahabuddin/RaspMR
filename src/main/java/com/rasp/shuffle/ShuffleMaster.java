package com.rasp.shuffle;

import java.io.IOException;

import com.rasp.mr.Job;

/**
 * Author : rahulmadhavan, sourabhsuman
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited : 4/11/2015
 */
public interface ShuffleMaster {

    void shuffleDataTransferCompleted(String taskId);

    void createShuffleTasks(Job job) throws IOException, InterruptedException;

    void cleanup(Job job);
}
