package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class NotificationPreferenceInfoTable extends BaseTable{

	private static final String TAG = NotificationPreferenceInfoTable.class.getCanonicalName();

	public static final String TABLE_NAME_NOTIFICATIONPREFERENCEINFO = "NotificationPreferenceInfo";

	public static final String COLUMN_NOTIFICATIONPREFERENCEINFO_ID = "_id";
	public static final String COLUMN_ACCOUNT_ID = "accountId";
	public static final String COLUMN_PUSHNOTIFICATION = "pushNotification";
	public static final String COLUMN_EMAILNOTIFICATION = "emailNotification";
	public static final String COLUMN_SMSNOTIFICATION = "smsNotification";

	public static final String NOTIFICATIONPREFERENCEINFOTABLE_TABLE_CREATE = "create table if not exists "+ TABLE_NAME_NOTIFICATIONPREFERENCEINFO+"("+
		COLUMN_NOTIFICATIONPREFERENCEINFO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+

        COLUMN_ACCOUNT_ID+" TEXT, "+
		COLUMN_PUSHNOTIFICATION+" TEXT, "+
		COLUMN_EMAILNOTIFICATION+" TEXT, "+
		COLUMN_SMSNOTIFICATION+" TEXT"+
		")";


	@Override
	 public void onCreate(SQLiteDatabase database) {
		database.execSQL(NOTIFICATIONPREFERENCEINFOTABLE_TABLE_CREATE);
		Log.d(TAG,"created table "+TABLE_NAME_NOTIFICATIONPREFERENCEINFO);
	}


	@Override
	 public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTIFICATIONPREFERENCEINFO);
		onCreate(database);
	}

}