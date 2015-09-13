package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by bijesh on 10/8/2014.
 */
public class MaintenanceAlertTable extends BaseTable {

    private static final String TAG = MaintenanceAlertTable.class.getCanonicalName();

    private static final String TABLE_NAME_MAINTENANCE_ALERT = "maintenance_alert";

    private static final String COLUMN_MAINTENANCE_Id = "_id";
    private static final String COLUMN_SERVICE_TYPE 	= "service_type";
    private static final String COLUMN_ALERT_NAME = "alert_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE	= "date";
    private static final String COLUMN_MILEAGE_INTERVAL	= "mileage_interval";//INTEGER
    private static final String COLUMN_DAY_MONTH = "day_month";
    private static final String COLUMN_USER_SELECTION = "userSelection";
    private static final String COLUMN_MAINTENANCE_ID_ON_SERVER = "maintenance_id_on_server";

    private static final String MAINTENANCE_ALERT_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_MAINTENANCE_ALERT +"(" +
            COLUMN_MAINTENANCE_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_SERVICE_TYPE + " Text, " +
            COLUMN_ALERT_NAME + " Text," +
            COLUMN_DESCRIPTION + " Text," +
            COLUMN_DATE + " INTEGER," +
            COLUMN_MILEAGE_INTERVAL + " INTEGER," +
            COLUMN_DAY_MONTH + " INTEGER," +
            COLUMN_USER_SELECTION +" Text,"+
            COLUMN_MAINTENANCE_ID_ON_SERVER + " INTEGER UNIQUE" +
            ")";


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(MAINTENANCE_ALERT_TABLE_CREATE);
        Log.d(TAG, "created table " + TABLE_NAME_MAINTENANCE_ALERT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAINTENANCE_ALERT);
        onCreate(database);
    }
}
