package com.rasp.driver;

import com.rasp.fs.Iterable;
import com.rasp.mr.ReduceContext;
import com.rasp.mr.Reducer;
import com.rasp.mr.Writable;
import com.rasp.mr.slave.WritableImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom reducer implementation for calculating averages
 * Author : Sourabh Suman
 * File   : AvgReducer.java
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/19/15
 * Edited :
 */
public class AvgReducer implements Reducer{

    static final Logger LOG = LoggerFactory.getLogger(AvgReducer.class);

    @Override
	public void cleanup() {
		
	}

    @Override
	public void reduce(Writable paramWritable, Iterable paramIterable,
			ReduceContext paramReduceContext) {
		
    	try {			
			int count = 0;
			int sum = 0;
			while(paramIterable.hasNext()){
				Object o = paramIterable.next().getObj();
                LOG.debug(o.toString());
				Double i = Double.parseDouble(o.toString());
				count++;
				sum+=i;
			}
			
			paramReduceContext.write(paramWritable, new WritableImpl(sum/count));
		} catch (Exception e) {
			LOG.error("",e);
		}
		
	}

    @Override
	public void setup() {
		
	}
}
