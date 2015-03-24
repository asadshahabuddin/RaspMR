/**
 * Author : Rahul Madhavan
 * File   : TaskTracker.java
 * Email  : 
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package raspmr.RaspMR;

/**
 * <code>TaskTracker</code> is responsible for
 * 1) maintaining a queue of tasks.
 * 2) tracking progress of tasks.
 * 3) Reporting progress of task to {@link JobTracker}
 * 4) executing tasks in an ordered manner
 *
 */
public interface TaskTracker
{
    /**
     * Submits task for execution
     *
     * @param {@link Task}
     */
    void submit(Task task);

    /**
     * returns the next task to be executed
     *
     * @return {@link Task}
     */
    Task nextTask();

    /**
     *
     * @return true if the <code>TaskTracker</code> has started successfully
     */
    boolean start();

    /**
     *
     * @return true if the <code>TaskTracker</code> has stopped successfully
     */
    boolean stop();
}