/**
 * Author : Asad Shahabuddin
 * File   : DataNodeServerImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.fs.slave;

import java.io.IOException;
import com.rasp.fs.DataNode;
import java.io.FileOutputStream;
import com.rasp.fs.InputSplitImpl;
import com.rasp.config.Configuration;
import java.io.FileNotFoundException;
import com.rasp.utils.file.FSHelpers;
import com.rasp.config.SlaveConfiguration;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class DataNodeServerImpl
    implements DataNode {
    private Service service;
    private FileOutputStream f;
    private SlaveConfiguration configuration;

    /**
     * Constructor.
     *
     * @param configuration The configuration object.
     * @throws FileNotFoundException
     */
    public DataNodeServerImpl(SlaveConfiguration configuration) throws FileNotFoundException {
        this.service = ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                Configuration.DATA_NODE_PORT);
        this.configuration = configuration;
    }

    @Override
    public void closeInputSplit() throws IOException {
        close();
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public void storeInputSplit(InputSplitImpl inputSplit)
            throws IOException, InterruptedException {
        f = FSHelpers.deleteAndCreateFile(inputSplit.getLocation(), true);
        configuration.addInputSplit(inputSplit);
    }

    @Override
    public void storeChunk(byte[] b)
            throws IOException {
        f.write(b);
    }

    /**
     * Close open handles/streams.
     *
     * @throws IOException
     */
    public void close()
            throws IOException {
        if (f != null) {
            f.close();
        }
    }
}