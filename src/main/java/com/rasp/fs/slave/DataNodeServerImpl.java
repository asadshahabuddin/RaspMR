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

import com.rasp.config.Configuration;
import com.rasp.config.SlaveConfiguration;
import com.rasp.fs.DataNode;
import com.rasp.fs.InputSplit;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

public class DataNodeServerImpl
    implements DataNode
{
    private Service service;
    private FileOutputStream f;
    private SlaveConfiguration configuration;


    public DataNodeServerImpl(SlaveConfiguration configuration) throws FileNotFoundException{
        this.service  = ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                Configuration.DATA_NODE_PORT);
        f = createDataStream();
        this.configuration = configuration;
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
    	File f = new File(SlaveConfiguration.INPUT_SPLIT_FILENAME);
    	if(f.exists())
    	{
    		f.delete();
    	}
    	return new FileOutputStream(SlaveConfiguration.INPUT_SPLIT_FILENAME, true);
    }

    @Override
    public void closeInputSplit() throws IOException{
        close();
    }

    @Override
    public Service getService()
    {
        return service;
    }

    @Override
    public void storeInputSplit(InputSplit inputSplit)
        throws IOException
    {
        configuration.addInputSplit(inputSplit);
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