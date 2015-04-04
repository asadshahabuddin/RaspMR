package com.rasp.fs.slave;

import com.rasp.fs.DataNode;
import com.rasp.fs.InputSplit;
import raspmr.RaspMR.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/3/15
 * Edited :
 */
public class DataNodeServerImpl implements DataNode {

    private Service service;

    public DataNodeServerImpl(Service service){
        this.service = service;
    }

    @Override
    public void storeInputSplit(InputSplit inputSplit) {

    }

    @Override
    public void storeChunk(byte[] b) {

    }

    @Override
    public Service getService() {
        return service;
    }
}
