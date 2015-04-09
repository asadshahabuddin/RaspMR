/**
 * Author : Rahul Madhavan
 * File   : Task.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Apr 9, 2015
 */

package com.rasp.mr;

/* Import list */
import com.rasp.fs.InputSplit;
import com.rasp.utils.autodiscovery.Service;

import java.io.IOException;

/**
 * Task represents a {@link Mapper} or {@link Reducer}
 * running for a single input split
 *
 */
public interface Task {
    /**
     * returns the Job to which the task belongs
     *
     * @return {@link com.rasp.mr.Job}
     */
    Job getJob();

    /**
     * returns the {@link com.rasp.fs.InputSplit}which represents the input for the task
     *
     * @return
     */
    InputSplit getTaskInputSplit();

    /**
     * sets the InputSplit for the Task
     *
     * @param inputSplit
     */
    void setTaskInputSplit(InputSplit inputSplit);

    /**
     * executes the task with the {@link com.rasp.fs.InputSplit} given from {@link Task#getTaskInputSplit}
     *
     * @return true if the task runs appropriately
     */
    boolean execute()
    	throws IllegalAccessException, InstantiationException,
               InterruptedException,   IOException;

    String getTaskId();

    void complete();

    boolean isCompleted();

    void setJob(Job job);

    Service getService() throws IOException, InterruptedException;
}
/* End of Task.java */