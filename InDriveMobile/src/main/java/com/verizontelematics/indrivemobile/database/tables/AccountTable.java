package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by z689649 on 10/9/14.
 */
public class AccountTable extends BaseTable {

    private static final String TAG = MaintenanceLogTable.class.getCanonicalName();

    public static final String TABLE_NAME_ACCOUNT = "accounts";

    private static final String COLUMN_ACCOUNT_ID = "_id";
    public static final String COLUMN_ACCOUNT_ID_SERVER = "server_account_id";
    public static final String COLUMN_VEHICLE_DES_ = "alert_name";
    private static final String ACCOUNTS_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_ACCOUNT +"(" +
            COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_ACCOUNT_ID_SERVER + " Text  NOT NULL UNIQUE, " +
            COLUMN_VEHICLE_DES_ + " Text )";


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(ACCOUNTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onCreate(database);
    }


}
