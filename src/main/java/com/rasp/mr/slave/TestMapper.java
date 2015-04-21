/**
 * Author : Rahul Madhavan
 * File   : TestMapper.java
 */

package com.rasp.mr.slave;

import org.slf4j.Logger;
import com.rasp.mr.Mapper;
import java.io.IOException;
import com.rasp.mr.MapContext;
import org.slf4j.LoggerFactory;

/**
 * User program - Mapper
 */
public class TestMapper implements Mapper {
    static final Logger LOG = LoggerFactory.getLogger(TestMapper.class);

    @Override
    public void setup() {
        // TODO
    }

    @Override
    public void map(Long key, String value, MapContext mapContext) {
        try {
            mapContext.write(new WritableImpl(key), new WritableImpl(value));
            LOG.debug(key + "   :   " + value);
        } catch (IOException e) {
            LOG.error("", e);
        }
    }

    @Override
    public void cleanup() {
        // TODO
    }
}