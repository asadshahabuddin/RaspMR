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
 * <code>JobTracker</code>  is responsible for managing the
 * lifecycle of the jobs that are submitted to it.
 * the job tracker uses the
 * {@link com.rasp.mr.MapperMaster},
 * {@link com.rasp.shuffle.ShuffleMaster}
 * {@link com.rasp.mr.ReducerMaster}
 * to manage the life cycle of the job in its respective phases
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
     * Returns the next job to be executed.
     * @return {@link Job}
     */
    Job nextJob();

    /**
     * Send the task to a slave node for execution.
     * @param task
     *            The task to execute on a worker node.
     * @throws IOException
     * @throws InterruptedException
     */
    void sendTask(Task task) throws IOException, InterruptedException;


    /**
     * @return the {@link com.rasp.mr.MapperMaster} for the job tracker
     */
    MapperMaster getMapperMaster();

    /**
     * @return the {@link com.rasp.shuffle.ShuffleMaster} for the job tracker
     */
    ShuffleMaster getShuffleMaster();

    /**
     * @return the {@link com.rasp.mr.ReducerMaster} for the job tracker
     */
    ReducerMaster getReducerMaster();

    /**
     * begins the execution of the given job
     *
     * @param job
     * @throws IOException
     * @throws InterruptedException
     */
    void execute(Job job) throws IOException, InterruptedException;
}