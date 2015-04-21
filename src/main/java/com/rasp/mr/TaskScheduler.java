/**
 * Author : Shivastuti Koul
 * File   : TaskScheduler.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr;

import java.io.IOException;
import com.google.protobuf.ServiceException;

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