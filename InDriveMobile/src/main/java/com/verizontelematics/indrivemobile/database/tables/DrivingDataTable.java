package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by bijesh on 12/10/2014.
 */
public class DrivingDataTable extends BaseTable{

	private static final String TAG = DrivingDataTable.class.getCanonicalName();

	public static final String TABLE_NAME_DRIVING_DATA = "driving_data";

	private static final String COLUMN_DRIVING_DATA_ID = "_id";
	public static final String COLUMN_CARBON_FOOTPRINT_LBS = "carbonFootPrintLbs";
	public static final String COLUMN_CITY_GALLONS = "cityGallons";
	public static final String COLUMN_CITY_MILES = "cityMiles";
	public static final String COLUMN_DRIVING_DATE = "drivingDate";
	public static final String COLUMN_ETHANOL_CITY_GALLONS = "ethanolCityGallons";
	public static final String COLUMN_ETHANOL_HIGHWAY_GALLONS = "ethanolHighwayGallons";
	public static final String COLUMN_HIGHWAY_GALLONS = "highwayGallons";
	public static final String COLUMN_HIGHWAY_MILES = "highwayMiles";
	public static final String COLUMN_MAX_SPEED = "maxSpeed";
	public static final String COLUMN_TOTAL_TIME_HOURS = "totalTimeHours";
	public static final String COLUMN_TRIPS = "trips";

	private static final String DRIVING_DATA_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_DRIVING_DATA +"("+
            COLUMN_DRIVING_DATA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+

            COLUMN_CARBON_FOOTPRINT_LBS +" TEXT, "+
            COLUMN_CITY_GALLONS +" TEXT, "+
            COLUMN_CITY_MILES +" TEXT, "+
            COLUMN_DRIVING_DATE +" INTEGER, "+
            COLUMN_ETHANOL_CITY_GALLONS +" TEXT, "+
            COLUMN_ETHANOL_HIGHWAY_GALLONS +" TEXT, "+
            COLUMN_HIGHWAY_GALLONS +" TEXT, "+
            COLUMN_HIGHWAY_MILES +" TEXT, "+
            COLUMN_MAX_SPEED +" INTEGER, "+
            COLUMN_TOTAL_TIME_HOURS +" TEXT, "+
		COLUMN_TRIPS+" TEXT"+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(DRIVING_DATA_TABLE_CREATE);
		Log.d(TAG,"created table "+ TABLE_NAME_DRIVING_DATA);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DRIVING_DATA);
		onCreate(database);
	}

}