package com.verizontelematics.indrivemobile.utils.ui;

import android.util.Log;

import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.DrivingDataTable;
import com.verizontelematics.indrivemobile.models.uimodels.DrivingChartData;

import java.text.DecimalFormat;

/**
 * Created by bijesh on 12/11/2014.
 */
public enum DrivingDataImpl {INS;

    private static final String TAG = DrivingDataImpl.class.getCanonicalName();

    private static final String QUERY_MAX_SPEED = "select max("+ DrivingDataTable.COLUMN_MAX_SPEED +") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
    private static final String QUERY_TOTAL_CITY_MILES = "select sum("+DrivingDataTable.COLUMN_CITY_MILES +") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
    private static final String QUERY_TOTAL_HIGHWAY_MILES = "select sum("+DrivingDataTable.COLUMN_HIGHWAY_MILES +") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
    private static final String QUERY_TOTAL_TRIPS = "select sum("+DrivingDataTable.COLUMN_TRIPS+") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
    private static final String QUERY_TOTAL_CITY_GALLONS = "select sum("+DrivingDataTable.COLUMN_CITY_GALLONS +") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
    private static final String QUERY_TOTAL_HIGHWAY_GALLONS = "select sum("+DrivingDataTable.COLUMN_HIGHWAY_GALLONS +") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
    private static final String QUERY_TOTAL_ETHANOL_CITY_GALLONS = "select sum("+DrivingDataTable.COLUMN_ETHANOL_CITY_GALLONS +") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;
    private static final String QUERY_TOTAL_ETHANOL_HIGHWAY_GALLONS = "select sum("+DrivingDataTable.COLUMN_ETHANOL_HIGHWAY_GALLONS +") from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA;

    double maxSpeed=0,totalCityMiles=0,totalHighwayMiles=0, totalTrips =0,totalCityGallons=0,totalHighwayGallons=0,totalEthanolCityGallons=0,totalEthanolHighwayGallons = 0;

//    query for chart calculations
//    private static final String QUERY_FOR_PARTICULAR_DATE_ASC = "select * from "+DrivingDataTable.TABLE_NAME_DRIVING_DATA+
//        " where '"+DrivingDataTable.COLUMN_DRIVING_DATE+"' >= AND "+DrivingDataTable.COLUMN_DRIVING_DATE+
//        " "+" ORDER BY drivingDate ASC";



    public double[] loadDrivingDataUI(StorageTransaction transaction){
        //Total Miles comprising of Highway and city miles
        String str = getTotalCityMiles(transaction);
        totalCityMiles = (str == null) ? 0.0f : Double.parseDouble(str);

        str = getTotalHighwayMiles(transaction);
        totalHighwayMiles = (str == null) ? 0.0f : Double.parseDouble(str);
        double totalMiles = totalHighwayMiles + totalCityMiles; // BRule: 210207 : not round here on UI will take care how to show.

        //max speed
        str = getMaxSpeed(transaction);
        maxSpeed = (str == null) ? 0.0f : Double.parseDouble(str);
        maxSpeed= roundOf(maxSpeed);

        //Average for entire trip
        str=getTotalTrips(transaction);
        totalTrips = (str == null) ? 0.0f : Double.parseDouble(str);

        double avgTrip = roundOf(totalMiles / totalTrips); // BRule: 210329

        //carbon footprint calculation using city and highway gallons consumed
        str=getTotalCityGallons(transaction);
        totalCityGallons = (str == null) ? 0.0f : Double.parseDouble(str);
        str=getTotalHighwayGallons(transaction);
        totalHighwayGallons = (str == null) ? 0.0f : Double.parseDouble(str);
        str=getTotalEthanolCityGallons(transaction);
        totalEthanolCityGallons = (str == null) ? 0.0f : Double.parseDouble(str);
        str=getTotalEthanolHighwayGallons(transaction);
        totalEthanolHighwayGallons = (str == null) ? 0.0f : Double.parseDouble(str);
        double totalGallons = totalCityGallons + totalHighwayGallons;
        double totalEthanolGallons = totalEthanolCityGallons + totalEthanolHighwayGallons;
//        Log.d(TAG,"totalGallons "+totalGallons+" totalEthanolGallons "+totalEthanolGallons+" total Miles "+totalMiles);
        double carbonFootPrint = (((totalGallons - totalEthanolGallons) * 19.54) + (totalEthanolGallons * 12.6)) / totalMiles;
//        take 5.2 formal digits  // BRule : 210330

        //cityMPG
        double cityMPG =  totalCityMiles / totalCityGallons;
        //HighwayMPG
        double highwayMPG = totalHighwayMiles / totalHighwayGallons;


//        Log.d(TAG, "totalMiles "+totalMiles+" maxSpeed "+maxSpeed+" avgTrip "+avgTrip+" carbonFootPrint "+carbonFootPrint+
//        " cityMPG "+cityMPG+" highwayMPG "+highwayMPG);

        double[] categoryData = {totalMiles,maxSpeed,avgTrip,carbonFootPrint,cityMPG,highwayMPG};
        return categoryData;
    }


