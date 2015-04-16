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

public interface JobFactory{

    public Job createJob(String inputFile);

    public String getName();

}


