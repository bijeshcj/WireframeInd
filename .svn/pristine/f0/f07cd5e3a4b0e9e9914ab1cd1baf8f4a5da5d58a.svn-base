package com.verizontelematics.indrivemobile.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.verizontelematics.indrivemobile.database.tables.DrivingDataTable;
import com.verizontelematics.indrivemobile.models.uimodels.DrivingChartData;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by bijesh on 12/12/2014.
 */
public class DrivingDataTransaction {


    private static final String TAG = DrivingDataTransaction.class.getCanonicalName();

    public  ArrayList<Double> getTotalMilesPerDay1(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        Log.d(TAG,"$$$ startDate "+startDate);
        long previousDate = startDate,currentDate = 0;
        boolean isFirst = true;
        String query = "select drivingDate,cityMiles,highwayMiles from driving_data";
        ArrayList<Double> retList = new ArrayList<Double>();

        Cursor cursor = mDatabase.rawQuery(query, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(isFirst) {

                    currentDate = Long.parseLong(cursor.getString(cursor.getColumnIndex("drivingDate")));


//                    Log.d(TAG, " $$$ previous drivingDate " + previousDate);
                    double cityMiles = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex("cityMiles"))));
                    double highWayMiles = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex("highwayMiles"))));
                    int interval = compareDates(previousDate,currentDate);
                    if(interval > 0) {
                        for (int i = 0; i < interval; i++) {
                            retList.add(0.0);
                        }
                        retList.add(cityMiles + highWayMiles);
                    }else {
                        retList.add(cityMiles + highWayMiles);
                    }
                    isFirst = false;

                    previousDate = currentDate;
                }else{

                    currentDate = Long.parseLong(cursor.getString(cursor.getColumnIndex("drivingDate")));

                    double cityMiles = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex("cityMiles"))));
                    double highWayMiles = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex("highwayMiles"))));


//                    if(!isFirst){
//                    add other day values to list

                    int interval = compareDates(previousDate,currentDate);
                    for(int i=1;i<interval;i++){
                        retList.add(0.0);
                    }
//                    }
                    retList.add(cityMiles + highWayMiles);
                    previousDate = currentDate;


                }


                cursor.moveToNext();
            }
        }

//        append zero records if number of days not equal
        int diff = howManyDays - retList.size();
        for(int i=0;i<diff;i++)
            retList.add(0.0);

//        retList.add(0.0);
//        retList.add(0.0);
//        retList.add(0.0);
//        retList.add(0.0);
        return retList;
    }

    public ArrayList<Double> getMaxSpeedPerDay(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+ DrivingDataTable.COLUMN_DRIVING_DATE +", "+ DrivingDataTable.COLUMN_MAX_SPEED +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query, null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_MAX_SPEED);
        }
        return retList;
    }

    public ArrayList<Double> getTotalHoursPerDay(SQLiteDatabase mDatabase, long startDate, int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_TOTAL_TIME_HOURS +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_TOTAL_TIME_HOURS);
        }
        return retList;
    }

    public ArrayList<Double> getAverageTripPerDay(DrivingChartData drivingChartData){
        ArrayList<Double> retList = new ArrayList<Double>();
        if(drivingChartData != null){
            ArrayList<Double> totalMiles = drivingChartData.getTotalMiles();
            ArrayList<Double> totalTimeHours = drivingChartData.getTotalHoursDays();
            for(int i=0;i<totalMiles.size();i++){
                if(totalTimeHours.get(i) == 0)
                    retList.add(0.0);
                else {
                    double avgTripPerDay = totalMiles.get(i) / totalTimeHours.get(i);
                    retList.add(roundOf(avgTripPerDay));
                }
            }
        }
        return retList;
    }

    public ArrayList<Double> getCityGallonsPerDay(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_CITY_GALLONS +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_CITY_GALLONS);
        }
        return retList;
    }

    public ArrayList<Double> getEthanolCityGallonsPerDay(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_ETHANOL_CITY_GALLONS +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_ETHANOL_CITY_GALLONS);
        }
        return retList;
    }

    public ArrayList<Double> getHighwayGallonsPerDay(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_HIGHWAY_GALLONS +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_HIGHWAY_GALLONS);
        }
        return retList;
    }

    public ArrayList<Double> getEthanolHighwayGallonsPerDay(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_ETHANOL_HIGHWAY_GALLONS +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_ETHANOL_HIGHWAY_GALLONS);
        }
        return retList;
    }

    public ArrayList<Double> getCarbonFootPrintsPerDay(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_CARBON_FOOTPRINT_LBS +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_CARBON_FOOTPRINT_LBS);
        }
        return retList;
    }

    public ArrayList<Double> getCityMilesPerDay(SQLiteDatabase mDatabase,long startDate,int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_CITY_MILES +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_CITY_MILES);
        }
        return retList;
    }

    public ArrayList<Double> getHighwayMilesPerDay(SQLiteDatabase mDatabase, long startDate, int howManyDays){
        ArrayList<Double> retList = null;
        String query = "select "+DrivingDataTable.COLUMN_DRIVING_DATE +", "
                +DrivingDataTable.COLUMN_HIGHWAY_MILES +" from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor != null && cursor.getCount() > 0){
            retList = getDataWithMissingDates(cursor,startDate,howManyDays,DrivingDataTable.COLUMN_HIGHWAY_MILES);
        }
        return retList;
    }


    public ArrayList<Double> getCityMPGPerDay(DrivingChartData drivingChartData){
        ArrayList<Double> retList = new ArrayList<Double>();
        if(drivingChartData != null){
            ArrayList<Double> totalCityMiles = drivingChartData.getCityMiles();
            ArrayList<Double> totalCityGallons = drivingChartData.getCityGallons();
            for(int i=0;i< totalCityMiles.size();i++){
                if(totalCityGallons.get(i) == 0)
                    retList.add(0.0);
                else {
                    double avgTripPerDay = totalCityMiles.get(i) / totalCityGallons.get(i);
                    retList.add(roundOf(avgTripPerDay));
                }
            }
        }
        return retList;
    }

    public ArrayList<Double> getHighwayMPGPerDay(DrivingChartData drivingChartData){
        ArrayList<Double> retList = new ArrayList<Double>();
        if(drivingChartData != null){
            ArrayList<Double> totalHighwayMiles = drivingChartData.getHighwayMiles();
            ArrayList<Double> totalHighwayGallons = drivingChartData.getHighwayGallons();
            for(int i=0;i< totalHighwayMiles.size();i++){
                if(totalHighwayGallons.get(i) == 0)
                    retList.add(0.0);
                else {
                    double avgTripPerDay = totalHighwayMiles.get(i) / totalHighwayGallons.get(i);
                    retList.add(roundOf(avgTripPerDay));
                }
            }
        }
        return retList;
    }


    private ArrayList<Double> getDataWithMissingDates(Cursor cursor,long startDate,int howManyDays,String columnName){
        ArrayList<Double> retList = new ArrayList<Double>();
        long previousDate = startDate,currentDate = 0;
        boolean isFirst = true;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(isFirst){
                currentDate = Long.parseLong(cursor.getString(cursor.getColumnIndex("drivingDate")));
                double val1 = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(columnName))));
                int interval = compareDates(previousDate,currentDate);
                if(interval > 0) {
                    for (int i = 0; i < interval; i++) {
                        retList.add(0.0);
                    }
                    retList.add(val1);
                }else {
                    retList.add(val1);
                }
                isFirst = false;

                previousDate = currentDate;

            }else{

                currentDate = Long.parseLong(cursor.getString(cursor.getColumnIndex("drivingDate")));


                double val1 = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(columnName))));

