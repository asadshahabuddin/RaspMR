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
        f = new RandomAccessFile(inputSplit.getLocation(), "r");
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
    public float getProgress()
        throws IOException, InterruptedException {
        // TODO
        return 0;
    }

    @Override
    public void close()
        throws IOException {
        if(f != null) {
            f.close();
        }
    }
    
    /* Main method for unit testing */
    /*
    public static void main(String[] args) {
        try {
            RecordReader reader = new RecordReader();
            reader.initialize(new com.rasp.fs.InputSplit(0, 8192, "test.txt"), null);
            while(reader.nextKeyValue()) {
                System.out.println("Key  : " + reader.getCurrentKey());
                System.out.println("Value: " + reader.getCurrentValue());
            }
            reader.close();
        } catch(InterruptedException intre) {
            intre.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    */
}
/* End of RecordReader.java */