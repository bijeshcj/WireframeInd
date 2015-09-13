package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bijesh on 10/13/2014.
 */
public class LogServiceTypesTable extends BaseTable {

    private static String TAG = LogServiceTypesTable.class.getCanonicalName();

    public static final String TABLE_NAME_LOG_SERVICE_TYPES = "LogServiceTypes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SERVICE_NAME = "serviceName";
    private static final String COLUMN_IS_PREDEFINED = "isPredefined";

    private static final String CREATE_TABLE_QUERY = "create table if not exists "+ TABLE_NAME_LOG_SERVICE_TYPES +"(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_SERVICE_NAME + " Text  NOT NULL UNIQUE, " +
            COLUMN_IS_PREDEFINED + " INTEGER )";


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOG_SERVICE_TYPES);
        onCreate(database);
    }
}
