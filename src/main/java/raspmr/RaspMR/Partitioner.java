/**
 * Author : Sourabh Suman
 * File   : Partitioner.java
 * Email  : 
 * Created: Mar 24, 2015
 * Edited : Mar 24, 2015
 */

package raspmr.RaspMR;

/**
 * Interface for Partitioning map output into different
 * groups for sending as input to reducer.
 * Values for same key must be sent to the same reducer.
 * */
public interface Partitioner<KEY, VALUE>
{
    /** 
    * Get the partition number for a given key (hence record) given the total 
    * number of partitions i.e. number of reduce-tasks for the job.
    * @param key the key to be partitioned.
    * @param value the entry value.
    * @param totalPartitions the total number of partitions.
    * @return the partition number for the key.
    */
    public abstract int getPartition(KEY key, VALUE value, int totalPartitions);
}
