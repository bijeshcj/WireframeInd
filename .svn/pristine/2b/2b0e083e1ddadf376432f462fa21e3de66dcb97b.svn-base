package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by z688522 on 11/24/14.
 */
public class VehicleHealthStatusTable extends BaseTable {

    private static final String TAG = "VehicleHealthStatusTable";

    public static final String TABLE_NAME_HEALTH_STATUS = "health_status";

    private static final String COLUMN_HEALTH_STATUS_LOCAL_REC_Id = "_id";
    public static final String COLUMN_ServiceArea = "service_area";
    public static final String COLUMN_ServiceAreaStatus = "service_area_status";

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table if not exists "+TABLE_NAME_HEALTH_STATUS
                +"("
                +COLUMN_HEALTH_STATUS_LOCAL_REC_Id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_ServiceArea+" Text,"
                +COLUMN_ServiceAreaStatus+" Text"
                +")");
        Log.d(TAG, "created table " + TABLE_NAME_HEALTH_STATUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HEALTH_STATUS);
        onCreate(database);
    }
}
