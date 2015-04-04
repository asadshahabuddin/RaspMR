package com.rasp.interfaces;

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
    boolean scheduleTask();

    /**
     * executes the task
     *
     * @param {@link Task}
     */
}