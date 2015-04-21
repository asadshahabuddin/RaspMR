/**
 * Author : Rahul Madhavan
 * File   : MasterDriver.java
 * Email  : rahulk@ccs.neu.edu
 * Created: Apr 3, 2015
 * Edited : Apr 8, 2015
 */

package com.rasp.driver;

/* Import list */
import com.google.protobuf.BlockingService;
import com.rasp.config.Configuration;
import com.rasp.fs.DataNode;
import com.rasp.fs.SInputSplitProtos;
import com.rasp.fs.slave.DataNodeServerImpl;
import com.rasp.fs.slave.DataTransferBlockingService;
import com.rasp.mr.Job;
import java.util.Scanner;
import java.io.IOException;
import com.rasp.fs.DataMaster;
import com.rasp.mr.JobNode;
import com.rasp.mr.JobTracker;
import com.rasp.mr.STaskProtos;
import com.rasp.mr.master.*;
import com.rasp.mr.slave.TaskBlockingService;
import com.rasp.mr.slave.TestMapper;
import com.rasp.fs.master.DataMasterImpl;
import com.rasp.config.MasterConfiguration;
import com.rasp.mr.slave.TestReducer;
import com.rasp.utils.autodiscovery.ServiceType;
import com.rasp.utils.protobuf.ProtoServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * represents the driver class (with the main method )for the master node
 *
 */
public class MasterDriver {
    /* Constant(s) */

    static final Logger LOG = LoggerFactory.getLogger(MasterDriver.class);
    
    public static void main(String[] args) throws IOException {

        
        MasterConfiguration configuration = new MasterConfiguration(Configuration.JOB_NODE_PORT, ServiceType.JOB_TRACKER);
        DataMaster dataMaster = new DataMasterImpl(configuration);
        JobTracker jobTracker = new JobTrackerImpl(configuration);
        JobScheduler jobScheduler = new JobScheduler(jobTracker);
        JobNode jobServer = new JobNodeServerImpl(configuration);


        /**
         * set the implementations for the various classes
         */
        configuration.setDataMaster(dataMaster);
        configuration.setJobTracker(jobTracker);
        configuration.setJobScheduler(jobScheduler);
        configuration.setJobServer(jobServer);
        configuration.setCleanup(false);


        /**
         * initialize and start the job scheduler
         */
        Thread jobSchedulerThread = new Thread(jobScheduler);
        jobSchedulerThread.start();

        /**
         * initialize and start the job JobNode
         */
        STaskProtos.JobService.BlockingInterface jobService
                = new JobNodeBlockingService(configuration.getJobServer());
        BlockingService js = STaskProtos.JobService.newReflectiveBlockingService(jobService);
        ProtoServer.startServer(configuration.getJobServer().getService(), js);


        /**
         * register the job factories
         */
        JobFactoryRegistry.register(TestJobFactory.class);
        JobFactoryRegistry.register(AvgJobFactory.class);

        /**
         * scan user input
         */
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String [] inputWords = input.trim().split(" ");

            if(inputWords.length > 0){
                String firstWord = inputWords[0];
                if(firstWord.equalsIgnoreCase("run")) {
                    if(inputWords.length == 3){
                        try {
                            long startTime = System.currentTimeMillis();
                            dataMaster.splitAndSend(inputWords[2]);
                            LOG.info("Split Time: " +
                                    (System.currentTimeMillis() - startTime) / 1000f);
                            startTime = System.currentTimeMillis();
                            jobTracker.submit(JobFactoryRegistry.createJob(inputWords[1], inputWords[2]));
                            LOG.info("Job Run Time: " +
                                    (System.currentTimeMillis() - startTime) / 1000f);
                        } catch (InterruptedException e) {
                            System.out.println("error - check logs");
                            LOG.error("",e);
                        }
                    }else{
                        System.out.println("run JobType inputFile");
                    }
                } else if(firstWord.equalsIgnoreCase("exit")) {
                    System.exit(0);
                } else {
                    System.out.println("I am too dumb to figure what you are trying to say");
                }

            }
        }
    }
}
/* End of MasterDriver.java */