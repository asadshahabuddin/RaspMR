/**
 * Author : Asad Shahabuddin
 * File   : RecordReader.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 31, 2015
 * Edited : Mar 31, 2015
 */

package com.rasp.fs;

/* Import list */
import java.io.IOException;
import java.io.RandomAccessFile;
import com.rasp.utils.file.FSHelpers;

public class RecordReaderImpl
    extends RecordReader {
    RandomAccessFile f;
    private long offset;
    private long key;
    private String value;
    
    @Override
    public void initialize(InputSplit inputSplit)
        throws IOException, InterruptedException {
        key = inputSplit.getOffset();
        offset = 0;
        value = "";
        f = new RandomAccessFile(FSHelpers.createFSFilePath(inputSplit.getLocation()), "r");
    }

    @Override
    public boolean nextKeyValue()
        throws IOException, InterruptedException {
        if(offset >= f.length()) {
            return false;
        }
        f.seek(offset);
        key++;
        value = f.readLine();
        offset += value.length() + 1;
        return true;
    }

    @Override
    public Long getCurrentKey()
        throws IOException, InterruptedException {
        return key;
    }

    @Override
    public String getCurrentValue()
        throws IOException, InterruptedException {
        return value;
    }

    @Override
    public void close()
        throws IOException {
        if(f != null) {
            f.close();
        }
    }
}