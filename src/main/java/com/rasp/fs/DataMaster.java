/**
 * Author : Rahul Madhavan
 * File   : DataMaster.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 4, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.fs;

/* Import list */
import java.io.IOException;

public interface DataMaster {
    public InputFormatImpl createInputFormat(String inputFile,int workerCount)
        throws InterruptedException, IOException;

    public void splitAndSend(String inputFile)
        throws IOException, InterruptedException;

    public InputFormatImpl getInputFormat(String path);
}
/* End of DataMaster.java */