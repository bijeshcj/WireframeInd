package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bijesh on 10/6/2014.
 */
public abstract class BaseTable {
    public abstract void onCreate(SQLiteDatabase database);
    public abstract void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion);
}
