package com.rasp.fs;

import raspmr.RaspMR.utils.autodiscovery.Service;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public interface DataNode {

    void storeInputSplit(InputSplit inputSplit) throws InterruptedException,IOException;

    void storeChunk(byte[] b)throws InterruptedException,IOException;

    Service getService();

}
