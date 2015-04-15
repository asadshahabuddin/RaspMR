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

public class MasterDriver {
    /* Constant(s) */
    private static final String INPUT_PATH =
            "/Users/rahulmadhavan/Documents/developer/ms/parallel/assignments/a3/a3data/test";

    public static void main(String[] args) throws IOException {
        MasterConfiguration configuration = new MasterConfiguration(9292, ServiceType.JOB_TRACKER);
        DataMaster dataMaster = new DataMasterImpl(configuration);
        JobTracker jobTracker = new JobTrackerImpl(configuration);
        JobScheduler jobScheduler = new JobScheduler(jobTracker);
        JobNode jobServer = new JobNodeServerImpl(configuration);


        configuration.setDataMaster(dataMaster);
        configuration.setJobTracker(jobTracker);
        configuration.setJobScheduler(jobScheduler);
        configuration.setJobServer(jobServer);

        Thread jobSchedulerThread = new Thread(jobScheduler);
        jobSchedulerThread.start();


        STaskProtos.JobService.BlockingInterface jobService
                = new JobNodeBlockingService(configuration.getJobServer());
        BlockingService js = STaskProtos.JobService.newReflectiveBlockingService(jobService);
        ProtoServer.startServer(configuration.getJobServer().getService(), js);

        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            Job job = new JobImpl();
            job.setInputPath(args[0]);
            job.setMapper(TestMapper.class);
            job.setReducer(TestReducer.class);

            if(input.equalsIgnoreCase("send")) {
                try {
                    dataMaster.splitAndSend(args[0]);
                    jobTracker.submit(job);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if(input.equalsIgnoreCase("exit")) {
                System.exit(0);
            } else {
                if(!input.trim().equalsIgnoreCase("\n")) {
                    System.out.println("meh...");
                }
            }
        }
    }
}
/* End of MasterDriver.java */