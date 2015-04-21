/**
 * Author : Shivastuti Koul
 * File   : TaskScheduler.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 5, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import com.rasp.mr.Task;
import org.slf4j.Logger;
import java.io.IOException;
import com.rasp.mr.MapperTask;
import com.rasp.mr.ShuffleTask;
import com.rasp.mr.TaskTracker;
import com.rasp.mr.ReducerTask;
import org.slf4j.LoggerFactory;
import com.google.protobuf.ServiceException;
import com.rasp.config.SlaveConfiguration;

public class TaskScheduler implements com.rasp.mr.TaskScheduler, Runnable {
    static final Logger LOG = LoggerFactory.getLogger(TaskScheduler.class);

    private TaskTracker taskTracker;
    private SlaveConfiguration configuration;

    /**
     * Constructor 1
     *
     * @param taskTracker   The task tracker.
     * @param configuration The configuration object.
     */
    public TaskScheduler(TaskTracker taskTracker, SlaveConfiguration configuration) {
        this.taskTracker = taskTracker;
        this.configuration = configuration;
    }

    @Override
    public boolean schedule()
            throws IllegalAccessException, InstantiationException,
            InterruptedException, IOException, ServiceException {

        Task task = taskTracker.nextTask();
        if (task == null) {
            return false;
        }
        if (task instanceof MapperTask) {
            task.execute();
            try {
                configuration.getJobNode().
                        mapCompleted(task.getTaskId(),
                                ((MapperTask) task).getMapContext().getKeyCountMap());
            } catch (ServiceException e) {
                LOG.error("", e);
            }
        } else if (task instanceof ShuffleTask) {
            task.execute();
            try {
                configuration.getJobNode().
                        shuffleDataTransferCompleted(task.getTaskId());

            } catch (ServiceException e) {
                LOG.error("", e);
            }
        } else if (task instanceof ReducerTask) {
            task.execute();
            try {
                configuration.getJobNode().
                        reduceCompleted(task.getTaskId());

            } catch (ServiceException e) {
                LOG.error("", e);
            }
        } else {
            throw new RuntimeException("unknown task type given to task scheduler: " + task.getClass());
        }

        return true;
    }

    @Override
    public void run() {
        boolean status = false;

        try {
            while (true) {
                if (status != schedule()) {
                    status = !status;
                    if (status) {
                        LOG.info("   [echo] Task scheduler has resumed scheduling");
                    } else {
                        LOG.info("   [echo] There are no tasks to schedule");
                    }
                }
            }
        } catch (IOException ioe) {
            LOG.error("", ioe);
        } catch (InterruptedException intre) {
            LOG.error("", intre);
        } catch (InstantiationException inste) {
            LOG.error("", inste);
        } catch (IllegalAccessException iae) {
            LOG.error("", iae);
        } catch (ServiceException e) {
            LOG.error("", e);
        }
    }
}