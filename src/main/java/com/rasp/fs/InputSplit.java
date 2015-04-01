/**
 * Author : Asad Shahabuddin
 * File   : InputSplit.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 30, 2015
 * Edited : Mar 30, 2015
 */

package com.rasp.fs;

/* Import list */
import java.io.IOException;

public class InputSplit
    implements com.rasp.interfaces.InputSplit
{
    private long offset;
    private long length;
    private String location;
    
    /**
     * Constructor
     * @param offset
     *            Starting offset.
     * @param length
     *            Block size, i.e, length.
     * @param location
     *            Node name where the split would be local. 
     */
    public InputSplit(long offset, long length, String location)
    {
        this.offset   = offset;
        this.length   = length;
        this.location = location;
    }
    
    /**
     * Set the offset.
     * @param offset
     *            Starting offset of the split.
     */
    public void setOffset(long offset)
    {
        this.offset = offset;
    }
    
    /**
     * Get the offset.
     */
    public long getOffset()
        throws IOException, InterruptedException
    {
        return offset;
    }
    
    /**
     * Set the length
     * @param length
     *            Block size of the split.
     */
    public void setLength(long length)
    {
        this.length = length;
    }
    
    /**
     * Get the block size.
     */
    public long getLength()
        throws IOException, InterruptedException
    {
        return length;
    }

    /**
     * Set the location.
     * @param location
     *            Location of the split.
     */
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    /**
     * Get the location.
     */
    public String getLocation()
        throws IOException, InterruptedException
    {
        return location;
    }
}
/* End of InputSplit.java */