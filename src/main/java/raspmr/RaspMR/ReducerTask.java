/**
 * Author : Rahul Madhavan
 * File   : ReducerTask.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package raspmr.RaspMR;

/**
 * ReducerTask represents a {@link Reducer} running for a single input split
 *
 */
public interface ReducerTask extends Task
{
    /**
     * sets the ReducerClass for the given Task
     * the {@link Task#execute()} should invoke the map of the given reducerClass Class
     *
     * @param reducerClass
     */
    void setReducerClass(Class<? extends Reducer<?, ?, ?, ?>> reducerClass);

    /**
     * returns the {@link Reducer} for the Task
     *
     * @return {@link Class<? extends Reducer>}
     */
    Class<? extends Reducer<?, ?, ?, ?>> getReducerClass();
}