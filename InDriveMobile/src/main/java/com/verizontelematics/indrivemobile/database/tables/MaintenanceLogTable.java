package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by bijesh on 10/6/2014.
 */
public class MaintenanceLogTable extends BaseTable {

    private static final String TAG = MaintenanceLogTable.class.getCanonicalName();

    public static final String TABLE_NAME_MAINTENANCE = "maintenance";

    public static final String COLUMN_MAINTENANCE_Id = "_id";
    public static final String COLUMN_SERVICE_TYPE 	= "service_type";
    public static final String COLUMN_SERVICE_CENTER 	= "service_center";
    public static final String COLUMN_ALERT_NAME = "alert_name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_DATE	= "date";
    public static final String COLUMN_MILEAGE	= "mileage";
    public static final String COLUMN_MAINTENANCE_ID_ON_SERVER = "maintenance_id_on_server";

    private static final String MAINTENANCE_LOG_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_MAINTENANCE +"(" +
            COLUMN_MAINTENANCE_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_SERVICE_TYPE + " Text, " +
            COLUMN_ALERT_NAME + " Text," +
            COLUMN_SERVICE_CENTER + " Text," +
            COLUMN_DESCRIPTION + " Text," +
            COLUMN_COST + " INTEGER," +
            COLUMN_DATE + " INTEGER," +
            COLUMN_MILEAGE + " INTEGER," +
            COLUMN_MAINTENANCE_ID_ON_SERVER + " Text UNIQUE " +
            ")";

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(MAINTENANCE_LOG_TABLE_CREATE);
        Log.d(TAG,"created table "+TABLE_NAME_MAINTENANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAINTENANCE);
        onCreate(database);
    }
}
