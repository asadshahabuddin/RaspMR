package com.rasp.shuffle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.mapred.JobQueueInfo;

import com.rasp.config.MasterConfiguration;
import com.rasp.mr.Job;
import com.rasp.mr.MapperTask;
import com.rasp.mr.ShuffleTask;
import com.rasp.mr.slave.ShuffleTaskImpl;
import com.rasp.utils.autodiscovery.Service;

/**
 * Author : Sourabh Suman, Shivastuti Koul
 * File   :
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/7/15
 * Edited : 4/11/15
 */
public class ShuffleMasterImpl implements ShuffleMaster {

    MasterConfiguration config;
    HashMap<Service, Map<String, Long>> serviceKeyFreq;
    Job job;
    Map<String, ShuffleTask> taskMap;

    // Threshold for number of reducers/keys to be used on one machine
    //static final int THRESHOLD = 10;    

    public ShuffleMasterImpl(MasterConfiguration conf){
        config = conf;
        taskMap = new HashMap<String, ShuffleTask>();
    }

    public void run(Job job) throws IOException, InterruptedException{
    	this.job = job;
    	serviceKeyFreq = getMachineKeyFreq();
    	HashMap<String, Service> keyWithService = getServicesWithMaxKeyFreq(serviceKeyFreq);
    	triggerMapDataTransferForAll(keyWithService, getAllServices());
        job.setReduceKeyServiceMap(createReduceKeyServiceMap());
    }    
    
    /**
     * get all machines associated with this job
     * @param job
     * @return 
     * @throws InterruptedException 
     * @throws IOException 
     */
    List<Service> getAllServices() throws IOException, InterruptedException{
    	List<Service> services = new ArrayList<Service>();
    	for (MapperTask task : job.getMapTasks()){
    		services.add(task.getService());
    	}
    	return services;
    }
    
    /**
     * given a job, get the machines associated with it, along with the keys 
     * and frequencies of each key on that machine 
     * @throws InterruptedException 
     * @throws IOException 
     */
    HashMap<Service, Map<String, Long>> getMachineKeyFreq() throws IOException, InterruptedException{
    	HashMap<Service, Map<String, Long>> map = new HashMap<Service, Map<String, Long>>();    	
    	
    	// for each task, get the machines and key, frequencies and save to var data
    	for (MapperTask task : job.getMapTasks()){
    		Service service = task.getService();
    		Map<String, Long> keyCountMap = task.getMapContext().getKeyCountMap();
    		map.put(service, keyCountMap);
    	}
    	return map;
    }
    
    /**
     * Method to retrieve Key, ServiceName pairs for machines with max frequency of each key
      */
    HashMap<String, Service> getServicesWithMaxKeyFreq(HashMap<Service, Map<String, Long>> data){
        HashMap<String, IntermediateServiceWithFrequency> interimOutput = new HashMap<String, IntermediateServiceWithFrequency>();

        for (Service service : data.keySet()){
            Map<String, Long> keyWithFreq = data.get(service);

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
    	//create task for each
    	if (keyMachine!=sourceMachine){ 
        	ShuffleTask task = createShuffleTask(key, keyMachine, sourceMachine);
        	taskMap.put(task.getTaskId(), task);
    	}
    }
    
    /**
     * create new shuffle task for given data
     * @param key
     * @param keyMachine
     * @param sourceMachine
     * @return
     */
    ShuffleTask createShuffleTask(String key, Service keyMachine, Service sourceMachine){
    	ShuffleTask task = new ShuffleTaskImpl();
    	task.setJob(job);
    	task.setKey(key);
    	task.setService(sourceMachine);
    	task.setDataTargetService(keyMachine);
    	return task;
    }

    @Override
    public synchronized void shuffleDataTransferCompleted(String taskId) {
    	ShuffleTask task = taskMap.get(taskId);
    	task.complete();
    	if (job.isShuffleComplete()){
    		config.getJobTracker().submit(job);
    	}
    }

    @Override
    public void cleanup(Job job) {
        taskMap.clear();
        serviceKeyFreq.clear();
        taskMap = null;
        serviceKeyFreq = null;
    }

    private Map<String,Service> createReduceKeyServiceMap(){
        return null;
    }
}
