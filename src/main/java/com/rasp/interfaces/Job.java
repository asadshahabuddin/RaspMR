/**
 * Author : Rahul Madhavan
 * File   : Job.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.interfaces;

/* Import list */
import org.apache.hadoop.fs.Path;

import java.util.List;

/**
 *
 * Job represents a Map Reduce Job
 * It references 1 {@link Mapper} and 1 {@link Reducer} Class
 * It also has references a {@link Context} object which holds
 * the context variables and its corresponding values for the job
 */
public interface Job
{
    /**
     * Sets the path for the input files of the job to <code>path</code>
     *
     * @param path
     */
    void setInputPath(String path);

    /**
     * Sets the path for the output files to <code>path</code>
     *
     * @param path
     */
    void setOutputPath(String path);

    /**
     * Set the {@link Mapper} class which represents the mapper to be used for this Job
     *
     * @param mapperClass
     */
    void setMapper(Class<? extends Mapper<?, ?>> mapperClass);


    /**
     * Set the {@link Reducer} class which represents the reducer to be used for this Job
     *
     * @param reducerClass
     */
    void setReducer(Class<? extends Reducer<?, ?, ?, ?>> reducerClass);


    /**
     * Set the {@link Partitioner}for this Job
     *
     * @param partitioner
     */
    void setPartitioner(Partitioner partitioner);

    /**
     * executes the given job
     *
     * @return true if the job executes correctly else returns false
     */
    boolean execute();

    /**
     * Every job has 1 {@link Context} object
     *
     * @return the {@link Context} object for the current job
     */
    Context getContext();


    boolean isMapComplete();
    boolean isShuffleComplete();
    boolean isReduceComplete();

    void mapComplete();
    void shuffleComplete();
    void reduceComplete();

    String getInputPath();

    void setMapTasks(List<MapperTask> mapTasks);
    void setReduceTasks(List<ReducerTask> reduceTasks);

    List<MapperTask> getMapTasks();
    List<ReducerTask> getReduceTasks();

    String getJobId();

    Class<? extends Mapper<?, ?>> getMapperClass();
    Class<? extends Reducer<?, ?, ?, ?>> getReducerClass();

}