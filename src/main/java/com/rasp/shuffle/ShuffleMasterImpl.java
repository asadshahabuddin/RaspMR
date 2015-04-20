package com.rasp.shuffle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rasp.config.MasterConfiguration;
import com.rasp.mr.Job;
import com.rasp.mr.MapperTask;
import com.rasp.mr.ShuffleTask;
import com.rasp.mr.slave.ShuffleTaskImpl;
import com.rasp.utils.autodiscovery.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author : Sourabh Suman, Shivastuti Koul, Rahul Madhavan
 * File   : 
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/7/15
 * Edited : 4/11/15
 */
public class ShuffleMasterImpl implements ShuffleMaster {

    static final Logger LOG = LoggerFactory.getLogger(ShuffleMasterImpl.class);

    private MasterConfiguration config;
    private Map<String, Map<Service, Map<String, Long>>> jobServiceKeyFrequency;
    private Map<String, ShuffleTask> taskMap;

    // Threshold for number of reducers/keys to be used on one machine
    //static final int THRESHOLD = 10;    

    public ShuffleMasterImpl(MasterConfiguration conf){
        config = conf;
        taskMap = new HashMap<>();
        jobServiceKeyFrequency = new HashMap<>();
    }
    
    /** 
     * Creates shuffle tasks for all keys/machines
     * @param job which calls this method
     * @throws IOException
     * @throws InterruptedException
     */
    public void createShuffleTasks(Job job) throws IOException, InterruptedException{

        jobServiceKeyFrequency.put(job.getJobId(),getMachineKeyFreq(job));
    	Map<String, Service> keyWithService = getServicesWithMaxKeyFreq(jobServiceKeyFrequency.get(job.getJobId()));
    	triggerMapDataTransferForAll(job,keyWithService);
        job.setReduceKeyServiceMap(keyWithService);
        LOG.info("Shuffle tasks count : " + taskMap.values().size());
        job.setShuffleTasks(new ArrayList<>(taskMap.values()));
    }
    
    /**
     * given a job, get the machines associated with it, along with the keys 
     * and frequencies of each key on that machine 
     * @throws InterruptedException 
     * @throws IOException 
     */
    private Map<Service, Map<String, Long>> getMachineKeyFreq(Job job) throws IOException, InterruptedException{
    	Map<Service, Map<String, Long>> map = new HashMap<Service, Map<String, Long>>();
    	
    	// for each task, get the machines and key, frequencies

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
    private Map<String, Service> getServicesWithMaxKeyFreq(Map<Service, Map<String, Long>> data){
        Map<String, IntermediateServiceWithFrequency> interimOutput = new HashMap<String, IntermediateServiceWithFrequency>();

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

        Map<String, Service> output = new HashMap<String, Service>();
        for (String key : interimOutput.keySet()){
            output.put(key, interimOutput.get(key).getService());
        }

        return output;
    }

    /**
     * direct slaves to transfer map output data for each key to the slave with max frequency of that key
     *
     */
    private void triggerMapDataTransferForAll(Job job, Map<String, Service> keyService) throws InterruptedException, IOException{
        // send msg to each machine to transfer map data for each key to the machine which has the max freq for that key

        for (String key : keyService.keySet()){
            triggerMapDataTransferForKey(job, key, keyService.get(key), getServicesForKey(job, key));
        }

    }
    
    private List<Service> getServicesForKey(Job job, String key) throws InterruptedException, IOException{
    	List<Service> services = new ArrayList<Service>();
    	for (MapperTask task : job.getMapTasks()){
    		Service service = task.getService();
    		Map<String, Long> keyCountMap = task.getMapContext().getKeyCountMap();
    		if(keyCountMap.get(key)!=null){
    			services.add(service);
    		}
    	}
    	return services;
    }

    // direct all slaves to transfer map output data for given one key to the given slave with max frequency of that key
    void triggerMapDataTransferForKey(Job job, String key, Service keyMachine, List<Service> allMachines){
        for (Service machine : allMachines){
            triggerMapDataTransferForKeyOnMachine(job, key, keyMachine, machine);
        }
    }

    // direct one slave to send map output data for given key to given target machine with max frequency
    void triggerMapDataTransferForKeyOnMachine(Job job, String key, Service keyMachine, Service sourceMachine){
    	//create task for each
    	if (!keyMachine.getIp().equalsIgnoreCase(sourceMachine.getIp())){
        	ShuffleTask task = createShuffleTask(job, key, keyMachine, sourceMachine);
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
    ShuffleTask createShuffleTask(Job job, String key, Service keyMachine, Service sourceMachine){
    	ShuffleTask task = new ShuffleTaskImpl(job);
    	task.setKey(key);
    	task.setService(sourceMachine);
    	task.setDataTargetService(keyMachine);
    	return task;
    }

    @Override
    public synchronized void shuffleDataTransferCompleted(String taskId) {
    	ShuffleTask task = taskMap.get(taskId);
    	task.complete();
        LOG.info("Shuffle Task Completed : " + taskId);
        Job job = task.getJob();
        checkShuffleComplete(job);
    }

    public void checkShuffleComplete(Job job){
        if (job.isShuffleComplete()){
            config.getJobTracker().submit(job);
        }
    }

    @Override
    public void cleanup(Job job) {
        for(ShuffleTask shuffleTask : job.getShuffleTasks()){
            taskMap.remove(shuffleTask.getTaskId());
        }
        jobServiceKeyFrequency.remove(job.getJobId());
    }
    
}
