/**
 * Author : Rahul Madhavan
 * File   : JobTracker.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr;

/* Import list */

import com.rasp.shuffle.ShuffleMaster;

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
     * Send the task to a worker node for execution. 
     * @param task
     *            The task to execute on a worker node.
     * @throws IOException
     * @throws InterruptedException
     */
    void sendTask(Task task) throws IOException, InterruptedException;


    MapperMaster getMapperMaster();

    ShuffleMaster getShuffleMaster();

    ReducerMaster getReducerMaster();

    void cleanup(Job job);

    void map(Job job) throws IOException, InterruptedException;

    void reduce(Job job);

    void shuffle(Job job) throws IOException, InterruptedException;
}