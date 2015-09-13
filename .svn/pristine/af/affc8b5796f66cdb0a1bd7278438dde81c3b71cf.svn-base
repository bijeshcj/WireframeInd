package com.verizontelematics.indrivemobile.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by z688522 on 11/4/14.
 */
public class MaintenanceReminderTable extends BaseTable {

    private static final String TAG = "MaintenanceReminderTable";

    public static final String TABLE_NAME_MAINTENANCE_REMINDER = "maintenance_reminder";

    public static final String COLUMN_MAINTENANCE_Id = "_id";
    public static final String COLUMN_REMINDER_ID = "reminder_id";
    public static final String COLUMN_REMINDER_DESC = "reminder_desc";
    public static final String COLUMN_NOTIFICATION_FLAG_SMS = "notification_flag_sms";
    public static final String COLUMN_SERVICE_TYPE = "service_type";
    public static final String COLUMN_REMINDER_DATE = "reminder_date";
    public static final String COLUMN_REMINDER_CONFIG_MILES = "reminder_config_miles";
    public static final String COLUMN_LAST_NOTIFICATION_DATE = "last_notification_date";
    public static final String COLUMN_SMS_PHONE_NUMBER = "sms_phone_number";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_REMINDER_NAME = "reminder_name";
    public static final String COLUMN_SERVICE_NAME = "service_name";
    public static final String COLUMN_LAST_ODO_METER = "last_odo_meter";
    public static final String COLUMN_NOTIFICATION_FLAG_EMAIL = "notification_flag_email";
    public static final String COLUMN_REMINDER_CONFIG_MONTHS = "reminder_config_months";



    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table if not exists "+TABLE_NAME_MAINTENANCE_REMINDER
                        +"("
                        +COLUMN_MAINTENANCE_Id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                        +COLUMN_REMINDER_ID+" Text,"
                        +COLUMN_REMINDER_DESC+" Text,"
                        +COLUMN_NOTIFICATION_FLAG_SMS+" Text,"
                        +COLUMN_SERVICE_TYPE+" Text,"
                        +COLUMN_REMINDER_DATE+" INTEGER,"
                        +COLUMN_REMINDER_CONFIG_MILES+ " INTEGER,"
                        +COLUMN_LAST_NOTIFICATION_DATE+" INTEGER,"
                        +COLUMN_SMS_PHONE_NUMBER+" Text,"
                        +COLUMN_EMAIL+" Text,"
                        +COLUMN_REMINDER_NAME+" Text,"
                        +COLUMN_SERVICE_NAME+" Text,"
                        +COLUMN_LAST_ODO_METER+" INTEGER,"
                        +COLUMN_NOTIFICATION_FLAG_EMAIL+" Text,"
                        +COLUMN_REMINDER_CONFIG_MONTHS+" INTEGER"
                        +")");
        Log.d(TAG, "created table " + TABLE_NAME_MAINTENANCE_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAINTENANCE_REMINDER);
        onCreate(database);
    }
}
