/**
 * Author : Asad Shahabuddin
 * File   : DataMasterImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited : Apr 2, 2015
 */

package com.rasp.fs.master;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import com.rasp.fs.DataNode;
import com.rasp.fs.DataMaster;
import com.rasp.fs.InputFormatImpl;
import com.rasp.config.Configuration;
import com.rasp.config.MasterConfiguration;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class DataMasterImpl implements DataMaster {
    MasterConfiguration configuration;
    Map<String, InputFormatMetaData> inputFileMap;

    /**
     * Constructor.
     *
     * @param configuration The configuration object.
     */
    public DataMasterImpl(MasterConfiguration configuration) {
        this.configuration = configuration;
        this.inputFileMap = new HashMap<>();
    }

    @Override
    public InputFormatImpl createInputFormat(String inputFile, int workerCount)
            throws InterruptedException, IOException {
        return new InputFormatImpl(inputFile, workerCount);
    }

    /**
     * Transmit a split to the target location.
     *
     * @param formatMetaData The input format.
     * @param dataNode       Location to write the split at.z
     * @return Returns true iff the current split is written
     * successfully at the specified location.
     */
    private void transmit(InputFormatMetaData formatMetaData, DataNode dataNode)
            throws IOException, InterruptedException {
        formatMetaData.getInputFormat().split(formatMetaData.nextIdx(), dataNode);
    }

    @Override
    public void splitAndSend(String inputFile)
            throws IOException, InterruptedException {
        List<Service> services = configuration.getDiscoverer().getServices(ServiceType.TASK_TRACKER);
        if (!inputFileMap.containsKey(inputFile)) {
            InputFormatImpl inputFormat = createInputFormat(inputFile, services.size());
            inputFileMap.put(inputFile, new InputFormatMetaData(inputFormat));
        }

        for (Service service : services) {
            Service dService = ServiceFactory.createService(
                    service.getServiceType(),
                    service.getIp(),
                    Configuration.DATA_NODE_PORT);
            transmit(inputFileMap.get(inputFile), configuration.getDataNode(dService));
        }
    }

    @Override
    public InputFormatImpl getInputFormat(String file) {
        return inputFileMap.get(file).getInputFormat();
    }

    /**
     * Class representing input format meta data.
     */
    private class InputFormatMetaData {
        private int idx;
        private InputFormatImpl inputFormat;

        /**
         * Constructor.
         *
         * @param inputFormat
         */
        private InputFormatMetaData(InputFormatImpl inputFormat) {
            this.inputFormat = inputFormat;
            idx = 0;
        }

        /**
         * Get the next index.
         *
         * @return The next index.
         */
        private int nextIdx() {
            int nextIdx = idx;
            if (nextIdx == inputFormat.getWorkerCount() - 1) {
                idx = 0;
            } else {
                idx++;
            }
            return nextIdx;
        }

        /**
         * Set the input format.
         *
         * @param inputFormat The input format object.
         */
        public void setInputFormat(InputFormatImpl inputFormat) {
            this.inputFormat = inputFormat;
        }

        /**
         * Get the input format.
         *
         * @return The input format object.
         */
        public InputFormatImpl getInputFormat() {
            return inputFormat;
        }
    }
}