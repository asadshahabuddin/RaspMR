/**
 * Author : Pulkit Jain
 * File   : ReduceContextImpl.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 12, 2015
 * Edited : Apr 12, 2015
 */

package com.rasp.mr;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;

public class ReduceContextImpl implements ReduceContext {
    FileOutputStream fop = null;
    FileInputStream fin = null;

    @Override
    public void write(Writable k, Writable v) throws IOException {

        // Check for null key

        if (v == null) {
            return;
        }
        String key = String.valueOf(k.getObj());
        byte[] value = (String.valueOf(v.getObj()) + "\n").getBytes();
        File file = new File(String.valueOf(key) + "-rout");
        if(!file.exists())
        {
            file.createNewFile();
        }
        fop.write(value);
    }

    @Override
    public void close() throws IOException {
        if(fop!= null){
            fop.close();
        }
    }

    @Override
    public Pair<Writable, Writable> read() throws IOException {
        int content;
        while ((content = fin.read()) != -1) {
            // convert to char and display it
            System.out.print((char) content);
        }
        return null;
    }
}

