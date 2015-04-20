package com.rasp.driver;

import java.io.IOException;

import com.rasp.mr.MapContext;
import com.rasp.mr.Mapper;
import com.rasp.mr.slave.WritableImpl;

public class AvgMapper implements Mapper{
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	public void map(Long paramLong, String paramString,
			MapContext paramMapContext) {
		
		String[] columns = paramString.split("\t");
		try{
			if (columns.length>=5) {
				//int num = Integer.parseInt(columns[1]);
				paramMapContext.write(new WritableImpl(columns[3]), new WritableImpl(columns[4]));
			}
		}catch(IOException e){
			e.printStackTrace();System.exit(1);
		}
		catch(NumberFormatException e){
			e.printStackTrace();
		}
	}

	public void setup() {
		// TODO Auto-generated method stub
		
	}
}
