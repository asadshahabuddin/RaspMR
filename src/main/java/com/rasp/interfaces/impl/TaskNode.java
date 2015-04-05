package com.rasp.interfaces.impl;

import com.rasp.interfaces.Task;
import raspmr.RaspMR.utils.autodiscovery.Service;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/4/15
 * Edited :
 */
public interface TaskNode {

    void sendTask(Task task);

    Service getService();

}