//                    if(!isFirst){
//                    add other day values to list

                int interval = compareDates(previousDate,currentDate);
                for(int i=1;i<interval;i++){
                    retList.add(0.0);
                }
//                    }
                retList.add(val1);
                previousDate = currentDate;


            }
            cursor.moveToNext();
        }
        //        append zero records if number of days not equal
        int diff = howManyDays - retList.size();
        for(int i=0;i<diff;i++)
            retList.add(0.0);
        return retList;
    }


    private ArrayList<Double> getDataWithMissingDates(Cursor cursor,long startDate,int howManyDays,String columnName1,String columnName2){
        ArrayList<Double> retList = new ArrayList<Double>();
        long previousDate = startDate,currentDate = 0;
        boolean isFirst = true;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(isFirst){
                currentDate = Long.parseLong(cursor.getString(cursor.getColumnIndex("drivingDate")));
                double val1 = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(columnName1))));
                double val2 = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(columnName2))));
                int interval = compareDates(previousDate,currentDate);
                if(interval > 0) {
                    for (int i = 0; i < interval; i++) {
                        retList.add(0.0);
                    }
                    retList.add(val1+val2);
                }else {
                    retList.add(val1+val2);
                }
                isFirst = false;
                Log.d(TAG,"$$ end of first condition list is "+retList);
                previousDate = currentDate;

            }else{
                Log.d(TAG,"$$$ else condition");
                currentDate = Long.parseLong(cursor.getString(cursor.getColumnIndex("drivingDate")));
                Log.d(TAG, " $$$ current drivingDate " + currentDate);

                double val1 = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(columnName1))));
                double val2 = roundOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(columnName2))));
//                    if(!isFirst){
//                    add other day values to list
                Log.d(TAG,"$$$ comparing current date "+currentDate+" previousDate "+previousDate);
                int interval = compareDates(previousDate,currentDate);
                for(int i=1;i<interval;i++){
                    retList.add(0.0);
                }
//                    }
                retList.add(val1+val2);
                previousDate = currentDate;
                Log.d(TAG,"$$ end of else condition list is "+retList);

            }
            cursor.moveToNext();
        }
        //        append zero records if number of days not equal
        int diff = howManyDays - retList.size();
        for(int i=0;i<diff;i++)
            retList.add(0.0);
        return retList;
    }



    private int compareDates(long previousDate,long currentDate){
        DateTime pre = new DateTime(previousDate);
        DateTime cur = new DateTime(currentDate);
        Days days = Days.daysBetween(pre,cur);
//        System.out.println("$$$ day difference "+days.getDays());
        return days.getDays();
    }




    private  double roundOf(double value){
        DecimalFormat df2 = new DecimalFormat("#####.##");
        return Double.valueOf(df2.format(value));
    }





}
