package com.rasp.mr.slave;

import com.rasp.fs.*;
import com.rasp.mr.ReduceContext;
import com.rasp.mr.Reducer;
import com.rasp.mr.Writable;

import java.io.IOException;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/13/15
 * Edited :
 */
public class TestReducer implements Reducer{

    @Override
    public void setup() {

    }

    @Override
    public void reduce(Writable key, com.rasp.fs.Iterable values, ReduceContext context) {
        while(values.hasNext()){
            try {
                context.write(key,values.next());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void cleanup() {

    }
}
