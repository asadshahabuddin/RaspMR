/**
 * Author : Rahul Madhavan
 * File   : Mapper.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.mr;

/* Import list */

/**
 * Mapper is a simplified version of Hadoop Map Reduce Mapper
 *
 *
 *
 *
 */
public interface Mapper
{
    /**
     * this function is called once at the beginning of every map task {@link com.rasp.mr.MapperTask}
     */
    void setup();

    /**
     * a simplified version of the Hadoop Map Reduce Mapper#map function
     * this method will be invoked for each line of the input
     *
     * @param key a long number which is unique for every line of input, assigned by the RaspMR engine
     * @param value the input line in String format (the line separator is a newline character)
     * @return
     */
    void map(Long key,String value, MapContext mapContext);

    /**
     * this function is called once at the the end of every map task {@link com.rasp.mr.MapperTask}
     */
    void cleanup();
}