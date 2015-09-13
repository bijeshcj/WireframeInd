package com.verizontelematics.indrivemobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.verizontelematics.indrivemobile.constants.SQLiteConstants;
import com.verizontelematics.indrivemobile.database.tables.AccountInfoTable;
import com.verizontelematics.indrivemobile.database.tables.AccountTable;
import com.verizontelematics.indrivemobile.database.tables.DiagnosticAlertTable;
import com.verizontelematics.indrivemobile.database.tables.DiagnosticInfoTable;
import com.verizontelematics.indrivemobile.database.tables.DrivingDataTable;
import com.verizontelematics.indrivemobile.database.tables.LocationAlertTable;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceLogTable;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceReminderTable;
import com.verizontelematics.indrivemobile.database.tables.NotificationPreferenceInfoTable;
import com.verizontelematics.indrivemobile.database.tables.PolygonTable;
import com.verizontelematics.indrivemobile.database.tables.RecallTable;
import com.verizontelematics.indrivemobile.database.tables.ServiceTypesTable;
import com.verizontelematics.indrivemobile.database.tables.SpeedAlertTable;
import com.verizontelematics.indrivemobile.database.tables.SubscriptionInfoTypeTable;
import com.verizontelematics.indrivemobile.database.tables.ValetAlertTable;
import com.verizontelematics.indrivemobile.database.tables.VehicleHealthStatusTable;
import com.verizontelematics.indrivemobile.database.tables.VehicleTable;

/**
 * Created by bijesh on 10/6/2014.
 */
public class IndriveDBHelper extends SQLiteOpenHelper implements SQLiteConstants{

    private static final String TAG = IndriveDBHelper.class.getCanonicalName();

    public IndriveDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        getWritableDatabase();
        Context mContext = context;

    }

    public IndriveDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         new MaintenanceLogTable().onCreate(db);
         new AccountTable().onCreate(db);
         new ServiceTypesTable().onCreate(db);
         new MaintenanceReminderTable().onCreate(db);
         new RecallTable().onCreate(db);
         new VehicleHealthStatusTable().onCreate(db);
         new DrivingDataTable().onCreate(db);

//        for Alert Settings module
         new DiagnosticAlertTable().onCreate(db);
         new SpeedAlertTable().onCreate(db);
        new ValetAlertTable().onCreate(db);
         new LocationAlertTable().onCreate(db);
        new PolygonTable().onCreate(db);
//         new LogServiceTypesTable().onCreate(db);

//        for login module
        new AccountInfoTable().onCreate(db);
        new SubscriptionInfoTypeTable().onCreate(db);
        new VehicleTable().onCreate(db);
        new NotificationPreferenceInfoTable().onCreate(db);

        new DiagnosticInfoTable().onCreate(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Deleting all data on app upgrade.
        new MaintenanceLogTable().onUpgrade(db,oldVersion,newVersion);
        new AccountTable().onUpgrade(db,oldVersion,newVersion);
        new ServiceTypesTable().onUpgrade(db,oldVersion,newVersion);
        new MaintenanceReminderTable().onUpgrade(db, oldVersion, newVersion);
        new RecallTable().onUpgrade(db,oldVersion,newVersion);
        new VehicleHealthStatusTable().onUpgrade(db, oldVersion, newVersion);
        new DrivingDataTable().onUpgrade(db,oldVersion,newVersion);

        //        for Alert Settings module
        new DiagnosticAlertTable().onUpgrade(db,oldVersion,newVersion);
        new SpeedAlertTable().onUpgrade(db,oldVersion,newVersion);
        new ValetAlertTable().onUpgrade(db,oldVersion,newVersion);
        new LocationAlertTable().onUpgrade(db,oldVersion,newVersion);
        new PolygonTable().onUpgrade(db,oldVersion,newVersion);
//        new LogServiceTypesTable().onUpgrade(db,oldVersion,newVersion);

//        for login module
        new AccountInfoTable().onUpgrade(db,oldVersion,newVersion);
        new SubscriptionInfoTypeTable().onUpgrade(db,oldVersion,newVersion);
        new VehicleTable().onUpgrade(db,oldVersion,newVersion);
        new NotificationPreferenceInfoTable().onUpgrade(db,oldVersion,newVersion);

        new DiagnosticInfoTable().onUpgrade(db,oldVersion,newVersion);


    }



}
