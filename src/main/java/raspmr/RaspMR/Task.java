/**
 * Author : Rahul Madhavan
 * File   : Task.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package raspmr.RaspMR;

/**
 * Task represents a {@link Mapper} or {@link Reducer}
 * running for a single input split
 *
 */
public interface Task
{

    /**
     * returns the Job to which the task belongs
     *
     * @return {@link Job}
     */
    Job getJob();

    /**
     * returns the {@link InputSplit}which represents the input for the task
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
     * executes the task with the {@link InputSplit} given from {@link Task#getTaskInputSplit}
     *
     * @return true if the task runs appropriately
     */
    boolean execute();
}