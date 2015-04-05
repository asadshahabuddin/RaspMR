/**
 * Author : Asad Shahabuddin
 * File   : MapperTask.java
 * Email  : asad808@ccs.neu.edu 
 * Created: Apr 4, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.task;

/* Import list */
import java.io.IOException;
import java.util.UUID;

import com.rasp.config.Configuration;
import com.rasp.interfaces.Job;
import com.rasp.fs.RecordReader;
import com.rasp.interfaces.Mapper;
import com.rasp.interfaces.InputSplit;

import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

public class MapperTask
    implements com.rasp.interfaces.MapperTask
{
	@SuppressWarnings("unused")
	private String taskId;
	private InputSplit inputSplit;
	private Class<? extends Mapper<?, ?>> mapperClass;
    private boolean complete;

    public MapperTask()
    {
        taskId = UUID.randomUUID().toString();
    }

    public MapperTask(String taskId)
    {
        this.taskId = taskId;
    }
    
    @Override
    public String getTaskId()
    {
        return null;
    }

	@Override
	public Job getJob()
	{
		// TODO
		return null;
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
	public void setMapperClass(Class<? extends Mapper<?, ?>> mapperClass)
	{
		this.mapperClass = mapperClass;
	}

	@Override
	public Class<? extends Mapper<?, ?>> getMapperClass()
	{
		return mapperClass;
	}
	
	@Override
	public boolean execute()
		throws IllegalAccessException, InstantiationException,
			   InterruptedException,   IOException
	{
		Mapper<?, ?> mapper = mapperClass.newInstance();
		if(mapper == null)
		{
			return false;
		}
		
		/*
		(1) Initialize a reader object
		(2) Call map function successively for each key-value pair
		(3) Perform cleanup
		*/
		RecordReader reader = new RecordReader();
		reader.initialize(inputSplit);
		mapper.setup();
		while(reader.nextKeyValue())
		{
			mapper.map(reader.getCurrentKey(), reader.getCurrentValue());
		}
		reader.close();
		mapper.cleanup();

		return complete = true;
	}

    @Override
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
}
/* End of MapperTask.java */