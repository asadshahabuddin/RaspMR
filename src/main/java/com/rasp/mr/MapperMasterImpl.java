package com.rasp.mr;

import com.rasp.config.MasterConfiguration;
import com.rasp.fs.InputFormatImpl;
import com.rasp.fs.InputSplit;
import com.rasp.mr.slave.MapperTaskImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/12/15
 * Edited :
 */
public class MapperMasterImpl  implements MapperMaster{

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

        for(InputSplit inputSplit : inputFormat.getSplits()){

            MapperTask mapperTask = new MapperTaskImpl();
            mapperTask.setTaskInputSplit(inputSplit);
            mapperTask.setJob(job);
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
            System.out.println("MAP ::    key   ::"+key+"::    Value   ::"+keyCount.get(key));
        }
        task.getMapContext().setKeyCountMap(keyCount);

        if(job.isMapComplete()){
            configuration.getJobTracker().submit(job);
        }

    }

    @Override
    public void cleanup(Job job) {
        for(MapperTask task :job.getMapTasks()){
            taskMap.remove(task.getTaskId());
        }
    }
}
