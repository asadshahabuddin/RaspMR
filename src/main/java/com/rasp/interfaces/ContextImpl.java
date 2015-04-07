/**
 * Author : Pulkit Jain
 * File   : Writable.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.interfaces;

/* Import list */

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public abstract class ContextImpl implements Context{
    HashMap<String, FileOutputStream> keyMap;

    public ContextImpl(){
        keyMap = new HashMap<String, FileOutputStream>();
    }

    public void write(WritableImpl k, WritableImpl v)
        throws FileNotFoundException, IOException {
        if(v == null)
        {
            return;
        }
        String key = String.valueOf(k);
        byte[] value = (String.valueOf(v) + "\n").getBytes();
        File file = new File(String.valueOf(key));
        if (!keyMap.containsKey(key))
        {
            keyMap.put(key, new FileOutputStream(file, true));
            if(file.exists()) {
                file.delete();
            }
        }
        keyMap.get(key).write(value);
    }

    public void close()
        throws IOException{
        for(Map.Entry<String, FileOutputStream> entry : keyMap.entrySet())
        {
            if(entry.getValue() != null)
            {
                entry.getValue().close();
            }
        }
    }
}
