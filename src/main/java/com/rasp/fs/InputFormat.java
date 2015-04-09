/**
 * Author : Asad Shahabuddin
 * File   : InputFormat.java
 * Email  : asad808@ccs.neu.edu
 * Created: Mar 24, 2015
 * Edited : Mar 24, 2015
 */

package com.rasp.fs;

/* Import list */
import java.util.List;
import java.io.IOException;

/**
 * <code>InputFormat</code> describes the input-specification for a Map-Reduce
 * job.
 * 
 * <p>
 * The Map-Reduce framework relies on the <code>InputFormat</code> of the job
 * to:
 * <p>
 * <ol>
 * <li>
 * Validate the input-specification of the job.
 * <li>
 * Split-up the input file(s) into logical {@link com.rasp.fs.InputSplit}s, each of which is
 * then assigned to an individual {@link com.rasp.mr.Mapper}.</li>
 * <li>
 * Provide the {@link RecordReader} implementation to be used to glean input
 * records from the logical <code>InputSplit</code> for processing by the
 * {@link com.rasp.mr.Mapper}.</li>
 * </ol>
 * 
 * <p>
 * The default behavior of file-based {@link InputFormat}s, typically
 * sub-classes of {@link FileInputFormat}, is to split the input into
 * <i>logical</i> {@link com.rasp.fs.InputSplit}s based on the total size, in bytes, of the
 * input files.
 * </p>
 */
public abstract class InputFormat {
    private String inputFile;

    /**
     * Set input file
     * @param inputFile
     */
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
    
    /**
     * Get input file
     * @return
     */
    public String getInputFile() {
        return inputFile;
    }

    public abstract List<InputSplit> getSplits();

    /**
     * Logically split the set of input files for the job.
     *
     * <p>
     * Each {@link InputSplit} is then assigned to an individual {@link com.rasp.mr.Mapper}
     * for processing.
     * </p>
     *
     * <p>
     * <i>Note</i>: The split is a <i>logical</i> split of the inputs and the
     * input files are not physically split into chunks. For e.g. a split could
     * be <i>&lt;input-file-path, start, offset&gt;</i> tuple. The InputFormat
     * also creates the {@link RecordReader} to read the {@link InputSplit}.
     */
    public abstract void createSplits()
        throws IOException, InterruptedException;

    /**
     * Create a record reader for a given split. The framework will call
     * {@link RecordReader#initialize(InputSplit, com.rasp.mr.MapContext)} before
     * the split is used.
     * 
     * @param split
     *            the split to be read
     * @return a new record reader
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract RecordReader createRecordReader(InputSplit split)
        throws IOException, InterruptedException;
}
/* End of InputFormat.java */