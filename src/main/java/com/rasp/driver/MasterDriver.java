package com.rasp.driver;

import com.rasp.config.MasterConfiguration;
import com.rasp.fs.DataMaster;
import com.rasp.fs.master.DataMasterImpl;
import com.rasp.mr.Job;
import com.rasp.mr.JobTracker;
import com.rasp.mr.master.JobImpl;
import com.rasp.mr.master.JobScheduler;
import com.rasp.mr.master.JobTrackerImpl;
import com.rasp.mr.slave.TestMapper;
import com.rasp.utils.autodiscovery.ServiceType;

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
        JobScheduler jobScheduler = new JobScheduler(jobTracker);

        configuration.setDataMaster(dataMaster);
        configuration.setJobTracker(jobTracker);
        configuration.setJobScheduler(jobScheduler);

        Thread jobSchedulerThread = new Thread(jobScheduler);
        jobSchedulerThread.start();

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
