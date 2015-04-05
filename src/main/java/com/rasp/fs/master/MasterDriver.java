package com.rasp.fs.master;

import com.rasp.config.MasterConfiguration;
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

    public static void main(String[] args) {

        MasterConfiguration configuration = new MasterConfiguration(9292, ServiceType.JOB_TRACKER);
        DataMasterImpl dataMaster = new DataMasterImpl(configuration);
        configuration.setDataMaster(dataMaster);

        while(true){

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if(input.equalsIgnoreCase("send")){
                try {
                    dataMaster.splitAndSend("/Users/rahulmadhavan/Documents/developer/ms/parallel/assignments/a3/a3data/test");
                } catch (IOException e) {
                    e.printStackTrace();
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
