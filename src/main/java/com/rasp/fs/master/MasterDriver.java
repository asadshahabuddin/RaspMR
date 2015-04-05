package com.rasp.fs.master;

import com.rasp.config.MasterConfiguration;
import com.rasp.fs.DataMaster;
import com.rasp.interfaces.Job;
import com.rasp.interfaces.JobTracker;
import com.rasp.interfaces.MapperTask;
import com.rasp.interfaces.impl.JobImpl;
import com.rasp.interfaces.impl.JobTrackerImpl;
import com.rasp.interfaces.impl.TestMapper;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

import java.io.IOException;
import java.util.Scanner;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/3/15
 * Edited :
 */
public class MasterDriver {

    public static void main(String[] args) throws IOException {

        MasterConfiguration configuration = new MasterConfiguration(9292, ServiceType.JOB_TRACKER);
        DataMaster dataMaster = new DataMasterImpl(configuration);
        JobTracker jobTracker = new JobTrackerImpl(configuration);

        configuration.setDataMaster(dataMaster);
        configuration.setJobTracker(jobTracker);

        while(true){

            String inputPath = "/Users/rahulmadhavan/Documents/developer/ms/parallel/assignments/a3/a3data/test";
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            Job job = new JobImpl();
            job.setInputPath(inputPath);
            job.setMapper(TestMapper.class);

            if(input.equalsIgnoreCase("send")){
                try {
                    dataMaster.splitAndSend(inputPath);
                    jobTracker.submit(job);
                    Job job1 = jobTracker.nextJob();
                    jobTracker.createMapperTasksForJob(job);
                    for(MapperTask task : job1.getMapTasks()){
                        jobTracker.sendTask(task);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(input.equalsIgnoreCase("exit")){
                System.exit(0);
            }else{
                if(!input.trim().equalsIgnoreCase("\n"))
                    System.out.println("meh...");
            }

        }



        //System.out.println("done");
    }




}
