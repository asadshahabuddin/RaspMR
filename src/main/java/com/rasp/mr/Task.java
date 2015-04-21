/**
 * Author : Shivastuti Koul
 * File   : Task.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Mar 23, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr;

/* Import list */
import java.io.IOException;
import com.rasp.fs.InputSplit;
import com.google.protobuf.ServiceException;
import com.rasp.utils.autodiscovery.Service;

/**
 * Task represents a {@link Mapper} or {@link Reducer}
 * running for a single input split.
 *
 */
public interface Task {
    /**
     * Returns the Job to which the task belongs.
     *
     * @return {@link com.rasp.mr.Job}
     */
    Job getJob();

    /**
     * Returns the {@link com.rasp.fs.InputSplit}which represents
     * the input for the task.
     *
     * @return The input split.
     */
    InputSplit getTaskInputSplit();

    /**
     * Sets the InputSplit for the Task.
     *
     * @param inputSplit The input split.
     */
    void setTaskInputSplit(InputSplit inputSplit);

    /**
     * Executes the task with the {@link com.rasp.fs.InputSplit} given from {@link Task#getTaskInputSplit}.
     *
     * @return true if the task runs appropriately
     */
    boolean execute()
            throws IllegalAccessException, InstantiationException,
            InterruptedException, IOException, ServiceException;

    /**
     * Get the task identifier.
     *
     * @return The task ID.
     */
    String getTaskId();

    /**
     * Set the completion status to true.
     */
    void complete();

    /**
     * Return the task completion status.
     *
     * @return The completion status.
     */
    boolean isCompleted();

    /**
     * Set the job.
     *
     * @param job The job.
     */
    void setJob(Job job);

    /**
     * Get the service object.
     *
     * @return The service object.
     * @throws IOException
     * @throws InterruptedException
     */
    Service getService() throws IOException, InterruptedException;
}