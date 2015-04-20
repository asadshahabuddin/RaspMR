package com.rasp.mr.slave;

import com.rasp.mr.MapContext;
import com.rasp.mr.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/5/15
 * Edited :
 */
public class TestMapper implements Mapper{

    static final Logger LOG = LoggerFactory.getLogger(TestMapper.class);

    @Override
    public void setup() {

    }

    @Override
    public void map(Long key, String value,MapContext mapContext) {
        try {
            mapContext.write(new WritableImpl(key),new WritableImpl(value));
            LOG.debug(key +"   :   " +value);
        } catch (IOException e) {
            LOG.error("",e);
        }

    }

    @Override
    public void cleanup() {

    }
}
