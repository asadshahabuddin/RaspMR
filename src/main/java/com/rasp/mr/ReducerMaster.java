package com.rasp.mr;


/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/12/15
 * Edited :
 */

/**
 * this class is responsible for handling all the operations
 * related to rhe reduce phase
 *
 */
public interface ReducerMaster {

    /**
     * Create the reduce tasks for the specified job.
     * @param job The job to work on.
     *
     */
    void createReducerTasksForJob(Job job);

    /**
     * Mark the reduce task as complete.
     * @param taskId Unique ID of the a task.
     *
     */
    void completeReduceTask(String taskId);

    /**
     * removes the intermediate data generated for the reduce phase
     *
     * @param job
     */
    void cleanup(Job job);

    /**
     * removes the intermediate data generated by the reduce phase
     *
     * @param job
     */
    void whenReduceComplete(Job job);

}
