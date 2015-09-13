package com.verizontelematics.indrivemobile.utils.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by z688522 on 12/10/14.
 */
public class Week {
    private final static String TAG = "Week";
    public static final long DAY = 1000 * 60 * 60 * 24;
    public static final int MAXIMUM_DAYS = 7;
    private long startTime;
    private long endTime;
    private String mDateLabelFormat;

    public Week(long startTime, long endTime, String aDateLabelFormat ) {
        Calendar temp= Calendar.getInstance();
        temp.setTimeInMillis(startTime);
        temp.set(temp.get(Calendar.YEAR),temp.get(Calendar.MONTH),temp.get(Calendar.DATE),0,0,0);
        temp.set(Calendar.MILLISECOND,0);
        this.startTime = temp.getTimeInMillis();
        temp.setTimeInMillis(endTime);
        temp.set(temp.get(Calendar.YEAR),temp.get(Calendar.MONTH),temp.get(Calendar.DATE),23,59,59);
        temp.set(Calendar.MILLISECOND,0);
        this.endTime = temp.getTimeInMillis();
        this.mDateLabelFormat = aDateLabelFormat;
    }

    public Week(long startTime, long endTime) {
        Calendar temp= Calendar.getInstance();
        temp.setTimeInMillis(startTime);
        temp.set(temp.get(Calendar.YEAR),temp.get(Calendar.MONTH),temp.get(Calendar.DATE),0,0,01);
        temp.set(Calendar.MILLISECOND,0);
        this.startTime = temp.getTimeInMillis();

        this.endTime = endTime;
    }

    public Week(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
//        Log.d(TAG, " time : before  " + calendar.get(Calendar.DAY_OF_WEEK) + " : " + time);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        startTime = calendar.getTimeInMillis();
//        Log.d(TAG, " time : start "+calendar.get(Calendar.DAY_OF_WEEK)+ " : "+startTime);
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        endTime = calendar.getTimeInMillis();
//        Log.d(TAG, " time : end "+calendar.get(Calendar.DAY_OF_WEEK)+ " : "+endTime);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String[] getDayLabels(Context ctx) {
        String[] labelArray = new String[7];
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mFormatter = new SimpleDateFormat(mDateLabelFormat);
        for (int i = 0; i < 7; i++) {
            labelArray[i] = DateDataFormat.convertMillisAsDateString(ctx, startTime + i * DAY, mFormatter, false);
//            Log.d(TAG, "Day "+ labelArray[i]);
        }

        return labelArray;
    }

    public ArrayList<Boolean> getDisabledDaysStart(){
//        Log.d(TAG,"$$$ start time of week "+this.startTime+" end time "+this.endTime);
        long weekEndTime = 0;
        Calendar temp= Calendar.getInstance();
        temp.setTimeInMillis(endTime);
        temp.set(temp.get(Calendar.YEAR),temp.get(Calendar.MONTH),temp.get(Calendar.DATE),0,0,0);
        temp.set(Calendar.MILLISECOND,0);
        boolean[] retFlags = {false,false,false,false,false,false,false};

        weekEndTime = temp.getTimeInMillis();
//        Log.d(TAG,"$$$ weekEndTime "+weekEndTime);
//         get first day of month
        long firstDayOfMonth = CalendarUtil.getFirstDayOfMonth(weekEndTime);
//        Log.d(TAG,"$$$ firstDayOfMonth "+firstDayOfMonth);

//         get diff from start of week and first day of month
        int diffInDays = CalendarUtil.compareDates(startTime,firstDayOfMonth);
//        System.out.println("$$$ diffInDays "+diffInDays);
//         set the number of days to false in the return boolean[]
        if(diffInDays > 0 && diffInDays < 7){
            for(int i=0;i<diffInDays;i++){
                retFlags[i] = true;
            }
        }


        ArrayList<Boolean> retList = new ArrayList<Boolean>();
        for(boolean b:retFlags) {
//            Log.d(TAG, "$$ retFlag " + b);
            retList.add(b);
        }

        return retList;

    }

