/**
 * Author : Asad Shahabuddin
 * File   : RecordReader.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 24, 2015.
 * Edited : Mar 25, 2015.
 */

package com.rasp.fs;

/* Import list */
import java.io.Closeable;
import java.io.IOException;

public abstract class RecordReader
    implements Closeable {
    /**
     * Called once at initialization.
     * @param inputSplit
     *            The split that defines the range of records to read.
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract void initialize(InputSplit inputSplit)
        throws IOException, InterruptedException;
  
    /**
     * Read the next key, value pair.
     * 
     * @return
     *            true if a key/value pair was read.
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract boolean nextKeyValue()
        throws IOException, InterruptedException;
  
    /**
     * Get the current key.
     * @return
     *            Rhe current key or null if there is no key.
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract Long getCurrentKey()
        throws IOException, InterruptedException;
  
    /**
     * Get the current value.
     * @return
     *            The line that was read.
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract String getCurrentValue()
        throws IOException, InterruptedException;
  
    /**
     * Close the record reader.
     */
    public abstract void close()
        throws IOException;
}