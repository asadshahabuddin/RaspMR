package com.rasp.utils.file;

import com.rasp.mr.Job;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/15/15
 * Edited :
 */

/**
 *
 * This class contains helper methods for doing file system operations
 *
 */
public class FSHelpers {

    static final Logger LOG = LoggerFactory.getLogger(FSHelpers.class);

    private static final String MR_DIRECTORY = "mr-job";
    private static final String FS_DIRECTORY = "fs-data";
    private static final String REDUCE_FILE_IDENTIFIER = "rout";
    private static final String FILE_SEPARATOR = "/";


    /**
     * creates a file with the given fileName under a directory for the given Job(jobId)
     * the file mode will be append if the append variable is true
     * if the file exists it will be deleted
     *
     * @param job
     * @param fileName
     * @param append
     * @return the {@link java.io.FileOutputStream} for the created file
     * @throws IOException
     */
    public static FileOutputStream deleteAndCreateFile(Job job, String fileName, boolean append) throws IOException {
        FileUtils.forceMkdir(new File(createMRFilePath(job)));
        File file = FileUtils.getFile(createMRFilePath(job, fileName));
        FileUtils.deleteQuietly(file);
        return FileUtils.openOutputStream(file,append);
    }

    /**
     * creates a file with the given fileName under a directory for the {@link com.rasp.utils.file.FSHelpers#FS_DIRECTORY }
     * the file mode will be append if the append variable is true
     * if the file exists it will be deleted
     *
     * @param fileName
     * @param append
     * @return the {@link java.io.FileOutputStream} for the created file
     * @throws IOException
     */
    public static FileOutputStream deleteAndCreateFile(String fileName, boolean append) throws IOException {
        FileUtils.forceMkdir(new File(FS_DIRECTORY));
        File file = FileUtils.getFile(createFSFilePath(fileName));
        FileUtils.deleteQuietly(file);
        return FileUtils.openOutputStream(file,append);
    }


    /**
     * creates a {@link java.io.RandomAccessFile} with the given fileName under a directory for the given Job(jobId)
     * the file mode will be append if the append variable is true
     *
     * @param job
     * @param fileName
     * @param mode
     * @return {@link java.io.RandomAccessFile} for the created file
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openFile(Job job, String fileName, String mode) throws FileNotFoundException {
        return new RandomAccessFile(createMRFilePath(job, fileName), mode);
    }

    /**
     * @param job
     * @param fileName
     * @return the MR-path for the given file under the given jobs directory
     */
    private static String createMRFilePath(Job job, String fileName){
        return createMRFilePath(job)+ FILE_SEPARATOR +fileName;
    }

    /**
     * @param job
     * @return the path for the given jobs directory
     */
    private static String createMRFilePath(Job job){
        return MR_DIRECTORY + FILE_SEPARATOR + job.getJobId() ;
    }

    /**
     * @param fileName
     * @return the path for filename under the filesystem
     */
    public static String createFSFilePath(String fileName){
        return FS_DIRECTORY + FILE_SEPARATOR + fileName;
    }

    /**
     * returns all the files under the job directory that contain the
     * given key in the file name and does not include the text
     * {@link com.rasp.utils.file.FSHelpers#REDUCE_FILE_IDENTIFIER} in its name
     *
     * @param key
     * @param job
     * @return {@link List<File>} list of files
     */
    public static List<File> getFilesFor(String key, Job job){
        File[] files = new File(createMRFilePath(job)).listFiles();
        LOG.debug("no of files in dir : " + files.length);
        List<File> fileList = new ArrayList<File>();
        for(File file : files) {
            if(file == null){
                LOG.error("file is null");
            }else{
                String name = file.getName();
                if(name.contains(key + "_") && !name.contains(REDUCE_FILE_IDENTIFIER)){
                    fileList.add(file);
                }
            }

        }
        return fileList;
    }

    /**
     * deletes all the files under the given jobs directory except the files
     * containing {@link com.rasp.utils.file.FSHelpers#REDUCE_FILE_IDENTIFIER} in its name
     *
     * @param job
     */
    public static void deleteFilesForJob(Job job){
        File[] files = new File(createMRFilePath(job)).listFiles();
        LOG.info("no of files in dir : " + files.length);
        for(File file : files) {
            if(file == null){
                LOG.error("file is null");
            }else{
                String name = file.getName();
                if(!name.contains(REDUCE_FILE_IDENTIFIER)){
                    file.delete();
                }
            }

        }
    }


}
