package com.verizontelematics.indrivemobile.database;

import android.content.ContentValues;

import com.verizontelematics.indrivemobile.models.data.BaseData;

/**
 * Created by bijesh on 10/10/2014.
 */
public abstract class ContentManager<T extends BaseData> {

    public abstract ContentValues generateContent(T data);

//    protected enum IgnoreColumn{
//        COLUMN_Id
//    }
    protected enum SQLType {
        TABLE,
        COLUMN
    }

    class DataContainer{
        Class cls;
        Object value;
        DataContainer(Class cls,Object value){
//            System.out.println(cls+" "+value);
            this.cls = cls;
            this.value = value;
        }
    }

}
