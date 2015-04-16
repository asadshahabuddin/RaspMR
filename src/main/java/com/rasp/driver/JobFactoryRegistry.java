package com.rasp.driver;

import com.rasp.mr.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/16/15
 * Edited :
 */
public class JobFactoryRegistry {

    static Map<String,JobFactory> jobFactoryMap = new HashMap<>();

    public static Job createJob(String jobType,String inputFile){
        return jobFactoryMap.get(jobType).createJob(inputFile);
    }

    public static void register(Class<? extends JobFactory> jobFactoryClass){
        try {
            JobFactory jobFactory = jobFactoryClass.newInstance();
            jobFactoryMap.put(jobFactory.getName(),jobFactory);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
