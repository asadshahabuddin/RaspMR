/**
 * Author : Pulkit Jain
 * File   : TaskTracker.java
 * Email  : 
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr;

/* Import list */
import com.google.protobuf.ServiceException;

import java.io.IOException;

public interface TaskScheduler
{
    /**
     * Schedule a task.
     * @return
     *            Return true iff a task was successfully scheduled.
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InterruptedException
     * @throws IOException
     */
    public boolean schedule()
            throws IllegalAccessException, InstantiationException,
            InterruptedException, IOException, ServiceException;
}