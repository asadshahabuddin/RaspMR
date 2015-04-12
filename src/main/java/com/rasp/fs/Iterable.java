/**
 * Author : Asad Shahabuddin
 * File   : Iterable.java
 * Email  : asad808@ccs.neu.edu
 * Created: Apr 12, 2015
 * Edited : Apr 12, 2015
 */

package com.rasp.fs;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

public class Iterable
    implements Iterator {
    private BufferedReader reader;
    private String cachedLine;
    private boolean finished;

    /**
     * Constructor
     * @param reader
     *            Reader object for the input file.
     */
    public Iterable(Reader reader) {
        if(reader == null) {
            throw new IllegalArgumentException("  [error] Reader object cannot be null");
        }
        if(reader instanceof BufferedReader) {
            this.reader = (BufferedReader) reader;
        } else {
            this.reader = new BufferedReader(reader);
        }
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
                    close();
                    return false;
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
    public Object next() {
        if(!hasNext()) {
            throw new NoSuchElementException("  [error] Reached EOF");
        }
        String curLine = cachedLine;
        cachedLine = null;
        return curLine;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("  [error] This operation is not supported");
    }

    public void close() {
        cachedLine = null;
        finished   = true;
        try {
            reader.close();
        } catch(IOException ioe) {
            throw new IllegalStateException(ioe.toString());
        }
    }
}