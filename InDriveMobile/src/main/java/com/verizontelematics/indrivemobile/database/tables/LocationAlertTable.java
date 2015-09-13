package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class LocationAlertTable extends BaseTable{

	private static final String TAG = LocationAlertTable.class.getCanonicalName();

	public static final String TABLE_NAME_LOCATION_ALERT = "LocationAlert";

	private static final String COLUMN_LOCATION_ALERT_ID = "_id";
	public static final String COLUMN_ALERT_NAME = "AlertName";
	public static final String COLUMN_ALERT_ID = "AlertId";
	public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_ALERT_TRIGGER = "AlertTrigger";
    public static final String COLUMN_RADIUS = "radius";
    public static final String COLUMN_LATITUDE = "Latitude";
    public static final String COLUMN_LONGITUDE = "Longitude";
//shape types circle and polygon

//schedule type
    public static final String COLUMN_WEEKDAYS = "WeekDays";
    public static final String COLUMN_MINUTES_FROM_MID_NIGHT = "MinutesFromMidnight";
    public static final String COLUMN_DURATION = "Duration";

	private static final String LOCATION_ALERT_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_LOCATION_ALERT +"("+
            COLUMN_LOCATION_ALERT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+

            COLUMN_ALERT_NAME +" TEXT, "+
            COLUMN_ALERT_ID +" INTEGER, "+
		COLUMN_STATUS+" TEXT, "+

            COLUMN_ALERT_TRIGGER+" TEXT, "+
            COLUMN_RADIUS+" INTEGER, "+
            COLUMN_LATITUDE+" INTEGER, "+
            COLUMN_LONGITUDE+" INTEGER, "+

            COLUMN_WEEKDAYS+" TEXT, "+
            COLUMN_MINUTES_FROM_MID_NIGHT+" INTEGER, "+
            COLUMN_DURATION+" INTEGER "+

		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(LOCATION_ALERT_TABLE_CREATE);
		Log.d(TAG,"created table "+ TABLE_NAME_LOCATION_ALERT);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATION_ALERT);
		onCreate(database);
	}

}