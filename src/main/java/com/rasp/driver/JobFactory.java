package com.rasp.driver;

import com.rasp.mr.Job;

import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/16/15
 * Edited :
 */

/**
 * this class represents a factory for a type of Job
 */
public interface JobFactory{

    /**
     *  creates a job and sets the input file for the job as the given input file
     *
     * @param inputFile
     * @return
     */
    public Job createJob(String inputFile);

    /**
     *
     * @return the name of the job factory
     */
    public String getName();

}


