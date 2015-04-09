/**
 * Author : Rahul Madhavan
 * File   : SlaveConfiguration.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.config;

/* Import list */
import java.util.Map;
import java.util.HashMap;

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
    private Map<Integer,InputSplitImpl> inputSplitMap;
    private TaskTracker taskTracker;
    public static final String INPUT_SPLIT_FILENAME = "split.txt";
    public ProtoClient protoClient;

    public SlaveConfiguration()
        throws FileNotFoundException {
        super(Configuration.DATA_NODE_PORT,ServiceType.TASK_TRACKER);
        protoClient = new ProtoClient();
        inputSplitMap = new HashMap<>();
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

    public JobNode getJobNode() {
        return jobNode;
    }

    public void setJobNode(JobNode jobNode) {
        this.jobNode = jobNode;
    }
}
/* End of SlaveConfiguration.java */