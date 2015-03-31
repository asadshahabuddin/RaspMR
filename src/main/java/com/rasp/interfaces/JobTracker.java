/**
 * Author : Rahul Madhavan
 * File   : JobTracker.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.interfaces;

/* Import list */
import java.util.List;

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
     * Submits Job for execution
     *
     * @param job {@link Job}
     */
    void submit(Job job);

    /**
     * returns the next task to be executed
     *
     * @return {@link Job}
     */
    Job nextJob();

    /**
     *
     * @return true if the <code>TaskTracker</code> has started successfully
     */
    boolean start();

    /**
     *
     * @return true if the <code>TaskTracker</code> has stopped successfully
     */
    boolean stop();

    /**
     * returns the list of Tasks for the given job
     *
     * @param job
     * @return {@link List<Task>}
     */
    List<Task> getTasksForJob(Job job);
}