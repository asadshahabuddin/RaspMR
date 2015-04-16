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
public interface MapperMaster {

    /**
     * Create map tasks for the specified job.
     * @param job
     *            The job to work on.
     */
    void createMapperTasksForJob(Job job);

    /**
     * Mark the map task as complete.
     * @param taskId
     *            Unique ID of the a task.
     */
    void completeMapTask(String taskId, Map<String, Long> keyCount);


    void cleanup(Job job);

    void checkMapComplete(Job job);


}
