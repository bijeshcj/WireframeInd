package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DiagnosticInfoTable extends BaseTable{

	private static final String TAG = DiagnosticInfoTable.class.getCanonicalName();

	public static final String TABLE_NAME_DIAGNOSTIC_INFO = "DiagnosticInfo";

	private static final String COLUMN_DIAGNOSTIC_INFO_ID = "_id";
    public static final String COLUMN_SERVICE_AREA="ServiceArea";
	public static final String COLUMN_DTC_CODE = "DTCCode";
	public static final String COLUMN_DTC_DESC = "DTCDescription";

	private static final String DIAGNOSTIC_INFO_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_DIAGNOSTIC_INFO +"("+
            COLUMN_DIAGNOSTIC_INFO_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_SERVICE_AREA +" TEXT, "+
            COLUMN_DTC_CODE +" TEXT, "+
            COLUMN_DTC_DESC +" TEXT "+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(DIAGNOSTIC_INFO_TABLE_CREATE);
		Log.d(TAG,"created table "+ TABLE_NAME_DIAGNOSTIC_INFO);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIAGNOSTIC_INFO);
		onCreate(database);
	}

}