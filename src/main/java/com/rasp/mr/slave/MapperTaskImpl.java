/**
 * Author : Asad Shahabuddin
 * File   : MapperTask.java
 * Email  : asad808@ccs.neu.edu 
 * Created: Apr 4, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import java.util.UUID;
import java.io.IOException;

import com.rasp.fs.RecordReaderImpl;
import com.rasp.mr.MapContext;
import com.rasp.mr.MapContextImpl;
import com.rasp.mr.Job;
import com.rasp.mr.Mapper;
import com.rasp.config.Configuration;
import com.rasp.fs.InputSplit;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class MapperTaskImpl implements com.rasp.mr.MapperTask{
	private String taskId;
	private Job job;
	private InputSplit inputSplit;
	private Class<? extends Mapper> mapperClass;
	private boolean complete;
    private MapContext mapContext;
    private Service service;

    public MapperTaskImpl(Job job)
    {
        taskId = UUID.randomUUID().toString();
        mapContext = new MapContextImpl(job);
        this.job = job;

    }

    public MapperTaskImpl(String taskId, Job job,Service service)
    {
        this.taskId = taskId;
        this.service = service;
        this.job = job;
        mapContext = new MapContextImpl(job);
    }
    
    @Override
    public String getTaskId()
    {
        return taskId;
    }

    @Override
    public void setJob(Job job)
    {
        this.job = job;
    }
    
	@Override
	public Job getJob()
	{
		return job;
	}

	@Override
	public void setTaskInputSplit(InputSplit inputSplit)
	{
		this.inputSplit = inputSplit;
	}
	
	@Override
	public InputSplit getTaskInputSplit()
	{
		return inputSplit;
	}

	@Override
	public void setMapperClass(Class<? extends Mapper> mapperClass)
	{
		this.mapperClass = mapperClass;
	}

	@Override
	public Class<? extends Mapper> getMapperClass()
	{
		return mapperClass;
	}
	
	@Override
	public boolean execute() throws IllegalAccessException, InstantiationException, InterruptedException, IOException{

        System.out.println("executing for : "+taskId);
        Mapper mapper = mapperClass.newInstance();
		
		/*
		(1) Initialize a reader object
		(2) Call map function successively for each key-value pair
		(3) Perform cleanup
		*/
		RecordReaderImpl reader = new RecordReaderImpl();
		reader.initialize(inputSplit);
		mapper.setup();
		while(reader.nextKeyValue()){
			mapper.map(reader.getCurrentKey(), reader.getCurrentValue(), mapContext);
		}
        mapContext.close();
		reader.close();
		mapper.cleanup();

		return complete = true;
	}

    public void complete()
    {
        complete = true;
    }

    @Override
    public boolean isCompleted()
    {
        return complete;
    }

    @Override
    public Service getService()
		throws IOException, InterruptedException
    {
        return ServiceFactory.createService(
               ServiceType.TASK_TRACKER,
               getTaskInputSplit().getLocation(),
               Configuration.TASK_NODE_PORT);
    }

    public MapContext getMapContext() {
        return mapContext;
    }

    @Override
    public void setMapContext(MapContext mapContext) {
        this.mapContext = mapContext;
    }
}
/* End of MapperTask.java */