/**
 * Author : Pulkit Jain
 * File   : WritableImpl.java
 * Email  :
 * Created:
 * Edited :
 */

package com.rasp.interfaces;

/* Import list */
import java.io.Serializable;

public  class WritableImpl
    implements Serializable, Writable
{
    /* Constants */
    public static final int TYPE_NULL = 0;
    public static final int TYPE_INTEGER = 1;
    public static final int TYPE_STRING = 2;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_DOUBLE = 4;
    public static final int TYPE_LONG = 5;
    public static final int TYPE_OTHER = 6;

    private Object obj;
    private Integer type;

    public WritableImpl(Object obj)
    {
        this.obj = obj;
        type = TYPE_NULL;
    }

    public WritableImpl(Object obj, Integer type)
    {
        this.obj = obj;
        this.type = type;
    }

    public Object getObj()
    {
        return obj;
    }

    public int getType()
    {
        if(obj == null)
        {
            return TYPE_NULL;
        }
        if(type != TYPE_NULL)
        {
            return type;
        }

        switch(obj.getClass().toString())
        {
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

    public static void main(String[] args) {

        System.out.println(new WritableImpl("rahulk").getObj());

    }
}