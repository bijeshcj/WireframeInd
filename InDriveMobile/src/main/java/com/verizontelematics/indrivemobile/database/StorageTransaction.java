package com.verizontelematics.indrivemobile.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.verizontelematics.indrivemobile.database.tables.AccountInfoTable;
import com.verizontelematics.indrivemobile.database.tables.AccountTable;
import com.verizontelematics.indrivemobile.database.tables.AlertServiceTypeTable;
import com.verizontelematics.indrivemobile.database.tables.BaseTable;
import com.verizontelematics.indrivemobile.database.tables.DiagnosticAlertTable;
import com.verizontelematics.indrivemobile.database.tables.DiagnosticInfoTable;
import com.verizontelematics.indrivemobile.database.tables.DrivingDataTable;
import com.verizontelematics.indrivemobile.database.tables.LocationAlertTable;
import com.verizontelematics.indrivemobile.database.tables.LogServiceTypesTable;
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
import com.verizontelematics.indrivemobile.models.Accounts;
import com.verizontelematics.indrivemobile.models.data.AddressType;
import com.verizontelematics.indrivemobile.models.data.BaseData;
import com.verizontelematics.indrivemobile.models.data.DiagnosticAlert;
import com.verizontelematics.indrivemobile.models.data.DiagnosticInfo;
import com.verizontelematics.indrivemobile.models.data.DrivingData;
import com.verizontelematics.indrivemobile.models.data.EmailType;
import com.verizontelematics.indrivemobile.models.data.HealthStatus;
import com.verizontelematics.indrivemobile.models.data.LocationAlert;
import com.verizontelematics.indrivemobile.models.data.MaintenanceLogData;
import com.verizontelematics.indrivemobile.models.data.MaintenanceReminderData;
import com.verizontelematics.indrivemobile.models.data.RecallData;
import com.verizontelematics.indrivemobile.models.data.ServiceType;
import com.verizontelematics.indrivemobile.models.data.SpeedAlert;
import com.verizontelematics.indrivemobile.models.data.SubscriptionInfoType;
import com.verizontelematics.indrivemobile.models.data.UserAccountInfoData;
import com.verizontelematics.indrivemobile.models.data.UserDetail;
import com.verizontelematics.indrivemobile.models.data.UserPreferenceData;
import com.verizontelematics.indrivemobile.models.data.ValetAlert;
import com.verizontelematics.indrivemobile.models.data.Vehicle;
import com.verizontelematics.indrivemobile.models.data.Vertex;
import com.verizontelematics.indrivemobile.models.uimodels.DrivingChartData;
import com.verizontelematics.indrivemobile.utils.ui.DBUtils;
import com.verizontelematics.indrivemobile.utils.ui.DrivingDataCategory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by bijesh on 10/6/2014.
 */
public class StorageTransaction {

    private static final String TAG = StorageTransaction.class.getCanonicalName();
    private SQLiteDatabase mDatabase;

    public StorageTransaction(Context context){
        IndriveDBHelper mDBHelper = new IndriveDBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }


    public void close(){
        mDatabase.close();
    }

    private final String DATE_FORMAT = "yyyyMMddHHmmss";

    @SuppressLint("SimpleDateFormat")
    private  final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public  long formatDateAsLong(Calendar cal){
        return Long.parseLong(dateFormat.format(cal.getTime()));
    }


    public UserDetail getUserDetails(String mobileUserid){
        UserDetail userDetail = new UserDetail();
        String query_userAccountInfo = String.format("SELECT * FROM '%s' WHERE '%s' = '%s'",AccountInfoTable.TABLE_NAME_ACCOUNTINFO,AccountInfoTable.COLUMN_MOBILEUSERID,mobileUserid);
        UserAccountInfoData userAccountInfoData  = new UserAccountInfoData();
        Cursor cursor = mDatabase.rawQuery(query_userAccountInfo,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                userAccountInfoData.setMobileUserID(mobileUserid);
                userAccountInfoData.setAccountID(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_ACCOUNTID)));
                userAccountInfoData.setAccountStatus(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_ACCOUNTSTATUS)));
                userAccountInfoData.setAccountType(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_ACCOUNTTYPE)));
                userAccountInfoData.setFirstName(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_FIRSTNAME)));
                userAccountInfoData.setLastName(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_LASTNAME)));
                EmailType emailType = new EmailType();
                emailType.setEmailMask(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_EMAIL)));
                emailType.setEmailToken(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_EMAIL_TOKEN)));
                userAccountInfoData.setEmail(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_EMAIL)));
                userAccountInfoData.setPhone(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_PHONE)));
                userAccountInfoData.setOrganization(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_ORGANIZATION)));

                AddressType addressType = new AddressType();
                addressType.setAddress1(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_ADDRESS1)));
                addressType.setAddress2(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_ADDRESS2)));
                addressType.setCity(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_CITY)));
                addressType.setState(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_STATE)));
                addressType.setCountry(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_COUNTRY)));
                addressType.setZipCode(cursor.getString(cursor.getColumnIndex(AccountInfoTable.COLUMN_ZIPCODE)));

                userAccountInfoData.setAddress(addressType);



                cursor.moveToNext();
            }
        }

        String query = "select * from SubscriptionInfoType where accountID = "+userAccountInfoData.getAccountID();
        cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            SubscriptionInfoType subscriptionInfoType = new SubscriptionInfoType();
            ArrayList<SubscriptionInfoType> lstSubscr = new ArrayList<SubscriptionInfoType>();
            while (!cursor.isAfterLast()){
                subscriptionInfoType.setPackageName(cursor.getString(cursor.getColumnIndex(SubscriptionInfoTypeTable.COLUMN_PACKAGE_NAME)));
                subscriptionInfoType.setPartNumber(cursor.getString(cursor.getColumnIndex(SubscriptionInfoTypeTable.COLUMN_PART_NUMBER)));

                lstSubscr.add(subscriptionInfoType);

                cursor.moveToNext();
            }


            userAccountInfoData.setSubscriptionInfo(lstSubscr);

            userDetail.setAccountInfo(userAccountInfoData);

            String queryVehicles = "select * from Vehicle where accountID = "+userAccountInfoData.getAccountID();
            cursor = mDatabase.rawQuery(queryVehicles,null);
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Vehicle vehicle = new Vehicle();
                ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
                while (!cursor.isAfterLast()){
                    vehicle.setMobileUserID(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_MOBILEUSERID)));
                    vehicle.setVehicleID(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_VEHICLEID)));
                    vehicle.setAccountID(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_ACCOUNTID)));
                    vehicle.setYear(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_YEAR)));
                    vehicle.setMake(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_MAKE)));
                    vehicle.setModel(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_MODEL)));
                    vehicle.setBodyType(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_BODYTYPE)));
                    vehicle.setUserType(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_USERTYPE)));
                    vehicle.setDeviceStatus(cursor.getString(cursor.getColumnIndex(VehicleTable.COLUMN_DEVICESTATUS)));

                    vehicles.add(vehicle);

                    cursor.moveToNext();
                }
                userDetail.setVehiclesInfo(vehicles);
            }

