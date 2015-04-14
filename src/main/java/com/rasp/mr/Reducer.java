/**
 * Author : Rahul Madhavan
 * File   : Reducer.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.mr;

/* Import list */
import java.util.Map;
import java.util.List;
import com.rasp.fs.Iterable;

/**
 * Reducer is a simplified version of Hadoop Map Reduce Reducer
 *
 *
 */
public interface Reducer
{
    /**
     * this function is called once at the beginning of every reduce task {@link com.rasp.mr.ReducerTask}
     */
    void setup();

    /**
     * a simplified version of the Hadoop Map Reduce Reducer#reduce function
     * this method will be invoked once for each key output by the mapper
     *
     * @param key of KEY_IN type
     * @param values a <code>List<VALUE_IN></code>
     * @return  A <code>Map<KEY_OUT,List<VALUE_OUT>></code> the user is responsible for defining this map
     *          and filling the appropriate keys and values
     */
    void reduce(Writable key, Iterable values, ReduceContext context);

    /**
     * this function is called once at the end of every reduce task {@link com.rasp.mr.ReducerTask}
     */
    void cleanup();
}