package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class SpeedAlertTable extends BaseTable{

	private static final String TAG = SpeedAlertTable.class.getCanonicalName();

	public static final String TABLE_NAME_SPEED_ALERT = "SpeedAlert";

	private static final String COLUMN_SPEED_ALERT_ID = "_id";
	public static final String COLUMN_ALERT_NAME = "AlertName";
	public static final String COLUMN_ALERT_ID = "AlertId";
	public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_MAXIMUM_SPEED = "MaximumSpeed";

	private static final String SPEED_ALERT_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_SPEED_ALERT +"("+
            COLUMN_SPEED_ALERT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+

            COLUMN_ALERT_NAME +" TEXT, "+
            COLUMN_ALERT_ID +" INTEGER, "+
		COLUMN_STATUS+" TEXT, "+
            COLUMN_MAXIMUM_SPEED +" INTEGER"+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(SPEED_ALERT_TABLE_CREATE);
		Log.d(TAG,"created table "+ TABLE_NAME_SPEED_ALERT);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SPEED_ALERT);
		onCreate(database);
	}

}