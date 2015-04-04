package com.rasp.config;

import com.rasp.fs.DataNode;
import com.rasp.fs.slave.DataNodeServerImpl;
import raspmr.RaspMR.utils.autodiscovery.Service;
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

    public SlaveConfiguration(int portNo, ServiceType serviceType){
        super(portNo,serviceType);
        dataNode = new DataNodeServerImpl(getService());
    }

    public DataNode getDataNode(){
        return dataNode;
    }


}
