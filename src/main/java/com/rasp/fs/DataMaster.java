package com.rasp.fs;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public interface DataMaster {

    public InputFormat createInputFormat(String inputFile,int workerCount) throws InterruptedException, IOException;

    public void splitAndSend(String inputFile) throws IOException, InterruptedException;

    public InputFormat getInputFormat(String path);

}
