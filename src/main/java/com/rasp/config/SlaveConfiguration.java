/**
 * Author : Rahul Madhavan
 * File   : SlaveConfiguration.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.config;

/* Import list */
import com.rasp.fs.DataNode;
import java.io.FileNotFoundException;
import com.rasp.fs.slave.DataNodeServerImpl;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

public class SlaveConfiguration extends Configuration
{
	private DataNode dataNode;

    public SlaveConfiguration(int portNo, ServiceType serviceType)
        throws FileNotFoundException
    {
        super(portNo,serviceType);
        dataNode = new DataNodeServerImpl("split.txt", getService());
    }

    public DataNode getDataNode()
    {
        return dataNode;
    }
}
/* End of SlaveConfiguration.java */