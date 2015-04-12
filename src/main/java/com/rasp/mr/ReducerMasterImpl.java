package com.rasp.mr;

import com.rasp.config.MasterConfiguration;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/12/15
 * Edited :
 */
public class ReducerMasterImpl implements ReducerMaster{

    private MasterConfiguration configuration;

    public ReducerMasterImpl(MasterConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void createReducerTasksForJob(Job job) {

    }

    @Override
    public void completeReduceTask(String taskId) {

    }

    @Override
    public void cleanup(Job job) {

    }
}
