package com.rasp.fs.slave;

import com.rasp.fs.DataNode;
import com.rasp.fs.InputSplit;
import raspmr.RaspMR.utils.autodiscovery.Service;

import java.io.UnsupportedEncodingException;

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
        System.out.println("received input split " + inputSplit.getIdx());
    }

    @Override
    public void storeChunk(byte[] b) {
        try {
            System.out.println("received chunk "  + new String(b, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Service getService() {
        return service;
    }
}
