/**
 * Author : Asad Shahabuddin
 * File   : ReducerTaskImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 12, 2015
 * Edited : Apr 12, 2015
 */

package com.rasp.mr;

import java.util.UUID;
import java.io.FileReader;
import java.io.IOException;
import com.rasp.fs.Iterable;
import com.rasp.fs.InputSplit;
import com.rasp.utils.autodiscovery.Service;

public class ReducerTaskImpl
    implements ReducerTask {
    private String taskId;
    private ReduceContext reduceContext;
    private Class<? extends Reducer> reducerClass;
    private Job job;
    private String key;
    private boolean complete;
    private Service service;

    public ReducerTaskImpl(Service service)
    {
        taskId = UUID.randomUUID().toString();
        this.service = service;
        reduceContext = new ReduceContextImpl();
    }

    public ReducerTaskImpl(String taskId, Service service)
    {
        this.taskId = taskId;
        this.service = service;
        reduceContext = new ReduceContextImpl();
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
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setTaskInputSplit(InputSplit inputSplit) {
        throw new UnsupportedOperationException("  [error] " +
                "This operation is not supported for a reducer task");
    }

    @Override
    public InputSplit getTaskInputSplit() {
        throw new UnsupportedOperationException("  [error] " +
                "This operation is not supported for a reducer task");
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
		(1) Initialize an iterator.
		(2) Call reduce function successively for each key-value pair.
		(3) Perform cleanup.
		*/
        Iterable iterable = new Iterable(key);
        reducer.setup();
        reducer.reduce(key, iterable, reduceContext);
        reduceContext.close();
        iterable.close();
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

    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}