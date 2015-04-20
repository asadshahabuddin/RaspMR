package com.rasp.driver;

import com.rasp.mr.Job;
import com.rasp.mr.master.JobImpl;

public class AvgJobFactory implements JobFactory{
	public Job createJob(String inputFile) {
		Job job = new JobImpl();
		job.setInputPath(inputFile);
		job.setMapper(AvgMapper.class);
		job.setReducer(AvgReducer.class);
		return job;
	}

	public String getName() {
		return "averages";
	}

}
