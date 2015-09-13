package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ValetAlertTable extends BaseTable{

	private static final String TAG = ValetAlertTable.class.getCanonicalName();

	public static final String TABLE_NAME_VALET_ALERT = "ValetAlert";

	public static final String COLUMN_VALET_ALERT_ID = "_id";
	public static final String COLUMN_LATITUDE = "Latitude";
	public static final String COLUMN_LONGITUDE = "Longitude";
    public static final String COLUMN_ALERT_NAME = "AlertName";
    private static final String COLUMN_ALERT_ID = "AlertId";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_ALERT_TRIGGER = "AlertTrigger";
    public static final String COLUMN_RADIUS = "radius";

	private static final String VALET_ALERT_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_VALET_ALERT +"("+
            COLUMN_VALET_ALERT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
        COLUMN_ALERT_ID+" INTEGER, "+
        COLUMN_ALERT_NAME+" Text, "+
        COLUMN_STATUS+" Text, "+
        COLUMN_ALERT_TRIGGER+" Text, "+
        COLUMN_RADIUS+" INTEGER, "+
		COLUMN_LATITUDE+" INTEGER, "+
		COLUMN_LONGITUDE+" INTEGER"+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(VALET_ALERT_TABLE_CREATE);
		Log.d(TAG,"created table "+ TABLE_NAME_VALET_ALERT);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VALET_ALERT);
		onCreate(database);
	}

}