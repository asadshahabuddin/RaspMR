/**
 * Author : Pulkit Jain
 * File   : MapContextImpl.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class MapContextImpl implements MapContext {
    HashMap<String, FileOutputStream> keyMap;
    Map<String, Long> countMap;

    public MapContextImpl() {
        keyMap = new HashMap<String, FileOutputStream>();
        countMap = new HashMap<String, Long>();
    }

    public void write(Writable k, Writable v)
            throws IOException {
        if (v == null) {
            return;
        }
        String key = String.valueOf(k.getObj());
        byte[] value = (String.valueOf(v.getObj()) + "\n").getBytes();
        File file = new File(String.valueOf(key));
        if (!keyMap.containsKey(key)) {
            if (file.exists()) {
                file.delete();
            }
            countMap.put(key, 1L);
            keyMap.put(key, new FileOutputStream(key + "_mout", true));
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