/**
 * Author : Rahul Madhavan
 * File   : DataNode.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 2, 2015
 * Edited : Apr 2, 2015
 */

package com.rasp.fs;

/* Import list */
import java.io.IOException;
import com.rasp.utils.autodiscovery.Service;

public interface DataNode {
    void storeInputSplit(InputSplitImpl inputSplit)
        throws InterruptedException,IOException;

    void storeChunk(byte[] b)
        throws InterruptedException,IOException;

    void closeInputSplit()
        throws IOException;

    Service getService();
}
/* End of DataNode.java */