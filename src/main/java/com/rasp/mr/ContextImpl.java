/**
 * Author : Pulkit Jain
 * File   : Writable.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr;

/* Import list */

import com.rasp.mr.slave.WritableImpl;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class ContextImpl implements Context {
    HashMap<String, FileOutputStream> keyMap;
    HashMap<String, Long> countMap;

    public ContextImpl() {
        keyMap = new HashMap<String, FileOutputStream>();
        countMap = new HashMap<String, Long>();
    }

    public void write(WritableImpl k, WritableImpl v)
            throws IOException{
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
            countMap.put(key, (long)1);
            keyMap.put(key, new FileOutputStream(key, true));
        }
        else{
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
        writeHashMap();
    }

    public void writeHashMap()
            throws IOException {
        File dictionaryFile = new File("masterKey");
        BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFile));
        Iterator<String> it = countMap.keySet().iterator();
        String header = "Key" + "\t" + "KeyCount" + "\n";
        writer.write(header);
        while (it.hasNext()){
            String key = it.next();
            String entryLine = key + "\t" + countMap.get(key) + "\n";
            writer.write(entryLine);
        }
        writer.close();
    }

    public static void main(String[] args)
            throws IOException {
//        ContextImpl cimpl = new ContextImpl();
//        cimpl.write(new WritableImpl(1), new WritableImpl("asad"));
//        cimpl.write(new WritableImpl(2), new WritableImpl("pulkit"));
//        cimpl.write(new WritableImpl(3), new WritableImpl("pulkit"));
//        cimpl.write(new WritableImpl(1), new WritableImpl("pulkit"));
//        cimpl.close();
    }
}
