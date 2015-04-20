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
public class FSHelpers {

    static final Logger LOG = LoggerFactory.getLogger(FSHelpers.class);

    private static final String MR_DIRECTORY = "mr-job";
    private static final String FS_DIRECTORY = "fs-data";

    public static FileOutputStream deleteAndCreateFile(Job job, String fileName, boolean append) throws IOException {
        FileUtils.forceMkdir(new File(createMRFilePath(job)));
        File file = FileUtils.getFile(createMRFilePath(job, fileName));
        FileUtils.deleteQuietly(file);
        return FileUtils.openOutputStream(file,append);
    }

    public static FileOutputStream deleteAndCreateFile(String fileName, boolean append) throws IOException {
        FileUtils.forceMkdir(new File(FS_DIRECTORY));
        File file = FileUtils.getFile(createFSFilePath(fileName));
        FileUtils.deleteQuietly(file);
        return FileUtils.openOutputStream(file,append);
    }


    public static RandomAccessFile openFile(Job job, String fileName, String mode) throws FileNotFoundException {
        return new RandomAccessFile(createMRFilePath(job, fileName), mode);
    }

    private static String createMRFilePath(Job job, String fileName){
        return createMRFilePath(job)+ "/" +fileName;
    }

    private static String createMRFilePath(Job job){
        return MR_DIRECTORY + "/" + job.getJobId() ;
    }

    public static String createFSFilePath(String fileName){
        return FS_DIRECTORY + "/" + fileName;
    }

    public static List<File> getFilesFor(String key, Job job){
        File[] files = new File(createMRFilePath(job)).listFiles();
        LOG.debug("no of files in dir : " + files.length);
        List<File> fileList = new ArrayList<File>();
        for(File file : files) {
            if(file == null){
                System.out.println("file is null");
            }else{
                String name = file.getName();
                if(name.contains(key + "_") && !name.contains("rout")){
                    fileList.add(file);
                }
            }

        }
        return fileList;
    }

    public static void deleteFilesForJob(Job job){
        File[] files = new File(createMRFilePath(job)).listFiles();
        LOG.info("no of files in dir : " + files.length);
        for(File file : files) {
            if(file == null){
                System.out.println("file is null");
            }else{
                String name = file.getName();
                if(!name.contains("rout")){
                    file.delete();
                }
            }

        }
    }


}
