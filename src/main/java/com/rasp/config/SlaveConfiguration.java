/**
 * Author : Rahul Madhavan
 * File   : SlaveConfiguration.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 4, 2015
 */

package com.rasp.config;

/* Import list */
import com.rasp.fs.DataNode;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.rasp.fs.InputSplit;
import com.rasp.fs.slave.DataNodeServerImpl;
import com.rasp.interfaces.TaskScheduler;
import com.rasp.interfaces.TaskTracker;
import com.rasp.interfaces.impl.TaskNode;
import raspmr.RaspMR.utils.autodiscovery.Service;
import raspmr.RaspMR.utils.autodiscovery.ServiceFactory;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

public class SlaveConfiguration extends Configuration
{
	private DataNode dataNode;
    private TaskNode taskNode;
    private Map<Integer,InputSplit> inputSplitMap;
    private TaskTracker taskTracker;
    public static final String INPUT_SPLIT_FILENAME = "split.txt";


    public SlaveConfiguration()
        throws FileNotFoundException
    {
        super(Configuration.DATA_NODE_PORT,ServiceType.TASK_TRACKER);
        inputSplitMap = new HashMap<>();
    }


    public void setDataNode(DataNode dataNode) {
        this.dataNode = dataNode;
    }

    public DataNode getDataNode()
    {
        return dataNode;
    }


    public TaskNode getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(TaskNode taskNode) {
        this.taskNode = taskNode;
    }

    public void addInputSplit(InputSplit inputSplit){
        inputSplitMap.put(inputSplit.getIdx(),inputSplit);
    }

    public InputSplit getInputSplit(int idx){
        return inputSplitMap.get(idx);
    }

    public TaskTracker getTaskTracker() {
        return taskTracker;
    }

    public void setTaskTracker(TaskTracker taskTracker) {
        this.taskTracker = taskTracker;
    }
}
/* End of SlaveConfiguration.java */