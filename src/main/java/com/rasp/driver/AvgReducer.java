package com.rasp.driver;

import com.rasp.fs.Iterable;
import com.rasp.mr.ReduceContext;
import com.rasp.mr.Reducer;
import com.rasp.mr.Writable;
import com.rasp.mr.slave.WritableImpl;

public class AvgReducer implements Reducer{
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
				System.out.println(o.toString());
				Integer i = Integer.parseInt(o.toString());
				count++;
				sum+=i;
			}

			
			paramReduceContext.write(paramWritable, new WritableImpl(sum/count));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setup() {
		// TODO Auto-generated method stub
		
	}
}
