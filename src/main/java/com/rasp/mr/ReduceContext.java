package com.rasp.mr;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/12/15
 * Edited :
 */
public interface ReduceContext {

    public void write(Writable key, Writable value) throws IOException;

    public void close() throws IOException;

    public void open();

    public Pair<Writable,Writable> read();

}
