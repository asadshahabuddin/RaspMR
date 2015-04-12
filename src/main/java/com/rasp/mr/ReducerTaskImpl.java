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
import com.rasp.config.Configuration;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class ReducerTaskImpl
    implements ReducerTask {
    private String taskId;
    private ReduceContext reduceContext;
    private Class<? extends Reducer> reducerClass;
    private Job job;
    private String key;
    private InputSplit inputSplit;
    private boolean complete;

    public ReducerTaskImpl()
    {
        taskId = UUID.randomUUID().toString();
        // reduceContext = new ReduceContextImpl();
    }

    public ReducerTaskImpl(String taskId)
    {
        this.taskId = taskId;
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
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
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
		(1) Initialize an iterator.
		(2) Call reduce function successively for each key-value pair.
		(3) Perform cleanup.
		*/
        Iterable iterable = new Iterable(new FileReader(inputSplit.getLocation()));
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

    @Override
    public Service getService()
        throws IOException, InterruptedException {
        return ServiceFactory.createService(
                ServiceType.TASK_TRACKER,
                inputSplit.getLocation(),
                Configuration.TASK_NODE_PORT);
    }
}