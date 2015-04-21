/**
 * Author : Shivastuti Koul
 * File   : MapperTaskImpl.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.mr.slave;

/* Import list */
import java.util.UUID;
import com.rasp.mr.Job;
import org.slf4j.Logger;
import com.rasp.mr.Mapper;
import java.io.IOException;
import com.rasp.mr.MapContext;
import com.rasp.fs.InputSplit;
import org.slf4j.LoggerFactory;
import com.rasp.mr.MapContextImpl;
import com.rasp.fs.RecordReaderImpl;
import com.rasp.config.Configuration;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.autodiscovery.ServiceFactory;

public class MapperTaskImpl implements com.rasp.mr.MapperTask {
    static final Logger LOG = LoggerFactory.getLogger(MapperTaskImpl.class);

    private String taskId;
    private Job job;
    private InputSplit inputSplit;
    private Class<? extends Mapper> mapperClass;
    private boolean complete;
    private MapContext mapContext;
    private Service service;

    /**
     * Constructor 1
     *
     * @param job The job.
     */
    public MapperTaskImpl(Job job) {
        taskId = UUID.randomUUID().toString();
        mapContext = new MapContextImpl(job);
        this.job = job;
    }

    /**
     * Constructor 2
     *
     * @param taskId  Task identifier.
     * @param job     The job.
     * @param service Wrapper object containing the IP and port of a node.
     */
    public MapperTaskImpl(String taskId, Job job, Service service) {
        this.taskId = taskId;
        this.service = service;
        this.job = job;
        mapContext = new MapContextImpl(job);
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setJob(Job job) {
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
    public void setMapperClass(Class<? extends Mapper> mapperClass) {
        this.mapperClass = mapperClass;
    }

    @Override
    public Class<? extends Mapper> getMapperClass() {
        return mapperClass;
    }

    @Override
    public boolean execute() throws IllegalAccessException, InstantiationException, InterruptedException, IOException {

        LOG.debug("executing for : " + taskId);
        Mapper mapper = mapperClass.newInstance();
		
		/*
		(1) Initialize a reader object
		(2) Call map function successively for each key-value pair
		(3) Perform cleanup
		*/
        RecordReaderImpl reader = new RecordReaderImpl();
        reader.initialize(inputSplit);
        mapper.setup();
        while (reader.nextKeyValue()) {
            mapper.map(reader.getCurrentKey(), reader.getCurrentValue(), mapContext);
        }
        mapContext.close();
        reader.close();
        mapper.cleanup();

        return complete = true;
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
                getTaskInputSplit().getLocation(),
                Configuration.TASK_NODE_PORT);
    }

    @Override
    public void setMapContext(MapContext mapContext) {
        this.mapContext = mapContext;
    }

    /**
     * Set task status to complete.
     */
    public void complete() {
        complete = true;
    }

    /**
     * Get the map context object.
     * @return
     *            The map context object.
     */
    public MapContext getMapContext() {
        return mapContext;
    }
}