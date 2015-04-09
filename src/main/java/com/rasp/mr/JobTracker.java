/**
 * Author : Rahul Madhavan
 * File   : JobTracker.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr;

/* Import list */

import java.io.IOException;
import java.util.Map;

/**
 * <code>JobTracker</code>  is responsible for maintaining queue of jobs
 * and executing jobs in specified order. Executing a job includes, splitting the job into
 * multiple tasks and submitting task to {@link TaskTracker}. The <code>JobTracker</code> is responsible for
 * creating and executing the tasks of a job in the required sequence and keeping
 * track of the progress of the job
 *
 *
 */
public interface JobTracker
{
    /**
     * Submits Job for execution.
     * @param job {@link com.rasp.mr.Job}
     */
    void submit(Job job);

    /**
     * Returns the next task to be executed.
     * @return {@link Job}
     */
    Job nextJob();

    /**
     * Create map tasks for the specified job.
     * @param job
     *            The job to work on.
     */
    void createMapperTasksForJob(Job job);

    /**
     * Create reduce tasks for the specified job.
     * @param job
     *            The job to work on.
     */
    void createReducerTasksForJob(Job job);

    /**
     * Mark the map task as complete.
     * @param taskId
     *            Unique ID of the a task.
     */
    void completeMapTask(String taskId, Map<String, Long> keyCount);

    /**
     * Send the task to a worker node for execution. 
     * @param task
     *            The task to execute on a worker node.
     * @throws IOException
     * @throws InterruptedException
     */
    void sendTask(Task task) throws IOException, InterruptedException;
}