//            opulate notification

            query = "select * from NotificationPreferenceInfo where accountId = "+userAccountInfoData.getAccountID();
            cursor = mDatabase.rawQuery(queryVehicles,null);
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                UserPreferenceData userPreferenceData = new UserPreferenceData();
                while (!cursor.isAfterLast()){
                    userPreferenceData.setAccountID(userAccountInfoData.getAccountID());
                    userPreferenceData.setEmailNotification(cursor.getString(cursor.getColumnIndex(NotificationPreferenceInfoTable.COLUMN_EMAILNOTIFICATION)));
                    userPreferenceData.setPushNotification(cursor.getString(cursor.getColumnIndex(NotificationPreferenceInfoTable.COLUMN_PUSHNOTIFICATION)));
                    userPreferenceData.setSmsNotification(cursor.getString(cursor.getColumnIndex(NotificationPreferenceInfoTable.COLUMN_SMSNOTIFICATION)));
                    cursor.moveToNext();
                }

                userDetail.setNotificationPreferenceInfo(userPreferenceData);
            }


        }



        return userDetail;
    }


    public long insertLoginModule(UserAccountInfoData userAccountInfoData,List<Vehicle> vehicles,UserPreferenceData userPreferenceData){
        Log.d(TAG,"inserting the data for login module "+userAccountInfoData);
        if(userAccountInfoData != null) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(AccountInfoTable.COLUMN_ACCOUNTSTATUS, userAccountInfoData.getAccountStatus());
            contentValues.put(AccountInfoTable.COLUMN_MOBILEUSERID,userAccountInfoData.getMobileUserID());
            contentValues.put(AccountInfoTable.COLUMN_ACCOUNTID, userAccountInfoData.getAccountID());
            contentValues.put(AccountInfoTable.COLUMN_ACCOUNTTYPE, userAccountInfoData.getAccountType());
            contentValues.put(AccountInfoTable.COLUMN_FIRSTNAME, userAccountInfoData.getFirstName());
            contentValues.put(AccountInfoTable.COLUMN_LASTNAME, userAccountInfoData.getLastName());
            contentValues.put(AccountInfoTable.COLUMN_EMAIL, userAccountInfoData.getEmail());
            contentValues.put(AccountInfoTable.COLUMN_PHONE, userAccountInfoData.getPhone());

            AddressType addressType = userAccountInfoData.getAddress();
            if (addressType != null) {
                contentValues.put(AccountInfoTable.COLUMN_ADDRESS1, addressType.getAddress1());
                contentValues.put(AccountInfoTable.COLUMN_ADDRESS2,addressType.getAddress2());
                contentValues.put(AccountInfoTable.COLUMN_CITY,addressType.getCity());
                contentValues.put(AccountInfoTable.COLUMN_STATE,addressType.getState());
                contentValues.put(AccountInfoTable.COLUMN_COUNTRY,addressType.getCountry());
                contentValues.put(AccountInfoTable.COLUMN_ZIPCODE,addressType.getZipCode());
            }

            mDatabase.insert(AccountInfoTable.TABLE_NAME_ACCOUNTINFO,null,contentValues);

            insertSubscriptionInfos(userAccountInfoData.getAccountID(), userAccountInfoData.getSubscriptionInfo());
//  insert vehicle data
            insertVehiclesInfo(userAccountInfoData.getAccountID(),vehicles);

//            insert notification preference info
            insertNotificationPreferenceInfo(userAccountInfoData.getAccountID(),userPreferenceData);

        }
        return 0;
    }

    public void insertNotificationPreferenceInfo(String accountId,UserPreferenceData userPreferenceData){
          if(userPreferenceData != null){
              ContentValues contentValues = new ContentValues();
              contentValues.put(NotificationPreferenceInfoTable.COLUMN_ACCOUNT_ID,accountId);
              contentValues.put(NotificationPreferenceInfoTable.COLUMN_PUSHNOTIFICATION,userPreferenceData.getPushNotification());
              contentValues.put(NotificationPreferenceInfoTable.COLUMN_SMSNOTIFICATION,userPreferenceData.getSmsNotification());
              contentValues.put(NotificationPreferenceInfoTable.COLUMN_EMAILNOTIFICATION, userPreferenceData.getEmailNotification());

              mDatabase.insert(NotificationPreferenceInfoTable.TABLE_NAME_NOTIFICATIONPREFERENCEINFO,null,contentValues);
          }
    }

    private void insertVehiclesInfo(String accountId,List<Vehicle> vehicles){
        if(vehicles != null){
            ContentValues contentValues = new ContentValues();
            for(Vehicle vehicle:vehicles) {
                contentValues.put(VehicleTable.COLUMN_ACCOUNTID, accountId);
                contentValues.put(VehicleTable.COLUMN_MOBILEUSERID, vehicle.getMobileUserID());
                contentValues.put(VehicleTable.COLUMN_VEHICLEID, vehicle.getVehicleID());
                contentValues.put(VehicleTable.COLUMN_YEAR, vehicle.getYear());
                contentValues.put(VehicleTable.COLUMN_MAKE, vehicle.getMake());
                contentValues.put(VehicleTable.COLUMN_MODEL, vehicle.getModel());
                contentValues.put(VehicleTable.COLUMN_BODYTYPE, vehicle.getBodyType());
                contentValues.put(VehicleTable.COLUMN_USERTYPE, vehicle.getUserType());
                contentValues.put(VehicleTable.COLUMN_DEVICESTATUS, vehicle.getDeviceStatus());
                mDatabase.insert(VehicleTable.TABLE_NAME_VEHICLE,null,contentValues);
            }
        }
    }

    private void insertSubscriptionInfos(String accountId,List<SubscriptionInfoType> subscriptionInfoTypes){
        if(subscriptionInfoTypes != null){
            ContentValues contentValues = null;
            for(SubscriptionInfoType subscriptionInfoType:subscriptionInfoTypes){
                contentValues = new ContentValues();
                contentValues.put(SubscriptionInfoTypeTable.COLUMN_ACCOUNTID,accountId);
                contentValues.put(SubscriptionInfoTypeTable.COLUMN_PACKAGE_NAME,subscriptionInfoType.getPackageName());
                contentValues.put(SubscriptionInfoTypeTable.COLUMN_PART_NUMBER,subscriptionInfoType.getPartNumber());

                mDatabase.insert(SubscriptionInfoTypeTable.TABLE_NAME_SUBSCRIPTION_INFO_TYPE,null,contentValues);
            }
        }
    }



    public long insertMaintenanceLog(Context context,MaintenanceLogData maintenance){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MaintenanceLogTable.COLUMN_SERVICE_TYPE,maintenance.getServiceType());
        contentValues.put(MaintenanceLogTable.COLUMN_ALERT_NAME,maintenance.getServiceName());
        contentValues.put(MaintenanceLogTable.COLUMN_DESCRIPTION,maintenance.getDescription());
        contentValues.put(MaintenanceLogTable.COLUMN_COST,maintenance.getCost());
        contentValues.put(MaintenanceLogTable.COLUMN_SERVICE_CENTER,maintenance.getServiceCenter());



       // contentValues.put(MaintenanceLogTable.COLUMN_DATE,formatDateAsLong(maintenance.getDate()));
        contentValues.put(MaintenanceLogTable.COLUMN_DATE,maintenance.getServiceDate());
        contentValues.put(MaintenanceLogTable.COLUMN_MILEAGE,maintenance.getMileage());
        long retVal = mDatabase.insert(MaintenanceLogTable.TABLE_NAME_MAINTENANCE,null,contentValues);
        Log.d(TAG,"maintenance inserted successfully");
        return retVal;
    }


    // there is problem with MaintenanceLogData and Maintenance classes use only one.
    // add the missing attributes to the class and use that.
    public void insertBulkMaintenanceLog(Context context,List<MaintenanceLogData> maintenance){

        for(MaintenanceLogData lData:maintenance) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MaintenanceLogTable.COLUMN_SERVICE_TYPE, lData.getServiceType());
            contentValues.put(MaintenanceLogTable.COLUMN_ALERT_NAME, lData.getServiceName());
            contentValues.put(MaintenanceLogTable.COLUMN_DESCRIPTION, lData.getDescription());
            contentValues.put(MaintenanceLogTable.COLUMN_COST, lData.getCost());
            contentValues.put(MaintenanceLogTable.COLUMN_SERVICE_CENTER,lData.getServiceCenter());
            // for now just remove the formatting of the date here.
            contentValues.put(MaintenanceLogTable.COLUMN_DATE, lData.getServiceDate());
            contentValues.put(MaintenanceLogTable.COLUMN_MILEAGE, lData.getMileage());
            contentValues.put(MaintenanceLogTable.COLUMN_MAINTENANCE_ID_ON_SERVER, lData.getServiceID());
            mDatabase.insert(MaintenanceLogTable.TABLE_NAME_MAINTENANCE, null, contentValues);
            Log.d(TAG, "maintenance inserted successfully");
        }

    }

    public void insertSpeedAlert(List<SpeedAlert> speedAlerts){
        for(SpeedAlert speedAlert:speedAlerts) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SpeedAlertTable.COLUMN_ALERT_ID,speedAlert.getAlertId());
            contentValues.put(SpeedAlertTable.COLUMN_ALERT_NAME,speedAlert.getAlertName());
            contentValues.put(SpeedAlertTable.COLUMN_STATUS,speedAlert.getStatus());
            contentValues.put(SpeedAlertTable.COLUMN_MAXIMUM_SPEED, speedAlert.getMaximumSpeed());
            mDatabase.insert(SpeedAlertTable.TABLE_NAME_SPEED_ALERT, null, contentValues);
            Log.d(TAG, "SpeedAlert inserted successfully");
        }
    }

    public void insertValetAlert(List<ValetAlert> valetAlerts){
        for(ValetAlert valetAlert:valetAlerts) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(ValetAlertTable.COLUMN_VALET_ALERT_ID,valetAlert.getAlertId());
            contentValues.put(ValetAlertTable.COLUMN_ALERT_NAME,valetAlert.getAlertName());
            if(valetAlert.getCircle() != null) {
                contentValues.put(ValetAlertTable.COLUMN_RADIUS, valetAlert.getCircle().getRadius());
                if(valetAlert.getCircle().getCircleCenter() != null){
                    contentValues.put(ValetAlertTable.COLUMN_LATITUDE,valetAlert.getCircle().getCircleCenter().getLatitude());
                    contentValues.put(ValetAlertTable.COLUMN_LONGITUDE,valetAlert.getCircle().getCircleCenter().getLongitude());
                }
            }
            contentValues.put(ValetAlertTable.COLUMN_STATUS,valetAlert.getStatus());
            contentValues.put(ValetAlertTable.COLUMN_ALERT_TRIGGER,valetAlert.getAlertTrigger());

            mDatabase.insert(ValetAlertTable.TABLE_NAME_VALET_ALERT, null, contentValues);
            Log.d(TAG,"inserted Valet Alert successfully");
        }
    }

    public void insertDiagnosticsAlert(List<DiagnosticAlert> diagnosticAlerts){
        Log.d(TAG,"$$$ insertDiagnosticsAlert "+diagnosticAlerts);

        for(DiagnosticAlert diagnosticAlert:diagnosticAlerts){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DiagnosticAlertTable.COLUMN_ALERT_ID,diagnosticAlert.getAlertId());
            contentValues.put(DiagnosticAlertTable.COLUMN_ALERT_NAME,diagnosticAlert.getAlertName());
            contentValues.put(DiagnosticAlertTable.COLUMN_STATUS,diagnosticAlert.getStatus());

            mDatabase.insert(DiagnosticAlertTable.TABLE_NAME_DIAGNOSTIC_ALERT, null, contentValues);
            Log.d(TAG, "DiagnosticsAlert inserted successfully");
        }
    }

    public void insertLocationAlerts(List<LocationAlert> locationAlerts){
        Log.d(TAG,"$$$ insertLocationAlerts "+locationAlerts);
        for(LocationAlert locationAlert:locationAlerts){
            ContentValues contentValues = new ContentValues();
            contentValues.put(LocationAlertTable.COLUMN_ALERT_NAME,locationAlert.getAlertName());
            contentValues.put(LocationAlertTable.COLUMN_ALERT_ID,locationAlert.getAlertId());
            contentValues.put(LocationAlertTable.COLUMN_STATUS,locationAlert.getStatus());
            contentValues.put(LocationAlertTable.COLUMN_ALERT_TRIGGER,locationAlert.getAlertTrigger());
            //shape types circle
            if(locationAlert.getShape() != null){
                if(locationAlert.getShape().getCircle() != null) {
                    contentValues.put(LocationAlertTable.COLUMN_RADIUS, locationAlert.getShape().getCircle().getRadius());
                    if(locationAlert.getShape().getCircle().getCircleCenter() != null){
                        contentValues.put(LocationAlertTable.COLUMN_LATITUDE,locationAlert.getShape().getCircle().getCircleCenter().getLatitude());
                        contentValues.put(LocationAlertTable.COLUMN_LONGITUDE,locationAlert.getShape().getCircle().getCircleCenter().getLongitude());
                    }
                }
            }
//           shape type polygon
            insertPolygonForAlert(locationAlert);

//schedule type
            if(locationAlert.getSchedule() != null){
                contentValues.put(LocationAlertTable.COLUMN_WEEKDAYS,locationAlert.getSchedule().getWeekDays());
                contentValues.put(LocationAlertTable.COLUMN_MINUTES_FROM_MID_NIGHT,locationAlert.getSchedule().getMinutesFromMidNight());
                contentValues.put(LocationAlertTable.COLUMN_DURATION,locationAlert.getSchedule().getDuration());
            }
            mDatabase.insert(LocationAlertTable.TABLE_NAME_LOCATION_ALERT, null, contentValues);
            Log.d(TAG, "LocationAlert inserted successfully");
        }
    }

    private void insertPolygonForAlert(LocationAlert locationAlert){
        ContentValues contentValues = new ContentValues();


        if(locationAlert.getShape() != null){
            if(locationAlert.getShape().getPolygon() != null){

                ArrayList<Vertex> vertices = locationAlert.getShape().getPolygon().getVertices();
                if(vertices != null) {
                    for (Vertex vertex : vertices) {
                        contentValues.put(PolygonTable.COLUMN_ALERT_ID,locationAlert.getAlertId());
                        contentValues.put(PolygonTable.COLUMN_ORDER,vertex.getOrder());
                        contentValues.put(PolygonTable.COLUMN_LATITUDE,vertex.getPosition().Latitude);
                        contentValues.put(PolygonTable.COLUMN_LONGITUDE,vertex.getPosition().Longitude);
                        mDatabase.insert(PolygonTable.TABLE_NAME_POLYGON, null, contentValues);
                    }
                }
            }
        }

//        public static final String COLUMN_LATITUDE = "latitude";
//        public static final String COLUMN_LONGITUDE = "longitude";
//        public static final String COLUMN_ORDER = "_order";

    }

    public void insertDrivingData(List<DrivingData> drivingData){
        for(DrivingData dData:drivingData){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DrivingDataTable.COLUMN_CARBON_FOOTPRINT_LBS,dData.getCarbonFootprintLbs());
            contentValues.put(DrivingDataTable.COLUMN_CITY_GALLONS,dData.getCityGallons());
            contentValues.put(DrivingDataTable.COLUMN_CITY_MILES,dData.getCityMiles());
            contentValues.put(DrivingDataTable.COLUMN_DRIVING_DATE,dData.getDrivingDate());
            contentValues.put(DrivingDataTable.COLUMN_ETHANOL_CITY_GALLONS,dData.getEthanolCityGallons());
            contentValues.put(DrivingDataTable.COLUMN_ETHANOL_HIGHWAY_GALLONS,dData.getEthanolHighwayGallons());
            contentValues.put(DrivingDataTable.COLUMN_HIGHWAY_GALLONS,dData.getHighwayGallons());
            contentValues.put(DrivingDataTable.COLUMN_HIGHWAY_MILES,dData.getHighwayMiles());
            contentValues.put(DrivingDataTable.COLUMN_MAX_SPEED,dData.getMaxSpeed());
            contentValues.put(DrivingDataTable.COLUMN_TOTAL_TIME_HOURS,dData.getTotalTimeHours());
            contentValues.put(DrivingDataTable.COLUMN_TRIPS,dData.getTrips());
            mDatabase.insert(DrivingDataTable.TABLE_NAME_DRIVING_DATA, null, contentValues);

        }
    }


    // Delete all the data.
    public void deleteAllDataForTable(String aTableName) {
        mDatabase.delete(aTableName, "", null);
    }

    /*private String getDateTime(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(dateString);
    }*/

    /*public long insertAlertLogs(AlertData alertData){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MaintenanceAlertTable.COLUMN_SERVICE_TYPE,alertData.getType());
        contentValues.put(MaintenanceAlertTable.COLUMN_ALERT_NAME,alertData.getName());
        contentValues.put(MaintenanceAlertTable.COLUMN_DESCRIPTION,alertData.getDetail());
        contentValues.put(MaintenanceAlertTable.COLUMN_DATE,alertData.getTime());
        contentValues.put(MaintenanceAlertTable.COLUMN_MILEAGE_INTERVAL,alertData.getMileage_interval());
        contentValues.put(MaintenanceAlertTable.COLUMN_USER_SELECTION,alertData.getUserSelection());

        long retVal = mDatabase.insert(MaintenanceAlertTable.TABLE_NAME_MAINTENANCE_ALERT,null,contentValues);

        Log.d(TAG,"maintenance alert inserted successfully");
        return retVal;
    }*/

    @Deprecated
    public ArrayList<MaintenanceLogData> getMaintenanceLogs(){
        String query = String.format("SELECT * FROM '%s'", MaintenanceLogTable.TABLE_NAME_MAINTENANCE);
        Cursor cursor = mDatabase.rawQuery(query,null);
        ArrayList<MaintenanceLogData> logList = null;
        if(cursor != null && cursor.getCount() > 0){
            logList = new ArrayList<MaintenanceLogData>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                MaintenanceLogData maintenance = new MaintenanceLogData();
                maintenance.setServiceType(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_SERVICE_TYPE)));
                maintenance.setServiceName(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_ALERT_NAME)));
                maintenance.setDescription(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DESCRIPTION)));
                maintenance.setCost("" + cursor.getDouble(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_COST)));
                maintenance.setMileage("" + cursor.getDouble(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MILEAGE)));
                maintenance.setServiceDate(cursor.getLong(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DATE)));
                logList.add(maintenance);
                cursor.moveToNext();
            }
            Log.d(TAG, "$$$ maintenance logs size " + logList.size());
        }

        return logList;
    }

    /**
     * Use this method to get all the rows from a table
     * @param tableName
     * @return
     */
    public Cursor getAllData(String tableName, String aSortOrderColumnName, boolean aIsAscendingOrder){

        String query = String.format("SELECT * FROM '%s'",tableName);
        if(aSortOrderColumnName != null){
            if(aIsAscendingOrder){
                query = query + " ORDER BY "+ aSortOrderColumnName +" ASC";
            }else{
                query = query + " ORDER BY "+ aSortOrderColumnName +" DESC";
            }
        }
       Log.d(TAG,"$$$ Query "+query);

        return mDatabase.rawQuery(query, null);
    }

    /**
     * Use this method to update the cursor adapter  with table name and sorting orders
     * @param tableName
     * @param aIsAscendingOrder
     * @param columns
     * @return
     */
    public Cursor getAllData(String tableName, boolean[] aIsAscendingOrder, String... columns){
        String query = DBUtils.generateSortQuery(String.format("SELECT * FROM '%s'",tableName),aIsAscendingOrder,columns);
        return mDatabase.rawQuery(query, null);
    }

    /**
     * Use this method to update the cursor adapter if you have any initial query, with table name and sorting orders
     *
     * @param tableName
     * @param initialQuery
     * @param aIsAscendingOrder
     * @param columns
     * @return Cursor
     */
    public Cursor getAllData(String tableName,String initialQuery,boolean[] aIsAscendingOrder, String... columns){
        String query = DBUtils.generateSortQuery(String.format(initialQuery,tableName),aIsAscendingOrder,columns);
        return mDatabase.rawQuery(query, null);
    }

    /**
     * Use this method to get single value from a table based on query
     * @param query
     * @return
     */
    public String getDataFromQuery(String query){

        String retVal = null;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
                retVal = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
        }

        return retVal;
    }


