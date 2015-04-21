/**
 * Author : Rahul Madhavan
 * File   : TestMapper.java
 */

package com.rasp.mr.slave;

import org.slf4j.Logger;
import java.io.IOException;
import com.rasp.mr.Reducer;
import com.rasp.mr.Writable;
import org.slf4j.LoggerFactory;
import com.rasp.mr.ReduceContext;

/**
 * User program - Reducer
 */
public class TestReducer implements Reducer {
    static final Logger LOG = LoggerFactory.getLogger(TestReducer.class);

    @Override
    public void setup() {
        // TODO
    }

    @Override
    public void reduce(Writable key, com.rasp.fs.Iterable values, ReduceContext context) {
        while (values.hasNext()) {
            try {
                context.write(key, values.next());
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
    }

    @Override
    public void cleanup() {
        // TODO
    }
}