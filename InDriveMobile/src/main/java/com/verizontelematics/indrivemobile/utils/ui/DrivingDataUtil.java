package com.verizontelematics.indrivemobile.utils.ui;

import android.util.Log;

import com.verizontelematics.indrivemobile.models.data.DrivingData;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bijesh on 12/10/2014.
 */
public class DrivingDataUtil {

    private static final String TAG = DrivingDataUtil.class.getCanonicalName();


    private void getDrivingDataForWeek(ArrayList<DrivingData> drivingData){


    }

    private double getMaximumSpeedOfWeek(ArrayList<DrivingData> drivingData){
      double maxSpeed = 0.0;
      for(DrivingData data:drivingData){
        if(data.getMaxSpeed() > maxSpeed){

        }
      }
      return maxSpeed;
    }


    /**
     * Use this method to get the start date of the week as millis for given any date
     * @param timeInMillis
     * @return
     */
    private static long getStartDateOfWeek(long timeInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(timeInMillis);
        Log.d(TAG, " time : before  "+calendar.get(Calendar.DAY_OF_WEEK)+" : "+ timeInMillis);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Log.d(TAG, " time : after "+calendar.get(Calendar.DAY_OF_WEEK)+ " : "+calendar.getTimeInMillis());
        return calendar.getTimeInMillis();
    }

    public static long getWeekLastDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        Log.d(TAG, " time : before  "+calendar.get(Calendar.DAY_OF_WEEK)+" : "+time);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        Log.d(TAG, " time : after "+calendar.get(Calendar.DAY_OF_WEEK)+ " : "+calendar.getTimeInMillis());
        return calendar.getTimeInMillis();
    }

    public long getStartTimeOfMonth(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis();
    }

    public long getEndTimeOfMonth(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis();
    }

    public long getStartWeekDayOfMonth(long timeInMillis) {
        return getStartDateOfWeek(getStartTimeOfMonth(timeInMillis));
    }

    public long getLastWeekDayOfMonth(long timeInMillis) {
        return getWeekLastDay(getEndTimeOfMonth(timeInMillis));
    }
}
