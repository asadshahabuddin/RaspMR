package com.rasp.mr.master;

import com.rasp.interfaces.*;
import com.rasp.mr.*;
import com.rasp.utils.autodiscovery.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public class JobImpl implements Job {

    private String jobId;
    private String inputPath;
    private Class<? extends Mapper> mapperClass;
    private Class<? extends Reducer> reducerClass;
    private boolean mapComplete;
    private boolean shuffleComplete;
    private boolean reduceComplete;
    private List<MapperTask> mapperTasks;
    private List<ReducerTask> reducerTasks;
    private Map<String,Service> reduceKeyServiceMap;


    public JobImpl(){
        mapComplete = false;
        shuffleComplete = false;
        reduceComplete = false;
        jobId = UUID.randomUUID().toString();
        mapperTasks = new ArrayList<>();
        reducerTasks = new ArrayList<>();
    }

    @Override
    public void setInputPath(String path) {
        this.inputPath = path;
    }

    @Override
    public void setMapper(Class<? extends Mapper> mapperClass) {
        this.mapperClass = mapperClass;
    }

    @Override
    public void setReducer(Class<? extends Reducer> reducerClass) {
        this.reducerClass = reducerClass;
    }

    @Override
    public void setPartitioner(Partitioner partitioner) {

    }

    @Override
    public boolean execute() {
        return false;
    }


    @Override
    public boolean isMapComplete() {
        if(!mapComplete){
            boolean result = true;
            for(MapperTask mapperTask: mapperTasks){
                if(!mapperTask.isCompleted()){
                    result = false;
                    break;
                }
            }
            if(result && mapperTasks.size() > 0)
                mapComplete = true;
        }
        return mapComplete;
    }


    @Override
    public boolean isShuffleComplete() {
        return shuffleComplete;
    }

    @Override
    public boolean isReduceComplete() {
        return reduceComplete;
    }

    @Override
    public void mapComplete() {
        mapComplete = true;
    }

    @Override
    public void shuffleComplete() {
        shuffleComplete = true;
    }

    @Override
    public void reduceComplete() {
        reduceComplete = true;
    }

    @Override
    public String getInputPath() {
        return inputPath;
    }

    @Override
    public void setMapTasks(List<MapperTask> mapTasks) {
        this.mapperTasks = mapTasks;
    }

    @Override
    public void setReduceTasks(List<ReducerTask> reduceTasks) {
        this.reducerTasks = reduceTasks;
    }

    @Override
    public List<MapperTask> getMapTasks() {
        return mapperTasks;
    }

    @Override
    public List<ReducerTask> getReduceTasks() {
        return reducerTasks;
    }

    public String getJobId() {
        return jobId;
    }

    @Override
    public Class<? extends Mapper> getMapperClass() {
        return mapperClass;
    }

    @Override
    public Class<? extends Reducer> getReducerClass() {
        return reducerClass;
    }

    @Override
    public Map<String, Service> getReduceKeyServiceMap() {
        return reduceKeyServiceMap;
    }

    @Override
    public void setReduceKeyServiceMap(Map<String, Service> keyServiceMap) {
        this.reduceKeyServiceMap = keyServiceMap;
    }

    @Override
    public void cleanup() {
        mapperTasks.clear();
        reducerTasks.clear();
        reduceKeyServiceMap.clear();

        mapperTasks = null;
        reducerTasks = null;
        reduceKeyServiceMap = null;
    }
}
