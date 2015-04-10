package com.rasp.mr;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/9/15
 * Edited :
 */
public interface ShuffleTask extends Task{

    void setKey(String key);

    String getKey();

}