    public ArrayList<Boolean> getDisabledDaysStartCustom(long firstDayData){
        ArrayList<Boolean> retList = new ArrayList<Boolean>();
        boolean[] retFlags = {false,false,false,false,false,false,false};
//      get week start time
//        System.out.println("$$$ week start custom "+startTime);
//      get first day of the week
//        System.out.println("$$$ firstDayData "+firstDayData);

//      diff days
        int diffInDays = CalendarUtil.compareDates(startTime,firstDayData);

//        System.out.println("$$$ diffInDays "+diffInDays);
//         set the number of days to false in the return boolean[]
        if(diffInDays > 0 && diffInDays < 7){
            for(int i=0;i<diffInDays;i++){
                retFlags[i] = true;
            }
        }

        for(boolean b:retFlags) {
//            Log.d(TAG, "$$ retFlag " + b);
            retList.add(b);
        }

        return retList;
    }

    public ArrayList<Boolean> getDisabledDaysEnd1(long lastDayOfMonth){

        long lastDayOfWeek = CalendarUtil.getLastDayOfWeek(startTime);
//        System.out.println("$$$ getDisabledDaysEnd1 lastDayOfWeek "+lastDayOfWeek+" lastDayOfMonth "+lastDayOfMonth);
        ArrayList<Boolean> retList = new ArrayList<Boolean>();
        boolean[] retFlags = {true,true,true,true,true,true,true};
        final int WEEKDAY = 7;
//         get diff from last day of month and end time of week
        int diffInDays = CalendarUtil.compareDates(lastDayOfMonth,lastDayOfWeek);
//        System.out.println("$$$ diffInDays "+diffInDays);
//         set the number of days to false in the return boolean[]
        if(diffInDays >0 && diffInDays < 7){
            int iteration = WEEKDAY - diffInDays;
            for(int i=0;i< iteration;i++){
                retFlags[i] = false;
            }
        }else if(diffInDays == 0){
            for(int i=0;i<retFlags.length;i++){
                retFlags[i] = false;
            }
        }
        for(boolean b:retFlags) {
//            Log.d(TAG, "$$ retFlag " + b);
            retList.add(b);
        }
        return retList;
    }

    public ArrayList<Boolean> getDisabledDaysEndCustom(long lastDayOfCustomTime){
        ArrayList<Boolean> retList = new ArrayList<Boolean>();
        long lastDayOfWeek = CalendarUtil.getLastDayOfWeek(startTime);
//        System.out.println("$$$ getDisabledDaysEndCustom lastDayOfWeek "+lastDayOfWeek+" lastDayOfCustomTime "+lastDayOfCustomTime);
        boolean[] retFlags = {true,true,true,true,true,true,true};
        final int WEEKDAY = 7;

//        get diff between last day of week and last date from custom time
        int diffInDays = CalendarUtil.compareDates(lastDayOfCustomTime,lastDayOfWeek);
//        System.out.println("$$$ diffInDays "+diffInDays);
        //         set the number of days to false in the return boolean[]
        if(diffInDays >0 && diffInDays < 7){
            int iteration = WEEKDAY - diffInDays;
            for(int i=0;i< iteration;i++){
                retFlags[i] = false;
            }
        }else if(diffInDays == 0){
            for(int i=0;i<retFlags.length;i++){
                retFlags[i] = false;
            }
        }
        for(boolean b:retFlags) {
//            Log.d(TAG, "$$ retFlag " + b);
            retList.add(b);
        }
        return retList;

    }



    public String getDateLabelFormat() {
        return mDateLabelFormat;
    }

    public void setDateLabelFormat(String mDateLabelFormat) {
        this.mDateLabelFormat = mDateLabelFormat;
    }

    public long getDay(int i) {
        return startTime + i * DAY;
    }
}
