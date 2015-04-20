package com.rasp.driver;

import com.rasp.fs.Iterable;
import com.rasp.mr.ReduceContext;
import com.rasp.mr.Reducer;
import com.rasp.mr.Writable;
import com.rasp.mr.slave.WritableImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvgReducer implements Reducer{

    static final Logger LOG = LoggerFactory.getLogger(AvgReducer.class);

	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

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

	public void setup() {
		// TODO Auto-generated method stub
		
	}
}
