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
import com.rasp.fs.DataMaster;
import com.rasp.fs.DataNode;
import com.rasp.fs.InputFormat;
import com.rasp.interfaces.Partitioner;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMasterImpl implements DataMaster{

    MasterConfiguration configuration;
    Map<String,InputFormatMetaData> inputFileMap;

    public DataMasterImpl(MasterConfiguration configuration){
        this.configuration = configuration;
        this.inputFileMap = new HashMap<>();
    }


    @Override
    public InputFormat createInputFormat(String inputFile,int workerCount)throws InterruptedException, IOException{
        return new InputFormat(inputFile, workerCount);
    }
    
    /**
     * Transmit a split to the target location.
     * @param formatMetaData
     *            The input format.
     * @param dataNode
     *            Location to write the split at.z
     * @return
     *            Returns true iff the current split is written
     *            successfully at the specified location.
     */
    private void transmit(InputFormatMetaData formatMetaData, DataNode dataNode) throws IOException,InterruptedException{
        formatMetaData.getInputFormat().split(formatMetaData.nextIdx(), dataNode);
    }

    @Override
    public void splitAndSend(String inputFile) throws IOException, InterruptedException {

        List<Service> services = configuration.getDiscoverer().getServices(ServiceType.TASK_TRACKER);
        InputFormat inputFormat = createInputFormat(inputFile,services.size());
        if(!inputFileMap.containsKey(inputFile)){
            inputFileMap.put(inputFile,new InputFormatMetaData(inputFormat));
        }
        for(Service service: services){
            Service dService = ServiceFactory.createService(
                    service.getServiceType(),
                    service.getIp(),
                    Configuration.DATA_NODE_PORT);
            transmit(inputFileMap.get(inputFile),configuration.getDataNode(dService));
        }

    }

    @Override
    public InputFormat getInputFormat(String file){
        return inputFileMap.get(file).getInputFormat();
    }


    private class InputFormatMetaData{

        private int idx;
        private InputFormat inputFormat;

        private InputFormatMetaData(InputFormat inputFormat){
            this.inputFormat = inputFormat;
            idx = 0;
        }

        private int nextIdx(){
            int nextIdx = idx;
            if(nextIdx == inputFormat.getWorkerCount() - 1){
                idx = 0;
            }else{
                idx++;
            }
            return nextIdx;
        }

        public InputFormat getInputFormat() {
            return inputFormat;
        }

        public void setInputFormat(InputFormat inputFormat) {
            this.inputFormat = inputFormat;
        }
    }

}