//    public Cursor getCursorForCompletedRecalls() {
//        return  mDatabase.rawQuery("SELECT * FROM 'recall' WHERE date_completed > 0 ORDER BY  component_name DESC,  date_issued ASC", null);
//    }
//
//    public Cursor getCursorForUnCompletedRecalls() {
//        return  mDatabase.rawQuery("SELECT * FROM 'recall' WHERE date_completed <= 0 ORDER BY  component_name DESC,  date_issued ASC", null);
//    }
    /**
     *  Use this method to get data from any particular column of a table
     * @param tableName
     * @param columnName
     * @return
     */
    public ArrayList<String> getDataFromColumn(String tableName, String columnName){
        ArrayList<String> retList = new ArrayList<String>();
        String query = String.format("SELECT %s FROM '%s'",columnName,tableName);
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String srt = cursor.getString(cursor.getColumnIndex(columnName));
                retList.add(srt);
                cursor.moveToNext();
            }
        }
        return retList;
    }



    public MaintenanceLogData getMaintenanceLog(int id){
        MaintenanceLogData maintenance = null;
        //int _id = getIdFromListPosition(position, MaintenanceLogTable.TABLE_NAME_MAINTENANCE, MaintenanceLogTable.COLUMN_MAINTENANCE_Id);
        String query = String.format("SELECT * FROM '%s' where %s = %d", MaintenanceLogTable.TABLE_NAME_MAINTENANCE,
                MaintenanceLogTable.COLUMN_MAINTENANCE_Id,id);
        Log.d(TAG, "query " + query);
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            maintenance = new MaintenanceLogData();
            maintenance.setServiceType(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_SERVICE_TYPE)));
            maintenance.setServiceName(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_ALERT_NAME)));
            maintenance.setDescription(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DESCRIPTION)));
            maintenance.setCost(""+cursor.getDouble(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_COST)));
            maintenance.setMileage(""+cursor.getLong(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MILEAGE)));
            maintenance.setServiceDate(cursor.getLong(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DATE)));
            maintenance.setServiceID(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MAINTENANCE_ID_ON_SERVER)));
          //  maintenance.setDate(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DATE)));
        }
        return maintenance;
    }

    public MaintenanceLogData getMaintenanceLog(Cursor cursor) {
        MaintenanceLogData maintenance;
        if (cursor == null)
            return null;
        maintenance = new MaintenanceLogData();
        maintenance.setServiceType(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_SERVICE_TYPE)));
        maintenance.setServiceName(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_ALERT_NAME)));
        maintenance.setDescription(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DESCRIPTION)));
        maintenance.setCost(""+cursor.getDouble(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_COST)));
        maintenance.setMileage(""+cursor.getLong(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MILEAGE)));
        maintenance.setServiceDate(cursor.getLong(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DATE)));
        maintenance.setServiceID(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MAINTENANCE_ID_ON_SERVER)));
        maintenance.setServiceCenter(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_SERVICE_CENTER)));
        return maintenance;
    }
    /*public ArrayList<AlertData> getAllMaintenanceRemainder(){
        ArrayList<AlertData> retList = null;
        Cursor cursor = getAllData(MaintenanceAlertTable.TABLE_NAME_MAINTENANCE_ALERT, new boolean[]{true}, MaintenanceAlertTable.COLUMN_ALERT_NAME);
        if(cursor != null && cursor.getCount() > 0){
            retList = new ArrayList<AlertData>();
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                AlertData alertData = new AlertData();
                alertData.setType(cursor.getString(cursor.getColumnIndex(MaintenanceAlertTable.COLUMN_SERVICE_TYPE)));
                retList.add(alertData);

                cursor.moveToNext();
            }
        }
        return retList;
    }*/

    public DrivingChartData getCategoryDrivingData(String query,long startDate,DrivingDataCategory category,int numberOfDays){
        DrivingChartData chartData = new DrivingChartData();
        DrivingDataTransaction drivingDataTransaction = new DrivingDataTransaction();
        ArrayList<Double> ttlMilesPerDay = drivingDataTransaction.getTotalMilesPerDay1(mDatabase,startDate,numberOfDays);
//        Log.d(TAG,"$$$ ttlMilesPerDay "+ttlMilesPerDay);
        chartData.setTotalMiles(ttlMilesPerDay);

        ArrayList<Double> maxSpeedPerDay = drivingDataTransaction.getMaxSpeedPerDay(mDatabase,startDate,numberOfDays);
//        Log.d(TAG,"$$$ maxSpeed per dat "+ maxSpeedPerDay);
        chartData.setMaxSpeeds(maxSpeedPerDay);

        ArrayList<Double> totalTimeHours = drivingDataTransaction.getTotalHoursPerDay(mDatabase, startDate, numberOfDays);
        chartData.setTotalHoursDays(totalTimeHours);
//        Log.d(TAG,"$$$ totalHours per day"+totalTimeHours);

        ArrayList<Double> averageTripPerDay = drivingDataTransaction.getAverageTripPerDay(chartData);
        chartData.setAverageTrip(averageTripPerDay);
//        Log.d(TAG,"$$$ averageTripDay "+averageTripPerDay);


//               get city gallons per day
        ArrayList<Double> cityGallonsPerDay = drivingDataTransaction.getCityGallonsPerDay(mDatabase,startDate,numberOfDays);
        chartData.setCityGallons(cityGallonsPerDay);
//        Log.d(TAG,"$$$ cityGallons "+cityGallonsPerDay);
//               get highway gallons per day
        ArrayList<Double> highwayGallonsPerDay = drivingDataTransaction.getHighwayGallonsPerDay(mDatabase,startDate,numberOfDays);
//        Log.d(TAG,"$$$ highway gallons "+highwayGallonsPerDay);
        chartData.setHighwayGallons(highwayGallonsPerDay);
//             get ethanol city gallons
        ArrayList<Double> ethanolCityGallons = drivingDataTransaction.getEthanolCityGallonsPerDay(mDatabase,startDate,numberOfDays);
//        Log.d(TAG,"$$$ ethanol city gallons "+ethanolCityGallons);
        chartData.setEthanolCityGallons(ethanolCityGallons);
//        get ethanol highway gallons
        ArrayList<Double> ethanolHighwayGallons = drivingDataTransaction.getEthanolHighwayGallonsPerDay(mDatabase,startDate,numberOfDays);
//        Log.d(TAG,"$$$ ethanol highway gallons "+ethanolHighwayGallons);
        chartData.setEthanolHighwayGallons(ethanolHighwayGallons);


//        get carbon foot print
        ArrayList<Double> carbonFootPrints = drivingDataTransaction.getCarbonFootPrintsPerDay(mDatabase,startDate,numberOfDays);
//        Log.d(TAG,"$$$ carbonFootPrints "+carbonFootPrints);
        chartData.setCarbonFootPrints(carbonFootPrints);

//        get city miles
        ArrayList<Double> cityMiles = drivingDataTransaction.getCityMilesPerDay(mDatabase,startDate,numberOfDays);
//        Log.d(TAG,"$$$ cityMiles "+cityMiles);
        chartData.setCityMiles(cityMiles);

//        get highway miles
        ArrayList<Double> highwayMiles = drivingDataTransaction.getHighwayMilesPerDay(mDatabase, startDate, numberOfDays);
//        Log.d(TAG,"$$$ highwayMiles "+highwayMiles);
        chartData.setHighwayMiles(highwayMiles);


//        get city mpg

        ArrayList<Double> cityMPG = drivingDataTransaction.getCityMPGPerDay(chartData);
//        Log.d(TAG,"$$$ cityMPG "+cityMPG);
        chartData.setCityMPG(cityMPG);

//        get highway mpg

        ArrayList<Double> highwayMPG = drivingDataTransaction.getHighwayMPGPerDay(chartData);
//        Log.d(TAG,"$$$ highwayMPG "+highwayMPG);
        chartData.setHighwayMPG(highwayMPG);


        return chartData;
    }





    private DrivingData getDrivingData(Cursor cursor){
        DrivingData drivingData = new DrivingData();
        drivingData.setCarbonFootprintLbs(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_CARBON_FOOTPRINT_LBS)));
        drivingData.setCityGallons(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_CITY_GALLONS)));
        drivingData.setCityMiles(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_CITY_MILES)));
        drivingData.setDrivingDate(cursor.getLong(cursor.getColumnIndex(DrivingDataTable.COLUMN_DRIVING_DATE)));
        drivingData.setEthanolCityGallons(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_ETHANOL_CITY_GALLONS)));
        drivingData.setEthanolHighwayGallons(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_ETHANOL_HIGHWAY_GALLONS)));
        drivingData.setHighwayGallons(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_HIGHWAY_GALLONS)));
        drivingData.setHighwayMiles(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_HIGHWAY_MILES)));
        drivingData.setMaxSpeed(cursor.getDouble(cursor.getColumnIndex(DrivingDataTable.COLUMN_MAX_SPEED)));
        drivingData.setTotalTimeHours(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_TOTAL_TIME_HOURS)));
        drivingData.setTrips(cursor.getString(cursor.getColumnIndex(DrivingDataTable.COLUMN_TRIPS)));
        return drivingData;
    }

    public ArrayList<MaintenanceReminderData> getAllMaintenanceReminders() {
        ArrayList<MaintenanceReminderData> retList = null;
        Cursor cursor = getAllData(MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER, new boolean[]{true}, MaintenanceReminderTable.COLUMN_REMINDER_NAME);
        if(cursor != null && cursor.getCount() > 0){
            retList = new ArrayList<MaintenanceReminderData>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                MaintenanceReminderData reminderData = getMaintenanceReminder(cursor);
                if (reminderData != null) {
                    retList.add(reminderData);
                }
                cursor.moveToNext();
            }
        }
        return retList;
    }

    public  MaintenanceReminderData getMaintenanceReminder(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        MaintenanceReminderData reminderData = new MaintenanceReminderData();

        reminderData.setReminderDescription(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_DESC)));
        reminderData.setNotificationFlagSms(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_NOTIFICATION_FLAG_SMS))));
        reminderData.setServiceType(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_SERVICE_TYPE)));
        reminderData.setReminderDate(cursor.getLong(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_DATE)));
        reminderData.setReminderConfigMonths(cursor.getLong(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_CONFIG_MONTHS)));
        reminderData.setReminderConfigMiles(cursor.getDouble(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_CONFIG_MILES)));
        reminderData.setLastNotificationDate(cursor.getLong(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_LAST_NOTIFICATION_DATE)));
        reminderData.setSmsPhoneNumber(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_SMS_PHONE_NUMBER)));
        reminderData.setEmail(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_EMAIL)));
        reminderData.setReminderName(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_NAME)));
        reminderData.setServiceName(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_SERVICE_NAME)));
        reminderData.setLastOdometer(cursor.getDouble(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_LAST_ODO_METER)));
        reminderData.setNotificationFlagEmail(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_NOTIFICATION_FLAG_EMAIL))));
        reminderData.setReminderID(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_ID)));

        return reminderData;
    }

    public MaintenanceReminderData getMaintenanceReminder(int id) {

        String query = String.format("SELECT * FROM '%s' where %s = %d", MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER,
                MaintenanceReminderTable.COLUMN_MAINTENANCE_Id, id);
        Cursor cursor = mDatabase.rawQuery(query, null);
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        return getMaintenanceReminder(cursor);
    }

    public ContentValues getContentValuesForReminder(MaintenanceReminderData reminderData) {
        if (reminderData == null)
            return null;

        ContentValues contentValues = new ContentValues();

        contentValues.put(MaintenanceReminderTable.COLUMN_REMINDER_ID, reminderData.getReminderID());
        contentValues.put(MaintenanceReminderTable.COLUMN_REMINDER_DESC, reminderData.getReminderDescription());
        contentValues.put(MaintenanceReminderTable.COLUMN_NOTIFICATION_FLAG_SMS, Boolean.toString(reminderData.isNotificationFlagSms()));
        contentValues.put(MaintenanceReminderTable.COLUMN_SERVICE_TYPE, reminderData.getServiceType());
        contentValues.put(MaintenanceReminderTable.COLUMN_REMINDER_DATE, reminderData.getReminderDate());
        contentValues.put(MaintenanceReminderTable.COLUMN_REMINDER_CONFIG_MILES, reminderData.getReminderConfigMiles());
        contentValues.put(MaintenanceReminderTable.COLUMN_LAST_NOTIFICATION_DATE, reminderData.getLastNotificationDate());
        contentValues.put(MaintenanceReminderTable.COLUMN_SMS_PHONE_NUMBER, reminderData.getSmsPhoneNumber());
        contentValues.put(MaintenanceReminderTable.COLUMN_EMAIL, reminderData.getEmail());
        contentValues.put(MaintenanceReminderTable.COLUMN_REMINDER_NAME, reminderData.getReminderName());
        contentValues.put(MaintenanceReminderTable.COLUMN_SERVICE_NAME, reminderData.getServiceName());
        contentValues.put(MaintenanceReminderTable.COLUMN_LAST_ODO_METER, reminderData.getLastOdometer());
        contentValues.put(MaintenanceReminderTable.COLUMN_NOTIFICATION_FLAG_EMAIL, Boolean.toString(reminderData.isNotificationFlagEmail()));
        contentValues.put(MaintenanceReminderTable.COLUMN_REMINDER_CONFIG_MONTHS, reminderData.getReminderConfigMonths());

        return  contentValues;
    }
    /*public AlertData getMaintenanceAlerts(int aId){
        AlertData alertData = null;
        //int _id = getIdFromListPosition(position, MaintenanceAlertTable.TABLE_NAME_MAINTENANCE_ALERT, MaintenanceAlertTable.COLUMN_MAINTENANCE_Id);
        String query = String.format("SELECT * FROM '%s' where %s = %d", MaintenanceAlertTable.TABLE_NAME_MAINTENANCE_ALERT,
                MaintenanceAlertTable.COLUMN_MAINTENANCE_Id,aId);
        Log.d(TAG,"query "+query);
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
//            alertData = new AlertData();
            String serviceType = cursor.getString(cursor.getColumnIndex(MaintenanceAlertTable.COLUMN_SERVICE_TYPE));
            String alertName = cursor.getString(cursor.getColumnIndex(MaintenanceAlertTable.COLUMN_ALERT_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(MaintenanceAlertTable.COLUMN_DESCRIPTION));
            long time = cursor.getLong(cursor.getColumnIndex(MaintenanceAlertTable.COLUMN_DATE));
            int mileageInterval = cursor.getInt(cursor.getColumnIndex(MaintenanceAlertTable.COLUMN_MILEAGE_INTERVAL));
            String userSelection = cursor.getString(cursor.getColumnIndex(MaintenanceAlertTable.COLUMN_USER_SELECTION));
            System.out.println("$$$ user selection now "+userSelection);
            alertData = new AlertData(serviceType,alertName,desc,time,"",mileageInterval,userSelection);
        }
        return alertData;
    }*/

    public void updateMaintenanceLog(int aId,MaintenanceLogData maintenance){
        //int _id = getIdFromListPosition(position, MaintenanceLogTable.TABLE_NAME_MAINTENANCE, MaintenanceLogTable.COLUMN_MAINTENANCE_Id);
        ContentValues args = new ContentValues();
        args.put(MaintenanceLogTable.COLUMN_SERVICE_TYPE,maintenance.getServiceType());
        args.put(MaintenanceLogTable.COLUMN_ALERT_NAME,maintenance.getServiceName());
        args.put(MaintenanceLogTable.COLUMN_DESCRIPTION,maintenance.getDescription());
        args.put(MaintenanceLogTable.COLUMN_COST,maintenance.getCost());
        args.put(MaintenanceLogTable.COLUMN_MILEAGE,maintenance.getMileage());
        args.put(MaintenanceLogTable.COLUMN_DATE,maintenance.getServiceDate());
        //args.put(MaintenanceLogTable.COLUMN_DATE,formatDateAsLong(maintenance.getDate()));
        mDatabase.update(MaintenanceLogTable.TABLE_NAME_MAINTENANCE,args, MaintenanceLogTable.COLUMN_MAINTENANCE_Id+" = ? ",
                new String[]{String.valueOf(aId)});
        Log.i(TAG,"log updated");
    }

    /*public void updateMaintenanceAlertLog(int aId,AlertData data){
        //int _id = getIdFromListPosition(position, MaintenanceAlertTable.TABLE_NAME_MAINTENANCE_ALERT, MaintenanceAlertTable.COLUMN_MAINTENANCE_Id);
        ContentValues args = getContentValues(data);
        mDatabase.update(MaintenanceAlertTable.TABLE_NAME_MAINTENANCE_ALERT,args,MaintenanceAlertTable.COLUMN_MAINTENANCE_Id+" = ?",
                new String[]{String.valueOf(aId)});
        Log.i(TAG,"Maintenance alert log updated");

    }*/

    public void updateMaintenanceReminder(int aId,MaintenanceReminderData data){
        //int _id = getIdFromListPosition(position, MaintenanceAlertTable.TABLE_NAME_MAINTENANCE_ALERT, MaintenanceAlertTable.COLUMN_MAINTENANCE_Id);
        ContentValues args = getContentValuesForReminder(data);
        mDatabase.update(MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER,args,MaintenanceReminderTable.COLUMN_MAINTENANCE_Id+" = ?",
                new String[]{String.valueOf(aId)});
        Log.i(TAG,"Maintenance alert log updated");

    }

   /* private ContentValues getContentValues(BaseData data){
        ContentValues contentValues = new ContentValues();
        if(data instanceof AlertData) {
            AlertData alertData = (AlertData)data;
            contentValues.put(MaintenanceAlertTable.COLUMN_SERVICE_TYPE, alertData.getType());
            contentValues.put(MaintenanceAlertTable.COLUMN_ALERT_NAME, alertData.getName());
            contentValues.put(MaintenanceAlertTable.COLUMN_DESCRIPTION, alertData.getDetail());
            contentValues.put(MaintenanceAlertTable.COLUMN_DATE, alertData.getTime());
            contentValues.put(MaintenanceAlertTable.COLUMN_MILEAGE_INTERVAL, alertData.getMileage_interval());
            contentValues.put(MaintenanceAlertTable.COLUMN_USER_SELECTION,alertData.getUserSelection());
        }
        return contentValues;
    }*/


    /**
     * Use this method to remove a row based on position from a list view
     *
     * @param aId
     */
    public void removeRowForIdAndColumn(int aId,String tableName,String columnId){
        //int _id = getIdFromListPosition(position, tableName,columnId);
        System.out.println("$$$ _id "+aId);
        mDatabase.delete(tableName, columnId + " = ?",
                new String[]{String.valueOf(aId)});
    }

    /**
     * Use this method to get the id of a for a specific list position.
     *
     * @param position
     * @param tableName
     * @param columnId
     * @return
     */
    private int getIdFromListPosition(int position,String tableName,String columnId){
        String countQuery = String.format("SELECT * FROM '%s'",tableName);
        int newPos = position;
        int counter = 0;
        int _id = 0;
        Cursor cursor = mDatabase.rawQuery(countQuery,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(counter == newPos){
                    _id = cursor.getInt(cursor.getColumnIndex(columnId));
                }
                counter++;
                cursor.moveToNext();
            }
        }
        return _id;
    }

    public long insertAccount(Accounts aAccount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountTable.COLUMN_ACCOUNT_ID_SERVER, aAccount.getAccountIdServer());
        contentValues.put(AccountTable.COLUMN_VEHICLE_DES_, aAccount.getVehicleDesc());
        long retVal = mDatabase.insert(AccountTable.TABLE_NAME_ACCOUNT, null, contentValues);
        Log.d(TAG, "Account inserted successfully");
        return retVal;
    }

    // populate with default service type.
    public long insertServiceType(ServiceType aServiceType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ServiceTypesTable.COLUMN_SERVICE_NAME, aServiceType.getName());
        long retVal = mDatabase.insert(ServiceTypesTable.TABLE_NAME_SERVICE_TYPES, null, contentValues);
        Log.d(TAG, "ServiceType inserted successfully");
        return retVal;
    }

    public long insertLogServiceType(BaseData baseData){
        ContentValues contentValues = getGenericContentValues(baseData,LogServiceTypesTable.class);
        return mDatabase.insert(LogServiceTypesTable.TABLE_NAME_LOG_SERVICE_TYPES,null,contentValues);
    }

    public long insertAlertServiceType(BaseData baseData){
        ContentValues contentValues = getGenericContentValues(baseData,AlertServiceTypeTable.class);
        return mDatabase.insert(AlertServiceTypeTable.TABLE_NAME_SERVICE_TYPE_ALERT,null,contentValues);
    }



    public ContentValues getGenericContentValues(BaseData baseData,Class<? extends BaseTable> cls){
        ContentManagerImpl<BaseData> cntImpl = new ContentManagerImpl<BaseData>();
        return cntImpl.generateContent(baseData, cls,IgnoreColumn.COLUMN_Id);
    }

    public void insertBulkMaintenanceReminders(List<MaintenanceReminderData> reminders) {
        for(MaintenanceReminderData reminderData:reminders) {
            long retVal = insertMaintenanceReminder(reminderData);
            Log.d(TAG, "maintenance reminder inserted status : "+retVal);
        }
    }

    public long insertMaintenanceReminder(MaintenanceReminderData reminderData) {
        ContentValues contentValues = getContentValuesForReminder(reminderData);
        long retVal = mDatabase.insert(MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER,null,contentValues);
        Log.d(TAG,reminderData.getReminderID()+" maintenance reminder inserted status "+retVal);
        return retVal;
    }

    // Recall database apis
    public void insertBulkRecalls(List<RecallData> recalls) {
        for(RecallData recallData:recalls) {
           insertRecall(recallData);
//            Log.d(TAG, "recall inserted status : "+retVal);
        }
    }

    public long insertRecall(RecallData recallData) {
        ContentValues contentValues = getContentValuesForRecall(recallData);
        if (contentValues != null) {
            long retVal = mDatabase.insert(RecallTable.TABLE_NAME_RECALL, null, contentValues);
//            Log.d(TAG,recallData.getRecallID()+" recall inserted status "+retVal);
            return retVal;
        }
        return -1;
    }

    private ContentValues getContentValuesForRecall(RecallData recallData) {
        if (recallData == null)
            return null;

        ContentValues contentValues = new ContentValues();

        contentValues.put(RecallTable.COLUMN_RecallID, recallData.getRecallID());
        contentValues.put(RecallTable.COLUMN_ComponentName, recallData.getComponentName());
        contentValues.put(RecallTable.COLUMN_CorrectiveAction, recallData.getCorrectiveAction());
        contentValues.put(RecallTable.COLUMN_DefectDescription, recallData.getDefectDescription());
        contentValues.put(RecallTable.COLUMN_Manufacturer, recallData.getManufacturer());
        contentValues.put(RecallTable.COLUMN_ManufacturingBeginDate, recallData.getManufacturingBeginDate());
        contentValues.put(RecallTable.COLUMN_ManufacturingEndDate, recallData.getManufacturingEndDate());
        contentValues.put(RecallTable.COLUMN_PotentialAffectedUnits, recallData.getPotentialAffectedUnits());
        contentValues.put(RecallTable.COLUMN_Summary, recallData.getSummary());
        contentValues.put(RecallTable.COLUMN_ComponentNumber, recallData.getComponentNumber());
        contentValues.put(RecallTable.COLUMN_DateIssued, recallData.getDateIssued());
        contentValues.put(RecallTable.COLUMN_DateCompleted, recallData.getDateCompleted());

        return  contentValues;
    }

    public  RecallData getRecall(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        RecallData recallData = new RecallData();

        recallData.setRecallID(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_RecallID)));
        recallData.setComponentName(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_ComponentName)));
        recallData.setCorrectiveAction(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_CorrectiveAction)));
        recallData.setDefectDescription(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_DefectDescription)));
        recallData.setManufacturer(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_Manufacturer)));
        recallData.setManufacturingBeginDate(cursor.getLong(cursor.getColumnIndex(RecallTable.COLUMN_ManufacturingBeginDate)));
        recallData.setManufacturingEndDate(cursor.getLong(cursor.getColumnIndex(RecallTable.COLUMN_ManufacturingEndDate)));
        recallData.setPotentialAffectedUnits(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_PotentialAffectedUnits)));
        recallData.setSummary(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_Summary)));
        recallData.setComponentNumber(cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_ComponentNumber)));
        recallData.setDateIssued(cursor.getLong(cursor.getColumnIndex(RecallTable.COLUMN_DateIssued)));
        recallData.setDateCompleted(cursor.getLong(cursor.getColumnIndex(RecallTable.COLUMN_DateCompleted)));

        return recallData;
    }

    public RecallData getRecall(int id) {

        String query = String.format("SELECT * FROM '%s' where %s = %d", RecallTable.TABLE_NAME_RECALL,
                RecallTable.COLUMN_RECALL_LOCAL_REC_ID, id);
        Cursor cursor = mDatabase.rawQuery(query, null);
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        return getRecall(cursor);
    }
    // Recall database apis ended.

    // Health Status database apis
    public void insertBulkHealthStatusRecords(List<HealthStatus> healthStatusList) {
        for (HealthStatus healthStatus : healthStatusList) {
            insertHealthStatus(healthStatus);
            insertDiagnosticInfo(healthStatus.getDiagnosticInfo(), healthStatus.getServiceArea());
        }
    }

    public long insertHealthStatus(HealthStatus healthStatus) {
        ContentValues contentValues = getContentValuesForHealthStatus(healthStatus);
        if (contentValues != null) {
            long retVal = mDatabase.insert(VehicleHealthStatusTable.TABLE_NAME_HEALTH_STATUS, null, contentValues);
            return retVal;
        }
        return -1;
    }

    public void insertDiagnosticInfo(ArrayList<DiagnosticInfo> diagnosticInfo, String serviceArea) {

        if (diagnosticInfo != null) {
            ContentValues contentValues = new ContentValues();
            for (DiagnosticInfo dtcInfo : diagnosticInfo) {
                contentValues.put(DiagnosticInfoTable.COLUMN_SERVICE_AREA, serviceArea);
                contentValues.put(DiagnosticInfoTable.COLUMN_DTC_CODE, dtcInfo.getCode());
                contentValues.put(DiagnosticInfoTable.COLUMN_DTC_DESC, dtcInfo.getDescription());
                mDatabase.insert(DiagnosticInfoTable.TABLE_NAME_DIAGNOSTIC_INFO, null, contentValues);
            }
        }

    }

    private ContentValues getContentValuesForHealthStatus(HealthStatus healthStatus) {
        if (healthStatus == null)
            return null;

        ContentValues contentValues = new ContentValues();
        contentValues.put(VehicleHealthStatusTable.COLUMN_ServiceArea, healthStatus.getServiceArea());
        contentValues.put(VehicleHealthStatusTable.COLUMN_ServiceAreaStatus, healthStatus.getServiceAreaStatus());
        return  contentValues;
    }

    private ContentValues getContentValuesForDiagnosticInfo(DiagnosticInfo diagnosticInfo,String serviceArea) {
        if (diagnosticInfo == null)
            return null;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DiagnosticInfoTable.COLUMN_SERVICE_AREA, serviceArea);
        contentValues.put(DiagnosticInfoTable.COLUMN_DTC_CODE, diagnosticInfo.getCode());
        contentValues.put(DiagnosticInfoTable.COLUMN_DTC_DESC, diagnosticInfo.getDescription());
        return  contentValues;
    }

    public HealthStatus getHealthStatus(Cursor cursor) {
        if (cursor == null)
            return null;
        HealthStatus healthStatus = new HealthStatus();
        healthStatus.setServiceArea(cursor.getString(cursor.getColumnIndex(VehicleHealthStatusTable.COLUMN_ServiceArea)));
        healthStatus.setServiceAreaStatus(cursor.getString(cursor.getColumnIndex(VehicleHealthStatusTable.COLUMN_ServiceAreaStatus)));
        return healthStatus;
    }

    public DiagnosticInfo getDiagnosticInfo(Cursor cursor) {
        if (cursor == null)
            return null;
        DiagnosticInfo diagnosticInfo = new DiagnosticInfo();
        diagnosticInfo.setCode(cursor.getString(cursor.getColumnIndex(DiagnosticInfoTable.COLUMN_DTC_CODE)));
        diagnosticInfo.setDescription(cursor.getString(cursor.getColumnIndex(DiagnosticInfoTable.COLUMN_DTC_DESC)));
        return diagnosticInfo;
    }

    private ArrayList<DiagnosticInfo> getDiagnosticInfoList(String serviceArea) {
        String diagnosticQuery = String.format("SELECT * FROM '%s' WHERE '%s' = '%s'", DiagnosticInfoTable.TABLE_NAME_DIAGNOSTIC_INFO, DiagnosticInfoTable.COLUMN_SERVICE_AREA, serviceArea);
        Cursor dtcCursor = mDatabase.rawQuery(diagnosticQuery, null);
        if (dtcCursor == null || dtcCursor.getCount() <= 0) {
            return null;
        }

        ArrayList<DiagnosticInfo> diagnosticInfoList = new ArrayList<DiagnosticInfo>();
        if (dtcCursor.getCount() > 0) {
            dtcCursor.moveToFirst();
            do {
                DiagnosticInfo diagnosticInfo = getDiagnosticInfo(dtcCursor);
                if (diagnosticInfo != null)
                    diagnosticInfoList.add(diagnosticInfo);
            } while (dtcCursor.moveToNext());


        }
        return diagnosticInfoList;
    }

    public List<HealthStatus> getHealthStatusRecords() {

        String query = String.format("SELECT * FROM '%s'", VehicleHealthStatusTable.TABLE_NAME_HEALTH_STATUS);
        Cursor cursor = mDatabase.rawQuery(query, null);
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        List<HealthStatus> healthStatusList = new ArrayList<HealthStatus>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                HealthStatus healthStatus = getHealthStatus(cursor);
                ArrayList<DiagnosticInfo> diagnosticInfoList = getDiagnosticInfoList(healthStatus.getServiceArea());
                if (diagnosticInfoList != null) {
                    healthStatus.setDiagnosticInfo(diagnosticInfoList);
                }
                if (healthStatus != null)
                    healthStatusList.add(healthStatus);
            } while (cursor.moveToNext());

        }


        return healthStatusList;
    }
    // Health Status database apis ended.
}
