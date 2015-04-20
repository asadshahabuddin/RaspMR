package com.rasp.mr;

import com.rasp.config.MasterConfiguration;
import com.rasp.fs.InputFormatImpl;
import com.rasp.fs.InputSplit;
import com.rasp.mr.slave.MapperTaskImpl;
import com.rasp.utils.autodiscovery.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/12/15
 * Edited :
 */
public class MapperMasterImpl  implements MapperMaster{

    static final Logger LOG = LoggerFactory.getLogger(MapperMasterImpl.class);

    Map<String, MapperTask> taskMap;
    MasterConfiguration configuration;

    public MapperMasterImpl(MasterConfiguration configuration) {
        taskMap   = new HashMap<>();
        this.configuration = configuration;

    }

    @Override
    public void createMapperTasksForJob(Job job)
    {
        List<MapperTask> taskList = new ArrayList<>();
        InputFormatImpl inputFormat = configuration.getDataMaster().getInputFormat(job.getInputPath());
        LOG.info("Input Splits count  "+ inputFormat.getSplits().size());

        for(InputSplit inputSplit : inputFormat.getSplits()){

            MapperTask mapperTask = new MapperTaskImpl(job);
            mapperTask.setTaskInputSplit(inputSplit);
            mapperTask.setMapperClass(job.getMapperClass());
            taskList.add(mapperTask);
            taskMap.put(mapperTask.getTaskId(),mapperTask);
        }
        job.setMapTasks(taskList);
    }


    @Override
    public synchronized void completeMapTask(String taskId, Map<String, Long> keyCount){
        MapperTask task = taskMap.get(taskId);
        Job job = task.getJob();

        task.complete();
        for (String key :keyCount.keySet()){
            LOG.debug("MAP ::    key   ::"+key+"::    Value   ::"+keyCount.get(key));
        }
        task.getMapContext().setKeyCountMap(keyCount);
        checkMapComplete(job);


    }

    public void checkMapComplete(Job job) {
        if(job.isMapComplete()){
            configuration.getJobTracker().submit(job);
        }
    }


    private Collection<Service> getServicesWithMapData(Job job) {
        Map<String,Service> serviceMap = new HashMap<>();
        for(MapperTask mapperTask : job.getMapTasks()){
            try {
                serviceMap.put(mapperTask.getService().getIp(),mapperTask.getService());
            } catch (IOException e) {
                LOG.error("",e);
            } catch (InterruptedException e) {
                LOG.error("",e);
            }
        }
        return serviceMap.values();
    }

    @Override
    public void cleanup(Job job) {
        if(configuration.isCleanup()){
            for(Service service: getServicesWithMapData(job)){
                configuration.getTaskNode(service).cleanup(job);
            }
        }
        for(MapperTask mapperTask : job.getMapTasks()){
            taskMap.remove(mapperTask.getTaskId());
        }
    }
}
