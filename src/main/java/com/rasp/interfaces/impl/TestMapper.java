package com.rasp.interfaces.impl;

import com.rasp.interfaces.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/5/15
 * Edited :
 */
public class TestMapper implements Mapper<Long,String>{

    @Override
    public void setup() {

    }

    @Override
    public Map<Long, List<String>> map(Long key, String value) {
        return null;
    }

    @Override
    public void cleanup() {

    }
}