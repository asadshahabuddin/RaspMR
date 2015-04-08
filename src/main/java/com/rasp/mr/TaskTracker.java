/**
 * Author : Rahul Madhavan
 * File   : TaskTracker.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.mr;

/**
 * <code>TaskTracker</code> is responsible for
 * (1) maintaining a queue of tasks.
 * (2) tracking progress of tasks.
 * (3) Reporting progress of task to {@link com.rasp.mr.JobTracker}
 * (4) executing tasks in an ordered manner
 */
public interface TaskTracker
{
    /**
     * Submits task for execution
     *
     * @param {@link Task}
     */
    public void submit(Task task);

    /**
     * returns the next task to be executed
     *
     * @return {@link Task}
     */
    public Task nextTask();
}