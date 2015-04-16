/**
 * Author : Asad Shahabuddin
 * File   : InputSplitImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 30, 2015
 * Edited : Mar 30, 2015
 */

package com.rasp.fs;

/* Import list */
import java.io.IOException;

public class InputSplitImpl
    implements InputSplit {
    private String inputFormatId;
    private int idx;
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
    public InputSplitImpl(int idx, long offset, long length, String location, String inputFormatId) {
        this.inputFormatId = inputFormatId;
        this.idx      = idx;
        this.offset   = offset;
        this.length   = length;
        this.location = location;
    }
    
    /**
     * Set the index.
     * @param idx
     *            Index of the split.
     */
    public void setIdx(int idx) {
        this.idx = idx;
    }
    
    /**
     * Get the index.
     * @return
     *            Index of the split.
     */
    public int getIdx() {
        return idx;
    }
    
    /**
     * Set the offset.
     * @param offset
     *            Starting offset of the split.
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }
    
    /**
     * Get the offset.
     */
    public long getOffset()
        throws IOException, InterruptedException {
        return offset;
    }
    
    /**
     * Set the length
     * @param length
     *            Block size of the split.
     */
    public void setLength(long length) {
        this.length = length;
    }
    
    /**
     * Get the block size.
     */
    public long getLength()
        throws IOException, InterruptedException {
        return length;
    }

    /**
     * Set the location.
     * @param location
     *            Location of the split.
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Get the location.
     */
    public String getLocation()
        throws IOException, InterruptedException {
        return location;
    }

    public String getInputFormatId() {
        return inputFormatId;
    }
}
/* End of InputSplitImpl.java */