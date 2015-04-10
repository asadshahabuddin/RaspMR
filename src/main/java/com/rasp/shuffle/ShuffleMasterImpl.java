package com.rasp.shuffle;

import com.rasp.config.MasterConfiguration;
import com.rasp.utils.autodiscovery.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sourabh,Shiva on 4/7/2015.
 */
public class ShuffleMasterImpl implements ShuffleMaster {

    MasterConfiguration config;

    // Threshold for number of reducers/keys to be used on one machine
    //static final int THRESHOLD = 10;

    // counter to hold total number of machines

    public ShuffleMasterImpl(MasterConfiguration conf){
        config = conf;
    }

    List<Service> getAllSlaves(){
        List<Service> slaves = new ArrayList<Service>();
        return slaves;
    }
//create a job 
    public void run(HashMap<Service, HashMap<String, Long>> data){
        HashMap<String, Service> keyWithService = getServicesWithMaxKeyFreq(data);
        triggerMapDataTransferForAll(keyWithService, getAllSlaves());
    }

    /**
     * Method to retrieve Key, ServiceName pairs for machines with max frequency of each key
      */
    HashMap<String, Service> getServicesWithMaxKeyFreq(HashMap<Service, HashMap<String, Long>> data){
        HashMap<String, IntermediateServiceWithFrequency> interimOutput = new HashMap<String, IntermediateServiceWithFrequency>();

        for (Service service : data.keySet()){
            HashMap<String, Long> keyWithFreq = data.get(service);

            for (String key : keyWithFreq.keySet()){
                if (interimOutput.containsKey(key)){
                    if (keyWithFreq.get(key).compareTo(interimOutput.get(key).getFrequency()) == 1){
                        interimOutput.get(key).setFrequency(keyWithFreq.get(key));
                    }
                }
                else{
                    interimOutput.put(key,
                            new IntermediateServiceWithFrequency(service,
                                    keyWithFreq.get(key)));
                }
            }
        }

        HashMap<String, Service> output = new HashMap<String, Service>();
        for (String key : interimOutput.keySet()){
            output.put(key, interimOutput.get(key).getService());
        }

        return output;
    }

    /**
     * direct slaves to transfer map output data for each key to the slave with max frequency of that key
     *
     */
    void triggerMapDataTransferForAll(HashMap<String, Service> keyService, List<Service> machines){
        // send msg to each machine to transfer map data for each key to the machine which has the max freq for that key

        for (String key : keyService.keySet()){
            triggerMapDataTransferForKey(key, keyService.get(key), machines);
        }

    }

    // direct all slaves to transfer map output data for given one key to the given slave with max frequency of that key
    void triggerMapDataTransferForKey(String key, Service keyMachine, List<Service> allMachines){
        for (Service machine : allMachines){
            triggerMapDataTransferForKeyOnMachine(key, keyMachine, machine);
        }
    }

    // direct one slave to send map output data for given key to given target machine with max frequency
    void triggerMapDataTransferForKeyOnMachine(String key, Service keyMachine, Service sourceMachine){

    }

    //
    void checkAllAcksReceivedAndTriggerReduce(){
    	//  create a lookup map to map for a machine(for a set of keys
        // lying on that machine)

    }

    @Override
    public void shuffleDataTransferCompleted(String taskId) {

    }
}
