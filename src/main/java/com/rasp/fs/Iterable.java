/**
 * Author : Asad Shahabuddin
 * File   : Iterable.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 12, 2015
 * Edited : Apr 12, 2015
 */

package com.rasp.fs;

import com.rasp.mr.Job;
import com.rasp.mr.Writable;
import com.rasp.mr.slave.WritableImpl;
import com.rasp.utils.file.FSHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Iterable
    implements Iterator<Writable> {

    static final Logger LOG = LoggerFactory.getLogger(Iterable.class);

    private List<File> fileList;
    private BufferedReader reader;
    private int fileIdx;
    private String cachedLine;
    private boolean finished;

    /**
     * Constructor
     * @param key
     *            The prefix to the names of all relevant files.
     */
    public Iterable(String key, Job job)
        throws FileNotFoundException {
        String dirName = System.getProperty("user.dir");
        LOG.debug("dir - name : " + dirName);
        fileList = FSHelpers.getFilesFor(key,job);
        if(fileList.size() == 0) {
            throw new IllegalArgumentException("  [error] No files detected for key '" + key + "'");
        }
        reader = new BufferedReader(new FileReader(fileList.get(fileIdx++)));
    }

    @Override
    public boolean hasNext() {
        if(cachedLine != null) {
            return true;
        } else if(finished) {
            return false;
        } else {
            try {
                cachedLine = reader.readLine();
                if(cachedLine == null) {
                    if(fileIdx == fileList.size()) {
                        close();
                        return false;
                    } else {
                        reader.close();
                        reader = new BufferedReader(new FileReader(fileList.get(fileIdx++)));
                        return hasNext();
                    }
                } else {
                    return true;
                }
            } catch(IOException ioe) {
                close();
                throw new IllegalStateException(ioe.toString());
            }
        }
    }

    @Override
    public Writable next() {
        if(!hasNext()) {
            throw new NoSuchElementException("  [error] Reached EOF");
        }
        String curLine = cachedLine;
        cachedLine = null;
        return new WritableImpl(curLine);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("  [error] This operation is not supported");
    }

    public void close() {
        cachedLine = null;
        finished = true;
        try {
            reader.close();
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe.toString());
        }
    }
}