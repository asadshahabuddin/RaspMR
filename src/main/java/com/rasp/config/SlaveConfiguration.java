package com.rasp.config;

import com.rasp.fs.DataNode;
import java.io.FileNotFoundException;
import com.rasp.fs.slave.DataNodeServerImpl;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/3/15
 * Edited :
 */
public class SlaveConfiguration extends Configuration {

    private DataNode dataNode;

    public SlaveConfiguration(int portNo, ServiceType serviceType)
        throws FileNotFoundException
    {
        super(portNo,serviceType);
        dataNode = new DataNodeServerImpl("split.txt", getService());
    }

    public DataNode getDataNode(){
        return dataNode;
    }
}
