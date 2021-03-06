/**
 * Author : Shivastuti Koul
 * File   : ShuffleTaskImpl.java
 * Email  : koul.sh@husky.neu.edu
 * Created: Apr 9, 2015
 * Edited : Apr 12, 2015
 */

package com.rasp.mr.slave;

import com.rasp.mr.*;
import java.util.UUID;
import java.io.IOException;
import com.rasp.fs.InputSplit;
import java.io.RandomAccessFile;
import com.rasp.config.Configuration;
import com.rasp.utils.file.FSHelpers;
import com.rasp.utils.autodiscovery.Service;

public class ShuffleTaskImpl implements ShuffleTask {
    /* Constant(s) */
    public static final int BUFFER_SIZE = 1000 * 8192;

    private String taskId;
    private Job job;
    private Service service;
    private String key;
    private Service dataTargetService;
    private boolean complete;
    private Configuration conf;

    /**
     * Constructor 1
     *
     * @param job The job.
     */
    public ShuffleTaskImpl(Job job) {
        taskId = UUID.randomUUID().toString();
        this.job = job;
    }

    /**
     * Constructor 2
     *
     * @param taskId  Task identifier.
     * @param job     The job.
     * @param service Wrapper object containing the IP and port of a node.
     * @param conf    Configuration object.
     */
    public ShuffleTaskImpl(String taskId, Job job, Service service, Configuration conf) {
        this.taskId = taskId;
        this.service = service;
        this.conf = conf;
        this.job = job;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public boolean execute()
            throws IllegalAccessException, InstantiationException,
            InterruptedException, IOException {
        TaskNode node = conf.getTaskNode(dataTargetService);
        /* Check if the node exists and is set */
        if (node == null) {
            return false;
        }

        RandomAccessFile f = FSHelpers.openFile(job, key + "_mout", "r");
        long flen = f.length();    /* Length of file */
        /* Check if the file has some content */
        if (flen == 0) {
            return false;
        }

        /*
        (1) Total bytes read for the current split.
        (2) Contiguous bytes read for a part of the current split.
        (3) Initiate data transfer for the key.
        */
        int bytesRead = 0;
        byte[] b = null;
        node.initiateDataTransferForKey(key, job.getJobId(), dataTargetService);

        while (bytesRead < flen) {
            int size = BUFFER_SIZE;
            /* Last chunk of bytes for the current split */
            if ((flen - bytesRead) < BUFFER_SIZE) {
                size = (int) (flen - bytesRead);
            }

            /*
            (1) Read a block from the input file at a specific offset.
            (2) Append block to the current input split.
            (3) Update bytes read for the current split.
            (4) Update bytes read for the current file.
            */
            f.seek(bytesRead);
            b = new byte[size];
            f.read(b, 0, size);
            node.transferDataForKey(b, key, job.getJobId(), dataTargetService);
            bytesRead += size;
        }
        node.terminateTransferDataForKey(key, job.getJobId(), dataTargetService);

        return true;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void complete() {
        complete = true;
    }

    @Override
    public boolean isCompleted() {
        return complete;
    }

    @Override
    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public Service getService() throws IOException, InterruptedException {
        return service;
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public InputSplit getTaskInputSplit() {
        return null;
    }

    @Override
    public void setTaskInputSplit(InputSplit inputSplit) {
    }

    @Override
    public Service getDataTargetService() {
        return dataTargetService;
    }

    @Override
    public void setDataTargetService(Service dataTargetService) {
        this.dataTargetService = dataTargetService;
    }
}