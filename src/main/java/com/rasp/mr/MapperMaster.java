package com.rasp.mr;

import com.rasp.utils.autodiscovery.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/12/15
 * Edited :
 */

/**
 * this class is responsible for handling all the operations
 * related to rhe map phase
 *
 */
public interface MapperMaster {

    /**
     * Create the map tasks for the specified job.
     *
     * @param job The job to work on.
     */
    void createMapperTasksForJob(Job job);

    /**
     * Mark the map task as complete.
     * @param taskId Unique ID of the a task.
     */
    void completeMapTask(String taskId, Map<String, Long> keyCount);

    /**
     * removes the intermediate data generated for the map phase
     *
     * @param job
     */
    void cleanup(Job job);

    /**
     * a callback which is to be invoked when all map tasks are completed
     *
     * @param job
     */
    void whenMapComplete(Job job);


}
