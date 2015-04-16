/**
 * Author : Pulkit Jain
 * File   : MapContextImpl.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr;

import com.rasp.utils.file.FSHelpers;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class MapContextImpl implements MapContext {
    HashMap<String, FileOutputStream> keyMap;
    Map<String, Long> countMap;
    Job job;

    public MapContextImpl(Job job) {
        keyMap = new HashMap<String, FileOutputStream>();
        countMap = new HashMap<String, Long>();
        this.job = job;
    }

    public void write(Writable k, Writable v)
            throws IOException {
        if (v == null) {
            return;
        }

        String key = String.valueOf(k.getObj());
        String fileName = key + "_mout";
        byte[] value = (String.valueOf(v.getObj()) + "\n").getBytes();

        if (!keyMap.containsKey(key)) {
            countMap.put(key, 1L);
            keyMap.put(key, FSHelpers.deleteAndCreateFile(job, fileName, true));
        } else {
            countMap.put(key, countMap.get(key) + 1);
        }
        keyMap.get(key).write(value);
    }

    public void close()
            throws IOException {
        for (Map.Entry<String, FileOutputStream> entry : keyMap.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().close();
            }
        }
    }

    @Override
    public void setKeyCountMap(Map<String, Long> keyCountMap) {
        countMap = keyCountMap;
    }

    @Override
    public Map<String, Long> getKeyCountMap() {
        return countMap;
    }
}