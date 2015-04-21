/**
 * Author : Shivastuti Koul
 * File   : ReducerTask.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.mr;

/**
 * ReducerTask represents a {@link Reducer} running for a single input split.
 */
public interface ReducerTask extends Task {
    /**
     * sets the ReducerClass for the given Task
     * The {@link Task#execute()} should invoke the map of the given reducer class.
     *
     * @param reducerClass The reducer class.
     */
    void setReducerClass(Class<? extends Reducer> reducerClass);

    /**
     * Returns the {@link Reducer} for the task.
     *
     * @return {@link Class<? extends Reducer>}
     */
    Class<? extends Reducer> getReducerClass();

    /**
     * Set the key.
     *
     * @param key The key.
     */
    void setKey(String key);

    /**
     * Get the key.
     *
     * @return The key.
     */
    String getKey();
}