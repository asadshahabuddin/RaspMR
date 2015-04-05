/**
 * Author : Asad Shahabuddin
 * File   : RecordReader.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 24, 2015.
 * Edited : Mar 25, 2015.
 */

package com.rasp.interfaces;

/* Import list */
import java.io.Closeable;
import java.io.IOException;

public abstract class RecordReader
    implements Closeable
{ 
    /**
     * Called once at initialization.
     * @param inputSplit
     *            the split that defines the range of records to read
     * @param context
     *            information about the task
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract void initialize(InputSplit inputSplit)
        throws IOException, InterruptedException;
  
    /**
     * Read the next key, value pair
     * 
     * @return
     *            true if a key/value pair was read
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract boolean nextKeyValue()
        throws IOException, InterruptedException;
  
    /**
     * Get the current key
     * @return
     *            the current key or null if there is no key
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract Long getCurrentKey()
        throws IOException, InterruptedException;
  
    /**
     * Get the current value
     * @return
     *            the line that was read
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract String getCurrentValue()
        throws IOException, InterruptedException;
  
    /**
     * The current progress of the record reader through its data.
     * @return
     *            a number between 0.0 and 1.0 that is the fraction of 
     *            the data read
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract float getProgress()
        throws IOException, InterruptedException;
  
    /**
     * Close the record reader
     */
    public abstract void close()
        throws IOException;
}
/* End of RecordReader.java */
