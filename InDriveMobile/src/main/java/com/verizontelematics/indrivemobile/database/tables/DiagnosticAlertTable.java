package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DiagnosticAlertTable extends BaseTable{

	private static final String TAG = DiagnosticAlertTable.class.getCanonicalName();

	public static final String TABLE_NAME_DIAGNOSTIC_ALERT = "DiagnosticAlert";

	private static final String COLUMN_DIAGNOSTIC_ALERT_ID = "_id";
	public static final String COLUMN_ALERT_NAME = "AlertName";
	public static final String COLUMN_ALERT_ID = "AlertId";
	public static final String COLUMN_STATUS = "Status";

	private static final String DIAGNOSTIC_ALERT_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_DIAGNOSTIC_ALERT +"("+
            COLUMN_DIAGNOSTIC_ALERT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+

            COLUMN_ALERT_NAME +" TEXT, "+
            COLUMN_ALERT_ID +" INTEGER, "+
		COLUMN_STATUS+" TEXT"+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(DIAGNOSTIC_ALERT_TABLE_CREATE);
		Log.d(TAG,"created table "+ TABLE_NAME_DIAGNOSTIC_ALERT);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIAGNOSTIC_ALERT);
		onCreate(database);
	}

}