/**
 * Author : Asad Shahabuddin
 * File   : Terminal.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited : Apr 2, 2015
 */

package com.rasp.fs;

/* Import list */
import java.io.IOException;
import java.io.FileInputStream;

public class Terminal
{
    public static int idx = 0;
    
    public InputFormat initTransmit(String inputFile,
                                    int workerCount)
        throws InterruptedException, IOException
    {
        return new InputFormat(inputFile, workerCount); 
    }
    
    /**
     * Transmit a split to the target location.
     * @param format
     *            The input format.
     * @param location
     *            Location to write the split at.z
     * @return
     *            Returns true iff the current split is written
     *            successfully at the specified location.
     */
    public boolean trasmit(InputFormat format, String location)
    {
        if(format == null)
        {
            return false;
        }
        format.split(idx++, location);
        return true;
    }
    
    /**
     * Receive a split and write it to the specified location.
     * @param f
     *            Stream of bytes.
     *            
     */
    public void receive(FileInputStream f)
    {
        // TODO
    }
}
/* End of Terminal.java */