package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class PolygonTable extends BaseTable{

	private static final String TAG = PolygonTable.class.getCanonicalName();

	public static final String TABLE_NAME_POLYGON = "Polygon";

	private static final String COLUMN_POLYGON_ID = "_id";
    public static final String COLUMN_ALERT_ID = "alert_id";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_ORDER = "_order";

	private static final String POLYGON_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_POLYGON+"("+
		COLUMN_POLYGON_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
        COLUMN_ALERT_ID+" INTEGER, "+
		COLUMN_LATITUDE+" INTEGER, "+
		COLUMN_LONGITUDE+" INTEGER, "+
		COLUMN_ORDER+" INTEGER"+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(POLYGON_TABLE_CREATE);
		Log.d(TAG,"created table "+TABLE_NAME_POLYGON);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POLYGON);
		onCreate(database);
	}

}