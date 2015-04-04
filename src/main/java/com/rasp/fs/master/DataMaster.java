/**
 * Author : Asad Shahabuddin
 * File   : DataMaster.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited : Apr 2, 2015
 */

package com.rasp.fs.master;

/* Import list */
import com.rasp.config.Configuration;
import com.rasp.config.MasterConfiguration;
import com.rasp.fs.DataNode;
import com.rasp.fs.InputFormat;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

import java.io.IOException;

import java.util.List;

public class DataMaster{
    public static int idx = 0;
    MasterConfiguration configuration;




    public DataMaster(MasterConfiguration configuration){
        this.configuration = configuration;
    }


    private InputFormat createInputFormat(String inputFile,int workerCount)throws InterruptedException, IOException{

        return new InputFormat(inputFile, workerCount); 
    }
    
    /**
     * Transmit a split to the target location.
     * @param format
     *            The input format.
     * @param dataNode
     *            Location to write the split at.z
     * @return
     *            Returns true iff the current split is written
     *            successfully at the specified location.
     */
    private void transmit(InputFormat format, DataNode dataNode) throws IOException,InterruptedException{
        format.split(idx++, dataNode);
    }

    public void splitAndSend(String inputFile) throws IOException, InterruptedException {

        List<Service> services = configuration.getDiscoverer().getServices(ServiceType.TASK_TRACKER);
        InputFormat inputFormat = createInputFormat(inputFile,services.size());

        for(Service service: services){
            transmit(inputFormat,configuration.getDataNode(service));
        }

    }
    

}
