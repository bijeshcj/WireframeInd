package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by z688522 on 11/10/14.
 */
public class RecallTable extends BaseTable {

    private static final String TAG = "RecallTable";

    public static final String TABLE_NAME_RECALL = "recall";

    public static final String COLUMN_RECALL_LOCAL_REC_ID = "_id";
    public static final String COLUMN_RecallID = "recall_id";
    public static final String COLUMN_ComponentName = "component_name";
    public static final String COLUMN_CorrectiveAction = "corrective_action";
    public static final String COLUMN_DefectDescription = "defect_description";
    public static final String COLUMN_Manufacturer = "manufacturer";
    public static final String COLUMN_ManufacturingBeginDate = "manufacturing_begin_date";
    public static final String COLUMN_ManufacturingEndDate = "manufacturing_end_date";
    public static final String COLUMN_PotentialAffectedUnits = "potential_affected_units";
    public static final String COLUMN_Summary = "summary";
    public static final String COLUMN_ComponentNumber = "component_number";
    public static final String COLUMN_DateIssued = "date_issued";
    public static final String COLUMN_DateCompleted = "date_completed";


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table if not exists "+TABLE_NAME_RECALL
                +"("
                + COLUMN_RECALL_LOCAL_REC_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_RecallID+" Text,"
                +COLUMN_ComponentName+" Text,"
                +COLUMN_CorrectiveAction+" Text,"
                +COLUMN_DefectDescription+" Text,"
                +COLUMN_Manufacturer+" Text,"
                +COLUMN_ManufacturingBeginDate+" INTEGER,"
                +COLUMN_ManufacturingEndDate+" INTEGER,"
                +COLUMN_PotentialAffectedUnits+" Text,"
                +COLUMN_Summary+" Text,"
                +COLUMN_ComponentNumber+" Text,"
                +COLUMN_DateIssued+" INTEGER,"
                +COLUMN_DateCompleted+" INTEGER"

                +")");
        Log.d(TAG, "created table " + TABLE_NAME_RECALL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RECALL);
        onCreate(database);
    }
}
