package com.rasp.fs.master;

import com.rasp.config.MasterConfiguration;
import raspmr.RaspMR.utils.autodiscovery.ServiceType;

import java.io.IOException;

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
        DataMaster dataMaster = new DataMaster(configuration);

        try {
            dataMaster.splitAndSend("");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




}
