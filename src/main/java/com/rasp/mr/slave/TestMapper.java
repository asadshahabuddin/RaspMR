package com.rasp.mr.slave;

import com.rasp.mr.MapContext;
import com.rasp.mr.Mapper;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/5/15
 * Edited :
 */
public class TestMapper implements Mapper{

    @Override
    public void setup() {

    }

    @Override
    public void map(Long key, String value,MapContext mapContext) {
        try {
            mapContext.write(new WritableImpl(key),new WritableImpl(value));
            System.out.println(key +"   :   " +value);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void cleanup() {

    }
}
