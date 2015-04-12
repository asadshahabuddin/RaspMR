/**
 * Author : Asad Shahabuddin
 * File   : ReducerTaskImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 12, 2015
 * Edited : Apr 12, 2015
 */

package com.rasp.mr;
import java.util.UUID;
import java.io.IOException;

import com.rasp.config.Configuration;
import com.rasp.fs.InputSplit;
import com.rasp.fs.RecordReaderImpl;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.autodiscovery.ServiceType;

public class ReducerTaskImpl
    implements ReducerTask {
    private String taskId;
    private ReduceContext reduceContext;
    private Class<? extends Reducer> reducerClass;
    private Job job;
    private InputSplit inputSplit;
    private boolean complete;
    private Service service;


    public ReducerTaskImpl(Service service)
    {
        taskId = UUID.randomUUID().toString();
        this.service = service;
        // reduceContext = new ReduceContextImpl();
    }

    public ReducerTaskImpl(String taskId, Service service)
    {
        this.taskId = taskId;
        this.service = service;
        // reduceContext = new ReduceContextImpl();
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setReducerClass(Class<? extends Reducer> reducerClass) {
        this.reducerClass = reducerClass;
    }

    @Override
    public Class<? extends Reducer> getReducerClass() {
        return reducerClass;
    }

    @Override
    public void setJob(Job job)
    {
        this.job = job;
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public void setTaskInputSplit(InputSplit inputSplit) {
        this.inputSplit = inputSplit;
    }

    @Override
    public InputSplit getTaskInputSplit() {
        return inputSplit;
    }

    @Override
    public boolean execute()
        throws IllegalAccessException, InstantiationException,
               InterruptedException, IOException {
        Reducer reducer = reducerClass.newInstance();
        if(reducer == null){
            return false;
        }

		/*
		(1) Initialize a reader object
		(2) Call map function successively for each key-value pair
		(3) Perform cleanup
		*/
        RecordReaderImpl reader = new RecordReaderImpl();
        reader.initialize(inputSplit);
        reducer.setup();
        while(reader.nextKeyValue())
        {
            // reducer.reduce(reader.getCurrentKey(), reader.getCurrentValue(), reduceContext);
        }
        reduceContext.close();
        reader.close();
        reducer.cleanup();

        return complete = true;
    }

    @Override
    public void complete() {
        complete = true;
    }

    @Override
    public boolean isCompleted() {
        return complete;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}