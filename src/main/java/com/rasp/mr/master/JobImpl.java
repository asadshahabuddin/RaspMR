package com.rasp.mr.master;

import com.rasp.interfaces.*;
import com.rasp.mr.*;
import com.rasp.mr.slave.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author : Rahul Madhavan, Sourabh Suman
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited : 4/12/15
 */
public class JobImpl implements Job {

    private String jobId;
    private String inputPath;
    private String outputPath;
    private Class<? extends Mapper> mapperClass;
    private Class<? extends Reducer> reducerClass;
    private boolean mapComplete;
    private boolean shuffleComplete;
    private boolean reduceComplete;
    private List<MapperTask> mapperTasks;
    private List<ReducerTask> reducerTasks;


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
    public void setOutputPath(String path) {
        this.outputPath = path;
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

    public boolean isTaskComplete(boolean taskComplete, List<Task> tasks){
    	if(!taskComplete){
            boolean result = true;
            for(Task task: tasks){
                if(!task.isCompleted()){
                    result = false;
                    break;
                }
            }
            if(result && tasks.size() > 0)
            	taskComplete = true;
        }
        return taskComplete;    	
    }
    
    @Override
    public boolean isMapComplete() {
    	List<Task> tasks = new ArrayList<Task>();
    	tasks.addAll(mapperTasks);
        mapComplete = isTaskComplete(mapComplete, tasks);
        return mapComplete;
    }


    @Override
    public boolean isShuffleComplete() {
    	/*List<Task> tasks = new ArrayList<Task>();
    	tasks.addAll(shuf);
        shuffleComplete = isTaskComplete(mapComplete, tasks);*/
        return shuffleComplete;
    }

    @Override
    public boolean isReduceComplete() {
    	List<Task> tasks = new ArrayList<Task>();
    	tasks.addAll(reducerTasks);
    	reduceComplete = isTaskComplete(reduceComplete, tasks);
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
}
