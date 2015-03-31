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

public class InputFormat
    extends com.rasp.interfaces.InputFormat
{
    /* Constant(s) */
    private static final int BUFFER_SIZE = 8192;
    
    private int workerCount;
    
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
    public List<InputSplit> getSplits() throws IOException,
        InterruptedException
    {
        List<InputSplit> splits = new ArrayList<InputSplit>();
        long l = blockSize(workerCount);
        for(int i = 0; i < workerCount; i++)
        {
            splits.add(new com.rasp.fs.InputSplit(offset(l, i), l, ""));
        }
        return splits;
    }

    @Override
    public RecordReader createRecordReader(InputSplit split)
        throws IOException, InterruptedException
    {
        // TODO
        return null;
    }
    
    /**
     * Write splits to the file system.
     */
    public void split()
    {
        try
        {
            int idx = 1;                                                     /* Split number being processed */ 
            List<InputSplit> splits = getSplits();                           /* Get a list of input splits */
            RandomAccessFile f = new RandomAccessFile(getInputFile(), "r");  /* Random access file object */
            FileOutputStream fout = null;                                    /* File output stream */
            int totalBytesRead = 0;                                          /* Total number of bytes read */
            
            /*
            (1) Process each split one after the other.
            (2) Read from each split in chunks of bytes.
            (3) Write each split to the file system.
            */
            for(InputSplit split : splits)
            {
                long flen = f.length();                        /* Length of file */
                int bytesRead = 0;                             /* Total bytes read for the current split */
                byte[] b = new byte[(int) split.getLength()];  /* Bytes read from the current split */
                f.seek(split.getOffset());
                
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
                            (1) Self-adjust number of bytes to be read to compensate
                                for fraction related approximations while calculating
                                an integer block size.
                            (2) Resize byte array to avoid writing '0' valued bytes.
                            */
                            size = (int) (f.length() - totalBytesRead);
                            b = new byte[size];
                        }
                    }
                    
                    /*
                    (1) Read a block from the input file at a specific offset
                    (2) Update bytes read for the current split
                    (3) Update bytes read for the current file
                    */ 
                    f.read(b, bytesRead, size);
                    bytesRead += size;
                    totalBytesRead += bytesRead;
                }
                
                /* Write the current split to file system */ 
                fout = new FileOutputStream("split" + idx++ + ".txt");
                fout.write(b);
                fout.close();
            }
            f.close();
        }
        catch(InterruptedException intre)
        {
            intre.printStackTrace();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    
    /* Main method for unit testing */
    /*
    public static void main(String[] args)
    {
        InputFormat inFormat = new InputFormat();
        inFormat.setInputFile("input/data.txt");
        inFormat.setWorkerCount(10);
        inFormat.split();
    }
    */  
}
/* End of InputFormat.java */