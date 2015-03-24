/**
 * Author : Rahul Madhavan
 * File   : Job.java
 * Email  : 
 * Created: Mar 23, 2015
 * Edited : Mar 24, 2015
 */

package raspmr.RaspMR;

/* Import list */
import org.apache.hadoop.fs.Path;

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
    void setInputPath(Path path);

    /**
     * Sets the path for the output files to <code>path</code>
     *
     * @param path
     */
    void setOutputPath(Path path);

    /**
     * Set the {@link Mapper} class which represents the mapper to be used for this Job
     *
     * @param mapperClass
     */
    void setMapper(Class<? extends Mapper<?, ?>> mapperClass);

    /**
     * Set the {@link Reducer} class which represents the combiner to be used for this Job
     *
     * @param combinerClass
     */
    void setCombiner(Class<? extends Reducer<?, ?, ?, ?>> combinerClass);


    /**
     * Set the {@link Reducer} class which represents the reducer to be used for this Job
     *
     * @param reducerClass
     */
    void setReducer(Class<? extends Reducer<?, ?, ?, ?>> reducerClass);

    /**
     * @return true if the job is a standalone job
     */
    boolean isStandalone();

    /**
     *
     * @param standalone true if this job is to be run in standalone mode
     */
    void setStandalone(boolean standalone);

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
}