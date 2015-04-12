/**
 * Author : Rahul Madhavan
 * File   : ReduceContext.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 12, 2015
 * Edited : Apr 12, 2015
 */

package com.rasp.mr;

import java.io.IOException;
import org.apache.commons.lang3.tuple.Pair;

public interface ReduceContext {
    public Pair<Writable, Writable> read() throws IOException;

    public void write(Writable k, Writable v) throws IOException;

    public void close() throws IOException;
}