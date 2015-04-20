package com.rasp.mr.slave;

import com.rasp.fs.*;
import com.rasp.mr.ReduceContext;
import com.rasp.mr.Reducer;
import com.rasp.mr.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/13/15
 * Edited :
 */
public class TestReducer implements Reducer{

    static final Logger LOG = LoggerFactory.getLogger(TestReducer.class);

    @Override
    public void setup() {

    }

    @Override
    public void reduce(Writable key, com.rasp.fs.Iterable values, ReduceContext context) {
        while(values.hasNext()){
            try {
                context.write(key,values.next());
            } catch (IOException e) {
                LOG.error("", e);
            }
        }

    }

    @Override
    public void cleanup() {

    }
}
