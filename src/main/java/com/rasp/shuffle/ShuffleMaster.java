package com.rasp.shuffle;

import com.rasp.mr.Job;
import com.rasp.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited :
 */
public interface ShuffleMaster {

    void shuffleDataTransferCompleted(String taskId);

    void run(Job job);

}
