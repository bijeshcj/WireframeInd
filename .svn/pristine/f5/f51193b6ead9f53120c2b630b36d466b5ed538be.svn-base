package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;


public class AlertServiceTypeTable extends BaseTable {

    public static final String TABLE_NAME_SERVICE_TYPE_ALERT = "ServiceTypeAlert";
    private static final String COLUMN_LOG_ALERT_ID = "_id";
    private static final String COLUMN_SERVICE_NAME = "serviceName";
    private static final String COLUMN_IS_PREDEFINED = "isPredefined";
    private static final String LOG_ALERT_TABLE_TABLE_CREATE = "create table if not exists " + TABLE_NAME_SERVICE_TYPE_ALERT + "(" +
            COLUMN_LOG_ALERT_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SERVICE_NAME + " TEXT, " + COLUMN_IS_PREDEFINED + " INTEGER" +
            ")";
    private static final String TAG = AlertServiceTypeTable.class.getCanonicalName();

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(LOG_ALERT_TABLE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SERVICE_TYPE_ALERT);
        onCreate(database);
    }

}