/**
 * Author : Asad Shahabuddin
 * File   : InputFormat.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 30, 2015.
 * Edited : Mar 30, 2015.
 */

package com.rasp.fs;

/* Import list */
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import com.rasp.interfaces.InputSplit;
import com.rasp.interfaces.RecordReader;
import raspmr.RaspMR.utils.autodiscovery.Service;

public class InputFormat
    extends com.rasp.interfaces.InputFormat
{
    /* Constant(s) */
    private static final int BUFFER_SIZE = 8192;
    
    private int workerCount;
    private List<InputSplit> splits;
    private static int splitIdx;
    private int totalBytesRead;
    private int shift = 0;
    
    /**
     * Default constructor
     */
    public InputFormat()
    {
        workerCount    = 0;
        splitIdx       = 0;
        totalBytesRead = 0;
        shift           = 0;
    }
    
    public InputFormat(String inputFile, int workerCount)
        throws InterruptedException, IOException
    {
        super.setInputFile(inputFile);
        this.workerCount = workerCount;
        splitIdx         = 0;
        totalBytesRead   = 0;
        shift            = 0;
        getSplits();  /* Create meta data for all input splits */
    }
    
    /**
     * Set the number of worker nodes.
     * @param workerCount
     *            The number of worker nodes currently identified
     *            by the server instance.
     */
    public void setWorkerCount(int workerCount)
    {
        this.workerCount = workerCount;
    }
    
    /**
     * Calculate virtual block size
     * @param workerCount
     *            Number of worker nodes connected to the 
     *            server instance.
     * @return
     */
    public long blockSize(int workerCount)
    {
        File file = new File(getInputFile());
        if(!file.exists())
        {
            return -1;
        }
        return (long) Math.ceil((float)file.length() / workerCount);
    }
    
    /**
     * Calculate file offset for a specific worker number
     * @param blockSize
     *            Virtual block size based on input file's
     *            size and the number of worker nodes.
     * @param workerIdx
     *            Worker node index.
     * @return
     */
    public long offset(long blockSize, int workerIdx)
    {
        return (blockSize * workerIdx);
    }
    
    @Override
    public void getSplits() throws IOException,
        InterruptedException
    {
        splits = new ArrayList<InputSplit>();
        long l = blockSize(workerCount);
        for(int i = 0; i < workerCount; i++)
        {
            splits.add(new com.rasp.fs.InputSplit(i, offset(l, i), l, ""));
        }
    }

    @Override
    public RecordReader createRecordReader(InputSplit split)
        throws IOException, InterruptedException
    {
        // TODO
        return null;
    }

    /**
     * Write an input split to the specified location.
     * @param dataNode
     *            The location to write the split at.
     * @return
     */
    public boolean split(int idx, DataNode dataNode)
        throws InterruptedException, IOException
    {
        if(splitIdx >= workerCount)
        {
            return false;
        }

        /* Send input split to the worker node */
        dataNode.storeInputSplit((com.rasp.fs.InputSplit) splits.get(splitIdx));
        
        RandomAccessFile fin = null;
        FileOutputStream fout = null;
        
        try
        {
            /*
            (1) Read from the split in chunks of bytes.
            (2) Write the split to the file system.
            */
            fin = new RandomAccessFile(getInputFile(), "r");  /* Random access file object */
            InputSplit split = splits.get(splitIdx++);        /* The current input split */
            long flen = fin.length();                         /* Length of file */
            /* Avoid creation of unnecessary empty splits */
            if(totalBytesRead == flen)
            {
                return false;
            }
                
            //fout = new FileOutputStream(location, true);  /* File output stream */
            int bytesRead = 0;                            /* Total bytes read for the current split */
            byte[] b = null;                              /* Contiguous bytes read for a part of the current split */
                
            while(bytesRead < split.getLength()
                  && totalBytesRead < flen)
            {
                int size = BUFFER_SIZE;
                /* Last chunk of bytes for the current split */
                if((split.getLength() - bytesRead) < BUFFER_SIZE)
                {
                    size = (int) (split.getLength() - bytesRead);
                    /* Last chunk of bytes for the file */
                    if((totalBytesRead + size) >= flen)
                    {
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
                fin.seek(split.getOffset() + bytesRead);
                b = new byte[size];
                fin.read(b, 0, size);
                //
                //fout.write(b);
                dataNode.storeChunk(b);
                bytesRead += size;
                totalBytesRead += size;
            }
                
            /*
            Ensure that the current record contains the last record in its 
            entirety.
            */ 
            b = new byte[1];
            fin.seek(totalBytesRead - 1);
            fin.read(b, 0, 1);
            while(totalBytesRead < flen && b[0] != 012)
            {
                fin.seek(totalBytesRead++);
                fin.read(b, 0, 1);
                //fout.write(b);
                dataNode.storeChunk(b);
                shift++;
            }
            
            /*
            (1) Update the current split's index, length and location.
            (2) Update the next split's starting offset.
            (3) Close file system objects.
            */
            ((com.rasp.fs.InputSplit) split).setIdx(idx);
            ((com.rasp.fs.InputSplit) split).setLength(split.getLength() + shift);
            ((com.rasp.fs.InputSplit) split).setLocation(dataNode.getService().getIp());
            if(splitIdx < workerCount)
            {
                InputSplit nextSplit = splits.get(splitIdx);
                ((com.rasp.fs.InputSplit) nextSplit).setOffset(nextSplit.getOffset() + shift);
            }
        }
        catch(InterruptedException intre)
        {
            intre.printStackTrace();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally
        {
            if(fout != null)
            {
                try
                {
                    fout.close();
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
            if(fin != null)
            {
                try
                {
                    fin.close();
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        
        return true;
    }
    
    /* Main method for unit testing */
    /*
    public static void main(String[] args)
    {
        InputFormat inFormat = null;
        try
        {
            inFormat = new InputFormat("input/data.csv", 5);
        }
        catch(InterruptedException intre)
        {
            intre.printStackTrace();
        }
        catch(IOException ioe)
        {
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
/* End of InputFormat.java */