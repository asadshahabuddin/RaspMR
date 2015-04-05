/**
 * Author : Rahul Madhavan
 * File   : Mapper.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.interfaces;

/* Import list */
import java.util.Map;
import java.util.List;

/**
 * Mapper is a simplified version of Hadoop Map Reduce Mapper
 *
 *
 * @param <KEY_OUT> Type of the key that will be emitted by map function
 * @param <VALUE_OUT> Type of the value that will be emitted by map function
 */
public interface Mapper<KEY_OUT, VALUE_OUT>
{
    /**
     * this function is called once at the beginning of every map task {@link MapperTask}
     */
    void setup();

    /**
     * a simplified version of the Hadoop Map Reduce Mapper#map function
     * this method will be invoked for each line of the input
     *
     * @param key a long number which is unique for every line of input, assigned by the RaspMR engine
     * @param value the input line in String format (the line separator is a newline character)
     * @return  A <code>Map<KEY_OUT,List<VALUE_OUT>></code> the user is responsible for defining this map
     *          and filling the appropriate keys and values
     */
    Map<KEY_OUT,List<VALUE_OUT>> map(Long key,String value);

    /**
     * this function is called once at the the end of every map task {@link MapperTask}
     */
    void cleanup();
}