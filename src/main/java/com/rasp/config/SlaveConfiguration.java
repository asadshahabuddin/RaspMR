/**
 * Author : Rahul Madhavan
 * File   : SlaveConfiguration.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.config;

/* Import list */
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.rasp.fs.master.DataNodeClientImpl;
import com.rasp.mr.slave.JobNodeClientImpl;
import com.rasp.shuffle.ShuffleSlave;
import com.rasp.utils.autodiscovery.Service;
import com.rasp.utils.autodiscovery.ServiceFactory;
import com.rasp.utils.protobuf.ProtoClient;
import com.rasp.fs.DataNode;
import com.rasp.mr.JobNode;
import com.rasp.mr.TaskNode;
import com.rasp.mr.TaskTracker;
import com.rasp.fs.InputSplitImpl;
import java.io.FileNotFoundException;
import com.rasp.utils.autodiscovery.ServiceType;

public class SlaveConfiguration extends Configuration {
	private DataNode dataNode;
    private TaskNode taskNode;
    private JobNode jobNode;
    private ShuffleSlave shuffleSlave;
    private Map<Integer,InputSplitImpl> inputSplitMap;
    private TaskTracker taskTracker;
    public static final String INPUT_SPLIT_FILENAME = "split.txt";


    public SlaveConfiguration()
        throws FileNotFoundException {
        super(Configuration.DATA_NODE_PORT,ServiceType.TASK_TRACKER);
        inputSplitMap = new HashMap<>();
    }


    /**
     *
     * @return the job node on the network
     */
    public JobNode getJobNode() {
        if(jobNode == null){
            List<Service> services=  this.getDiscoverer().getServices(ServiceType.JOB_TRACKER);
            if(services.size() != 1){
                throw new RuntimeException("There must be exactly one master node running in the network");
            }
            Service jobService = services.get(0);
            jobService = ServiceFactory.createService(ServiceType.JOB_TRACKER,jobService.getIp(),Configuration.JOB_NODE_PORT);
            jobNode = new JobNodeClientImpl(protoClient,jobService);
            return jobNode;
        }else{
            return jobNode;
        }
    }

    public void setDataNode(DataNode dataNode) {
        this.dataNode = dataNode;
    }
    public DataNode getDataNode() {
        return dataNode;
    }

    public TaskNode getTaskNode() {
        return taskNode;
    }
    public void setTaskNode(TaskNode taskNode) {
        this.taskNode = taskNode;
    }

    public void addInputSplit(InputSplitImpl inputSplit) {
        inputSplitMap.put(inputSplit.getIdx(),inputSplit);
    }
    public InputSplitImpl getInputSplit(int idx) {
        return inputSplitMap.get(idx);
    }

    public TaskTracker getTaskTracker() {
        return taskTracker;
    }
    public void setTaskTracker(TaskTracker taskTracker) {
        this.taskTracker = taskTracker;
    }

    public ShuffleSlave getShuffleSlave() {
        return shuffleSlave;
    }
    public void setShuffleSlave(ShuffleSlave shuffleSlave) {
        this.shuffleSlave = shuffleSlave;
    }
}
/* End of SlaveConfiguration.java */