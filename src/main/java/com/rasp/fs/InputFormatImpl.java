/**
 * Author : Asad Shahabuddin
 * File   : InputFormatImpl.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 30, 2015.
 * Edited : Apr 4, 2015.
 */

package com.rasp.fs;

/* Import list */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class InputFormatImpl
    extends InputFormat {

    static final Logger LOG = LoggerFactory.getLogger(InputFormatImpl.class);

    /* Constant(s) */
    private static final int BUFFER_SIZE = 8192;

    private int workerCount;
    private List<InputSplit> splits;
    private static int splitIdx;
    private int totalBytesRead;
    private int shift = 0;
    private String id;
    /**
     * Default constructor
     */
    public InputFormatImpl() {
        workerCount = 0;
        splitIdx = 0;
        totalBytesRead = 0;
        shift = 0;
    }

    public InputFormatImpl(String inputFile, int workerCount)
        throws InterruptedException, IOException {
        super.setInputFile(inputFile);
        id = UUID.randomUUID().toString();
        this.workerCount = workerCount;
        splitIdx = 0;
        totalBytesRead = 0;
        shift = 0;
        createSplits();  /* Create meta data for all input splits */
    }

    /**
     * Set the number of worker nodes.
     *
     * @param workerCount The number of worker nodes currently identified
     *                    by the server instance.
     */
    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    /**
     * Calculate virtual block size
     *
     * @param workerCount Number of worker nodes connected to the
     *                    server instance.
     * @return
     */
    public long blockSize(int workerCount) {
        File file = new File(getInputFile());
        if (!file.exists()) {
            return -1;
        }
        return (long) Math.ceil((float) file.length() / workerCount);
    }

    /**
     * Calculate file offset for a specific worker number
     *
     * @param blockSize Virtual block size based on input file's
     *                  size and the number of worker nodes.
     * @param workerIdx Worker node index.
     * @return
     */
    public long offset(long blockSize, int workerIdx) {
        return (blockSize * workerIdx);
    }

    @Override
    public void createSplits()
        throws IOException, InterruptedException {
        splits = new ArrayList<InputSplit>();
        long l = blockSize(workerCount);
        for (int i = 0; i < workerCount; i++) {
            splits.add(new InputSplitImpl(i, offset(l, i), l, "",id));
        }
    }

    @Override
    public List<InputSplit> getSplits() {
        return splits;
    }

    @Override
    public RecordReader createRecordReader(InputSplit split)
        throws IOException, InterruptedException {
        // TODO
        return null;
    }

    /**
     * Write an input split to the specified location.
     *
     * @param dataNode The location to write the split at.
     * @return
     */
    public boolean split(int idx, DataNode dataNode)
        throws InterruptedException, IOException {
        if (splitIdx >= workerCount) {
            return false;
        }
        
        /* Send input split to the worker node */
        dataNode.storeInputSplit((InputSplitImpl) splits.get(splitIdx));
        RandomAccessFile f = null;

        try {
            /*
            (1) Read from the split in chunks of bytes.
            (2) Write the split to the file system.
            */
            f = new RandomAccessFile(getInputFile(), "r");  /* Random access file object */
            InputSplit split = splits.get(splitIdx++);      /* The current input split */
            long flen = f.length();                         /* Length of file */
            /* Avoid creation of unnecessary empty splits */
            if (totalBytesRead == flen) {
                return false;
            }

            int bytesRead = 0;                            /* Total bytes read for the current split */
            byte[] b = null;                              /* Contiguous bytes read for a part of the current split */

            while (bytesRead < split.getLength()
                    && totalBytesRead < flen) {
                int size = BUFFER_SIZE;
                /* Last chunk of bytes for the current split */
                if ((split.getLength() - bytesRead) < BUFFER_SIZE) {
                    size = (int) (split.getLength() - bytesRead);
                    /* Last chunk of bytes for the file */
                    if ((totalBytesRead + size) >= flen) {
                        /*
                        Self-adjust number of bytes to be read to compensate
                        for fraction related approximations while calculating
                        an integer block size.
                        */
                        size = (int) (flen - totalBytesRead);
                    }
                }
                
                /*
                (1) Read a block from the input file at a specific offset.
                (2) Append block to the current input split.
                (3) Update bytes read for the current split.
                (4) Update bytes read for the current file.
                */
                f.seek(split.getOffset() + bytesRead);
                b = new byte[size];
                f.read(b, 0, size);
                dataNode.storeChunk(b);
                bytesRead += size;
                totalBytesRead += size;
            }
                
            /*
            Ensure that the current record contains the last record in its 
            entirety.
            */
            b = new byte[1];
            f.seek(totalBytesRead - 1);
            f.read(b, 0, 1);
            while (totalBytesRead < flen && b[0] != 012) {
                f.seek(totalBytesRead++);
                f.read(b, 0, 1);
                dataNode.storeChunk(b);
                shift++;
            }

            /*
            (1) Update the current split's index, length and location.
            (2) Update the next split's starting offset.
            (3) Close file system objects.
            */
            ((InputSplitImpl) split).setIdx(idx);
            ((InputSplitImpl) split).setLength(split.getLength() + shift);
            ((InputSplitImpl) split).setLocation(dataNode.getService().getIp());
            if (splitIdx < workerCount) {
                InputSplit nextSplit = splits.get(splitIdx);
                ((InputSplitImpl) nextSplit).setOffset(nextSplit.getOffset() + shift);
            }
        } catch (InterruptedException intre) {
            LOG.error("",intre);
        } catch (IOException ioe) {
            LOG.error("",ioe);
        } finally {
            if (f != null) {
                try {
                    f.close();
                    dataNode.closeInputSplit();
                } catch (IOException ioe) {
                    LOG.error("",ioe);
                }
            }
        }

        return true;
    }
    
    /* Main method for unit testing */
    /*
    public static void main(String[] args) {
        InputFormat inFormat = null;
        try {
            inFormat = new InputFormat("input/data.csv", 5);
        } catch(InterruptedException intre) {
            intre.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        inFormat.split("split1.csv");
        inFormat.split("split2.csv");
        inFormat.split("split3.csv");
        inFormat.split("split4.csv");
        inFormat.split("split5.csv");
    }
    */
}
/* End of InputFormatImpl.java */