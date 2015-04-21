/**
 * Author : Asad Shahabuddin
 * File   : Writable.java
 * Email  : asad808@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr;

public interface Writable {
    /**
     * Get the writable object.
     *
     * @return The writable object.
     */
    public Object getObj();

    /**
     * Get the object type.
     *
     * @return The object type.
     */
    public int getType();
}