package com.rasp.driver;

import com.rasp.mr.Job;
import com.rasp.mr.master.JobImpl;

/**
 * JobFactory implementation for creating jobs of type "averages"
 * Author : Sourabh Suman
 * File   : AvgJobFactory.java
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/19/15
 * Edited :
 */

public class AvgJobFactory implements JobFactory{
	
	/**
	 * create new job for "averages" job
	 * @param inputFile
	 * @return new job which will work with the given input file
	 */
	public Job createJob(String inputFile) {
		Job job = new JobImpl();
		job.setInputPath(inputFile);
		job.setMapper(AvgMapper.class);
		job.setReducer(AvgReducer.class);
		return job;
	}

	/**
	 * @return job name/type for this jobFactory
	 */
	public String getName() {
		return "averages";
	}

}