    private DrivingChartData getChartData(StorageTransaction storageTransaction,DrivingDataCategory category,long startDate,long endDate) {
        String QUERY_FOR_PARTICULAR_DATE_ASC = "select * from " + DrivingDataTable.TABLE_NAME_DRIVING_DATA + " where " +
                DrivingDataTable.COLUMN_DRIVING_DATE + " >=  '" + startDate + "' AND " + DrivingDataTable.COLUMN_DRIVING_DATE + " <= '" +
                endDate + "' ORDER BY " + DrivingDataTable.COLUMN_DRIVING_DATE + " ASC";
//        Log.d(TAG, "$$$ query to get date for particular date " + QUERY_FOR_PARTICULAR_DATE_ASC);
        DrivingChartData chartData = storageTransaction.getCategoryDrivingData(QUERY_FOR_PARTICULAR_DATE_ASC,startDate, category,CalendarUtil.numberOfDaysInAMonth(startDate));


        return chartData;
    }

    private double roundOf(double value){
        try {


            DecimalFormat df2 = new DecimalFormat("####");
            return Double.valueOf(df2.format(value));
        }
        catch (Exception e)
        {
            return  0;
        }
    }

    private String getMaxSpeed(StorageTransaction storageTransaction){
         return storageTransaction.getDataFromQuery(QUERY_MAX_SPEED);
    }

    private String getTotalCityMiles(StorageTransaction storageTransaction){
         return storageTransaction.getDataFromQuery(QUERY_TOTAL_CITY_MILES);
    }

    private String getTotalHighwayMiles(StorageTransaction storageTransaction){
        return storageTransaction.getDataFromQuery(QUERY_TOTAL_HIGHWAY_MILES);
    }

    private String getTotalTrips(StorageTransaction storageTransaction){
        return storageTransaction.getDataFromQuery(QUERY_TOTAL_TRIPS);
    }

    private String getTotalCityGallons(StorageTransaction storageTransaction){
        return storageTransaction.getDataFromQuery(QUERY_TOTAL_CITY_GALLONS);
    }

    private String getTotalHighwayGallons(StorageTransaction storageTransaction){
        return storageTransaction.getDataFromQuery(QUERY_TOTAL_HIGHWAY_GALLONS);
    }

    private String getTotalEthanolCityGallons(StorageTransaction storageTransaction){
        return storageTransaction.getDataFromQuery(QUERY_TOTAL_ETHANOL_CITY_GALLONS);
    }

    private String getTotalEthanolHighwayGallons(StorageTransaction storageTransaction){
        return storageTransaction.getDataFromQuery(QUERY_TOTAL_ETHANOL_HIGHWAY_GALLONS);
    }


}
