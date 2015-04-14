package com.rasp.shuffle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.rasp.config.SlaveConfiguration;
import com.rasp.utils.autodiscovery.Service;

/**
 * Author : Sourabh Suman, Rahul Madhavan
 * File   : ReducerTask.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

public class ShuffleSlaveImpl implements ShuffleSlave {

    SlaveConfiguration config;
    HashMap<String, FileOutputStream> files;
    
    public ShuffleSlaveImpl(SlaveConfiguration conf){
        config = conf;
        files = new HashMap<>();
    }

    @Override
    public void createDataHandlerFor(String key, Service service) throws FileNotFoundException{
        //create a key K using key and service data
        //create a file for K
    	String filename = getFileNameFromKeyService(key, service);
    	File f = new File(filename);    	
    	
    	if (!files.containsKey(filename)){
    		if (f.exists()){
        		f.delete();
        	}
    		files.put(filename, new FileOutputStream(filename, true));    		
    	}    	    	
    }

    @Override
    public void storeDataFor(byte[] data, String key, Service service) throws IOException {
        //create a key K using key and service data
        //fetch the file for K and append data to it
    	FileOutputStream file = files.get(getFileNameFromKeyService(key, service));
    	if (file!=null)
    		file.write(data);
    }

    @Override
    public void closeDataHandlerFor(String key, Service service) throws IOException {
        //create a key using key and service data
        //fetch the file for K and close it
    	String filename = getFileNameFromKeyService(key, service);
    	FileOutputStream file = files.get(filename);    	
    	if (file!=null){
    		file.close();
    		files.remove(filename);
    	}
    }
    
    private String getFileNameFromKeyService(String key, Service service){
    	return key + "_" + service.getIp().replace(".", "");
    }

}
