package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by bijesh on 10/6/2014.
 */
public class MaintenanceTable extends BaseTable {

    private static final String TAG = MaintenanceTable.class.getCanonicalName();

    private static final String TABLE_NAME_MAINTENANCE = "maintenance";

    private static final String COLUMN_MAINTENANCE_Id = "_id";
    private static final String COLUMN_SERVICE_TYPE 	= "service_type";
    private static final String COLUMN_ALERT_NAME = "alert_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_DATE	= "date";
    private static final String COLUMN_MILEAGE	= "mileage";//INTEGER

    private static final String MAINTENANCE_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_MAINTENANCE +"(" +
            COLUMN_MAINTENANCE_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_SERVICE_TYPE + " Text, " +
            COLUMN_ALERT_NAME + " Text," +
            COLUMN_DESCRIPTION + " Text," +
            COLUMN_COST + " INTEGER," +
            COLUMN_DATE + " INTEGER," +
            COLUMN_MILEAGE + " INTEGER" +
            ")";



    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(MAINTENANCE_TABLE_CREATE);
        Log.d(TAG,"created table "+TABLE_NAME_MAINTENANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAINTENANCE);
        onCreate(database);
    }
}
