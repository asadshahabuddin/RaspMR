/**
 * Author : Pulkit Jain
 * File   : WritableImpl.java
 * Email  : jain.pul@husky.neu.edu
 * Created: Apr 6, 2015
 * Edited : Apr 6, 2015
 */

package com.rasp.mr.slave;

/* Import list */

import com.rasp.mr.Writable;

import java.io.Serializable;


public class WritableImpl
        implements Serializable, Writable {
    /* Serial ID and constants */
    private static final long serialVersionUID = 1L;
    public static final int TYPE_NULL = 0;
    public static final int TYPE_INTEGER = 1;
    public static final int TYPE_STRING = 2;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_DOUBLE = 4;
    public static final int TYPE_LONG = 5;
    public static final int TYPE_OTHER = 6;

    private Object obj;
    private Integer type;


    /**
     * Constructor
     *
     * @param obj
     */
    public WritableImpl(Object obj) {

        this.obj = obj;
        type = TYPE_NULL;
    }


    /**
     * TODO - Write a proper description.
     *
     * @param obj
     * @param type
     */
    public WritableImpl(Object obj, Integer type) {
        this.obj = obj;
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public int getType() {
        if (obj == null) {
            return TYPE_NULL;
        }
        if (type != TYPE_NULL) {
            return type;
        }

        switch (obj.getClass().toString()) {
            case "class java.lang.Integer":
                type = TYPE_INTEGER;
                break;
            case "class java.lang.String":
                type = TYPE_STRING;
                break;
            case "class java.lang.Long":
                type = TYPE_LONG;
                break;
            case "class java.lang.Float":
                type = TYPE_FLOAT;
                break;
            case "class java.lang.Double":
                type = TYPE_DOUBLE;
                break;
            default:
                type = TYPE_OTHER;
        }
        return type;
    }

//    public static void main(String[] args) {
//        System.out.println(new WritableImpl("rahulk").getClass());
//    }

}
/* End of WritableImpl.java */
