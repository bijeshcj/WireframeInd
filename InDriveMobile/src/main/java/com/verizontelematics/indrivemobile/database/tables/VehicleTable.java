package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class VehicleTable extends BaseTable{

	private static final String TAG = VehicleTable.class.getCanonicalName();

	public static final String TABLE_NAME_VEHICLE = "Vehicle";

	public static final String COLUMN_VEHICLE_ID = "_id";
	public static final String COLUMN_MOBILEUSERID = "mobileUserId";
	public static final String COLUMN_VEHICLEID = "vehicleId";
	public static final String COLUMN_ACCOUNTID = "accountId";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_MAKE = "make";
	public static final String COLUMN_MODEL = "model";
	public static final String COLUMN_BODYTYPE = "bodyType";
	public static final String COLUMN_USERTYPE = "userType";
	public static final String COLUMN_DEVICESTATUS = "deviceStatus";

	public static final String VEHICLETABLE_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_VEHICLE+"("+
		COLUMN_VEHICLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+

		COLUMN_MOBILEUSERID+" TEXT, "+
		COLUMN_VEHICLEID+" TEXT, "+
		COLUMN_ACCOUNTID+" TEXT, "+
		COLUMN_YEAR+" TEXT, "+
		COLUMN_MAKE+" TEXT, "+
		COLUMN_MODEL+" TEXT, "+
		COLUMN_BODYTYPE+" TEXT, "+
		COLUMN_USERTYPE+" TEXT, "+
		COLUMN_DEVICESTATUS+" TEXT"+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(VEHICLETABLE_TABLE_CREATE);
		Log.d(TAG,"created table "+TABLE_NAME_VEHICLE);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VEHICLE);
		onCreate(database);
	}

}