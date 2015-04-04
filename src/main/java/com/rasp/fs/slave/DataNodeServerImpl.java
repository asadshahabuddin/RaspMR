/**
 * Author : Rahul Madhavan / Asad Shahabuddin
 * File   : DataNodeServerImpl.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.fs.slave;

/* Import list */
import java.io.File;
import java.io.IOException;
import com.rasp.fs.DataNode;
import com.rasp.fs.InputSplit;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import raspmr.RaspMR.utils.autodiscovery.Service;

public class DataNodeServerImpl
    implements DataNode
{
	private String dataFile;
    private Service service;
    private InputSplit inputSplit;
    private FileOutputStream f;
    
    /**
     * 
     * @param metaFile
     *            File where meta data about the split is stored.
     * @param dataFile
     *            File where the split data is stored.
     * @param service
     *            Service object.
     */
    public DataNodeServerImpl(String dataFile, Service service)
        throws FileNotFoundException
    {
    	this.dataFile = dataFile;
        this.service  = service;
        inputSplit    = null;
        f = createDataStream();
    }
    
    /**
     * Create a file output stream
     * @return
     *            The file output stream with append enabled.
     * @throws FileNotFoundException
     */
    public FileOutputStream createDataStream()
        throws FileNotFoundException
    {
    	File f = new File(dataFile);
    	if(f.exists())
    	{
    		f.delete();
    	}
    	return new FileOutputStream(dataFile, true);
    }
    
    @Override

    public Service getService()
    {
        return service;
    }

    @Override
    public void storeInputSplit(InputSplit inputSplit)
        throws FileNotFoundException, IOException
    {
    	this.inputSplit = inputSplit;
    }
    
    public InputSplit getInputSplit()
    {
    	return inputSplit;
    }

    @Override
    public void storeChunk(byte[] b)
        throws IOException
    {
    	f.write(b);
    }
    
    public void close()
        throws IOException
    {
    	if(f != null)
    	{
    		f.close();
    	}
    }
}
/* End of DataNodeServerImpl.java */