package com.rasp.driver;

import com.rasp.mr.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/16/15
 * Edited :
 */

/**
 * this class maintains the mapping of {@link com.rasp.driver.JobFactory names}
 * to {@link com.rasp.driver.JobFactory}
 *
 */
public class JobFactoryRegistry {

    static final Logger LOG = LoggerFactory.getLogger(JobFactoryRegistry.class);

    static Map<String,JobFactory> jobFactoryMap = new HashMap<>();

    /**
     *  creates a job of the given type with the given input file set in the job as input
     *
     * @param jobType
     * @param inputFile
     * @return
     */
    public static Job createJob(String jobType,String inputFile){
        return jobFactoryMap.get(jobType).createJob(inputFile);
    }

    /**
     * registers a {@link com.rasp.driver.JobFactory} which is identified
     * by its name {@link JobFactory#getName()}
     *
     * @param jobFactoryClass
     */
    public static void register(Class<? extends JobFactory> jobFactoryClass){
        try {
            JobFactory jobFactory = jobFactoryClass.newInstance();
            jobFactoryMap.put(jobFactory.getName(),jobFactory);
        } catch (InstantiationException e) {
            LOG.error("",e);
        } catch (IllegalAccessException e) {
            LOG.error("",e);
        }
    }

}
