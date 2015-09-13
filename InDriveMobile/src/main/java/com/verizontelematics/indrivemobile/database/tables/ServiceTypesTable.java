package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;

import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.models.data.ServiceType;

/**
 * Created by z689649 on 10/9/14.
 */
public class ServiceTypesTable extends BaseTable{
    private static final String TAG = MaintenanceLogTable.class.getCanonicalName();

    public static final String TABLE_NAME_SERVICE_TYPES = "ServiceTypes";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_SERVICE_NAME = "serviceName";
    private static final String COLUMN_SERVICE_DES = "serviceDes";
    private static final String SERVICE_TYPES_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_SERVICE_TYPES +"(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_SERVICE_NAME + " Text  NOT NULL UNIQUE, " +
            COLUMN_SERVICE_DES + " Text )";


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SERVICE_TYPES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SERVICE_TYPES);
        onCreate(database);
    }

    public static void populateDefaultServiceTypes(StorageTransaction lTrans){
        final String[] name = new String []{"Oil Change","Break Change","WindShield Wipers"};
        for (String aLName : name) {
            ServiceType lType = new ServiceType();
            lType.setName(aLName);
            lTrans.insertServiceType(lType);
        }
    }

}
