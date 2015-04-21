package com.rasp.shuffle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.rasp.config.SlaveConfiguration;
import com.rasp.mr.master.JobImpl;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.file.FSHelpers;

/**
 * Author : Sourabh Suman
 * File   : ShuffleSlaveImpl.java
 * Email  : sourabhs@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015, Apr 11, 2015
 */

public class ShuffleSlaveImpl implements ShuffleSlave {

    SlaveConfiguration config;
    HashMap<String, FileOutputStream> files;
    
    public ShuffleSlaveImpl(SlaveConfiguration conf){
        config = conf;
        files = new HashMap<>();
    }

    @Override
    public void createDataHandlerFor(String key, String jobId, Service service) throws IOException {
        //create a key K using key and service data
        //create a file for K
    	String filename = getFileNameFromKeyService(key, service);
    	if (!files.containsKey(filename)){
    		files.put(filename, FSHelpers.deleteAndCreateFile(new JobImpl(jobId),filename,true));
    	}    	    	
    }

    @Override
    public void storeDataFor(byte[] data, String key, String jobId, Service service) throws IOException {
        //create a key K using key and service data
        //fetch the file for K and append data to it
    	FileOutputStream file = files.get(getFileNameFromKeyService(key, service));
    	if (file!=null)
    		file.write(data);
    }

    @Override
    public void closeDataHandlerFor(String key, String jobId, Service service) throws IOException {
        //create a key using key and service data
        //fetch the file for K and close it
    	String filename = getFileNameFromKeyService(key, service);
    	FileOutputStream file = files.get(filename);    	
    	if (file!=null){
    		file.close();
    		files.remove(filename);
    	}
    }

    /**
     * @param key
     * @param service
     * @return new filename
     */
    private String getFileNameFromKeyService(String key, Service service){
    	return key + "_" + service.getIp().replace(".", "");
    }

}
