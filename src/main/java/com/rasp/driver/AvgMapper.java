package com.rasp.driver;

import java.io.IOException;

import com.rasp.mr.MapContext;
import com.rasp.mr.Mapper;
import com.rasp.mr.slave.WritableImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom mapper implementation for calculating averages program
 * Author : Sourabh Suman
 * File   : AvgMapper.java
 * Email  : sourabhs@ccs.neu.edu
 * Created: 4/19/15
 * Edited :
 */
public class AvgMapper implements Mapper{

    static final Logger LOG = LoggerFactory.getLogger(AvgMapper.class);

    @Override
	public void cleanup() {		
		
	}

	@Override
	public void map(Long paramLong, String paramString,
			MapContext paramMapContext) {
		
		String[] columns = paramString.split("\t");
		try{
			if (columns.length>=5) {
				paramMapContext.write(new WritableImpl(columns[3]), new WritableImpl(columns[4]));
			}
		}catch(IOException e){
            LOG.error("",e);
			System.exit(1);
		}
		catch(NumberFormatException e){
            LOG.error("",e);
		}
	}

	@Override
	public void setup() {
		
	}
}
