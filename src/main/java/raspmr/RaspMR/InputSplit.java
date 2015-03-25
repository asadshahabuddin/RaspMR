/**
 * Author : Asad Shahabuddin
 * File   : InputSplit.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 24, 2015
 * Edited : Mar 25, 2015
 */

package raspmr.RaspMR;

import java.io.IOException;

/**
 * <code>InputSplit</code> represents the data to be processed by an individual 
 * Mapper.
 * 
 * <p>
 * Typically, it presents a byte-oriented view on the input and is the 
 * responsibility of RecordReader of the job to process this and present a 
 * record-oriented view.
 * </p>
 */
public interface InputSplit
{
    /**
     * Get the size of the split, so that the input splits can be sorted by size.
     * @return
     *            the number of bytes in the split
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract long getLength()
        throws IOException, InterruptedException;
    
    /**
     * Get the list of nodes by name where the data for the split would be local. 
     * The locations do not need to be serialized.
     * @return
     *            a new array of the node nodes
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract String[] getLocations()
        throws IOException, InterruptedException;
}
