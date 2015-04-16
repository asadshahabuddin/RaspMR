package com.rasp.driver;

import com.rasp.mr.Job;
import com.rasp.mr.master.JobImpl;
import com.rasp.mr.slave.TestMapper;
import com.rasp.mr.slave.TestReducer;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/16/15
 * Edited :
 */
public class TestJobFactory implements JobFactory{

    @Override
    public Job createJob(String inputFile) {
        Job job = new JobImpl();
        job.setInputPath(inputFile);
        job.setMapper(TestMapper.class);
        job.setReducer(TestReducer.class);
        return job;
    }

    @Override
    public String getName() {
        return "test";
    }

}
