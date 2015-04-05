package com.rasp.interfaces;

import java.io.IOException;

/**
 * Created by Pulkit on 4/4/15.
 */
public interface TaskScheduler
{
    /**
     * returns task for execution
     *
     * @param {@link Task}
     */
    boolean scheduleTask()
		throws IllegalAccessException, InstantiationException,
			   InterruptedException,   IOException;
}