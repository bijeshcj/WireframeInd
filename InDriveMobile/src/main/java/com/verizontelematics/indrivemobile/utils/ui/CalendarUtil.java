package com.verizontelematics.indrivemobile.utils.ui;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.models.data.DrivingData;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by bijesh on 11/7/2014.
 */
public class CalendarUtil {

    private static final String TAG = CalendarUtil.class.getCanonicalName();


    public static void pushRemainderToCalender1(Context context,long time,int repeat,String name){

        Uri EVENTS_URI = Uri.parse(getCalendarUriBase() + "events");
        ContentResolver cr = context.getContentResolver();
        long eventStartTime = time;//getStartOrEndTime(time,true);
        Log.d(TAG,"$$$ time "+eventStartTime);
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, name);
        values.put(CalendarContract.Events.DESCRIPTION, "Maintenance Reminder for your car");
        values.put(CalendarContract.Events.DTSTART, eventStartTime);
        values.put(CalendarContract.Events.RRULE, "FREQ=MONTHLY;INTERVAL="+repeat+";WKST=MO");
        values.put(CalendarContract.Events.DURATION,"P1D"); // this duration is as per RFC2445 format
        values.put(CalendarContract.Events.EVENT_TIMEZONE,TimeZone.getDefault().getID());//Time.TIMEZONE_UTC
        values.put(CalendarContract.Events.ALL_DAY,0);
//        values.put(, 1);
        Log.d(TAG, "$$$ default timezone " + TimeZone.getDefault().getID());
        //TimeZone.getDefault().getID());//"US");
//        Log.d(TAG, "$$ repeat month " + repeat+" $$$ FREQ=YEARLY;INTERVAL="+repeat+";BYMONTH=1");
        //); //"FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO" //"FREQ=MONTHLY;INTERVAL="+repeat

        Uri event = cr.insert(EVENTS_URI, values);

        Log.d(TAG,"after inserting the event "+event.getLastPathSegment());
        try {
// reminder insert
            Uri REMINDERS_URI = Uri.parse(getCalendarUriBase() + "reminders");
            values = new ContentValues();
            values.put("event_id", event.getLastPathSegment());//Long.parseLong(event.getLastPathSegment())
            values.put("method", 1);
            values.put("minutes", 10);
            cr.insert(REMINDERS_URI, values);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"Remainder not added",Toast.LENGTH_SHORT).show();
        }


        Log.d(TAG,"$$$ remainder added to calender...");
    }

    public static int numberOfDaysInAMonth(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    public static long getOffSetGMTTime(Calendar calendar){
//        System.out.println("$$$ calendar "+calendar.getTimeZone().getRawOffset());
        return calendar.getTimeZone().getRawOffset();

    }



    public static long getUtcOfCurrentTime(long time){
        Calendar c = Calendar.getInstance();
        System.out.println("current: "+c.getTime());
        c.setTimeInMillis(time);
        Log.d(TAG,"after time change current: "+c.getTime());

        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();
        if(z.inDaylightTime(new Date())){
            offset = offset + z.getDSTSavings();
        }
        int offsetHrs = offset / 1000 / 60 / 60;
        int offsetMins = offset / 1000 / 60 % 60;

        Log.d(TAG,"offset: " + offsetHrs);
        Log.d(TAG,"offset: " + offsetMins);

        c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
        c.add(Calendar.MINUTE, (-offsetMins));

        Log.d(TAG,"GMT Time: "+c.getTime()+" as Millis "+c.getTimeInMillis());
        return c.getTimeInMillis();
    }

    public static long getGMTTime(long timeAsMillis){
        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(timeAsMillis);
        temp.set(temp.get(Calendar.YEAR),temp.get(Calendar.MONTH),temp.get(Calendar.DATE),0,0,0);
        timeAsMillis = temp.getTimeInMillis() + CalendarUtil.getOffSetGMTTime(temp);
        return timeAsMillis;
    }

    public static long getLastDayOfWeek(long startTime){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.DATE, 6);
        long retLong = calendar.getTimeInMillis();

        return retLong;
    }

    public static int compareDates(long previousDate,long currentDate){
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        System.out.println(sdf.format(new Date(previousDate)));
//        System.out.println(sdf.format(new Date(currentDate)));
        String preString = sdf.format(new Date(previousDate));
        String curString = sdf.format(new Date(currentDate));

        String[] preArr = preString.split("-");

        int preYear = Integer.parseInt(preArr[0]);
        int preMonth = Integer.parseInt(preArr[1]);
        int preDate = Integer.parseInt(preArr[2]);

        String[] curArr = curString.split("-");

        int curYear = Integer.parseInt(curArr[0]);
        int curMonth = Integer.parseInt(curArr[1]);
        int curDate = Integer.parseInt(curArr[2]);

//        System.out.println(preYear+" %% "+ preMonth +" %% "+preDate);
//        System.out.println(curYear+" %% "+ curMonth +" %% "+curDate);
        DateTime pre = new DateTime(preYear, preMonth,preDate,0,0);
        DateTime cur = new DateTime(curYear, curMonth,curDate,0,0);
        Days days = Days.daysBetween(pre,cur);
//        System.out.println("$$$ day difference "+days.getDays());
        return days.getDays();
    }


    public static Double[] getDataWithMissingDates(Week week,String key,List<DrivingData> drivingDataList){
        ArrayList<Double> retList = new ArrayList<Double>();
        long previousDate = week.getDay(0),currentDate = 0;
        boolean isFirst = true;
        Double[] data = new Double[7];
        for (int i = 0; i < Week.MAXIMUM_DAYS; i++)
            data[i] = (double) 0;

        for (int i = 0; i < Week.MAXIMUM_DAYS; i++) {
            Log.d(TAG,"$$$ i is "+i+" drivingDataList.size() "+drivingDataList.size());
            if(i < drivingDataList.size()) {
                DrivingData drivingData = drivingDataList.get(i);
                if (isFirst) {
                    Log.d(TAG,"$$$ in first condition");
                    currentDate = drivingData.getDrivingDate();
                    Double val = getValue(key,drivingData);
                    Log.d(TAG,"$$$ previous Date "+previousDate+" current date "+currentDate);
                    int interval = compareDates(previousDate,currentDate);

                    if(interval > 0) {
                        int j=0;
                        for ( ; j < interval; j++) {
//                            retList.add(0.0);
                            data[j] = 0.0;
                        }
                        data[j] = val;
                    }else {
                        data[i] = val;
                    }
                    isFirst = false;

                    previousDate = currentDate;

//                    for(Double d:data)
//                        System.out.println("DDDD "+d);

                } else {
                    Log.d(TAG,"$$$ in else condition");
                    currentDate = drivingData.getDrivingDate();
                    Log.d(TAG,"$$$ previous Date "+previousDate+" current date "+currentDate);
                    double val = getValue(key,drivingData);
                    int interval = compareDates(previousDate,currentDate);
                    int j = 1;
                    for( ;j<interval;j++){
                        data[j] = 0.0;
                    }
//                    }
                    data[j] = val;
                    previousDate = currentDate;
//                    for(Double d:data)
//                        System.out.println("DD "+d);
                }
            }
        }
        return data;

    }

    private static Double getValue(String key,DrivingData dd){
        Double retVal = 0.0;
        String str = "0";
        // below hardcoded logic need to refactored.
        if (key.equals("Average Trip")) {
            str = dd.getTrips();
        }
        else if (key.equals("Carbon Footprint")) {
            str = dd.getCarbonFootprintLbs();
        }
        else if (key.equals("City MPG")) {
            str = dd.getCityMiles();
        }
        else if (key.equals("Highway MPG")) {
            str = dd.getHighwayMiles();
        }
        try {
            retVal = (double) Math.round(Double.parseDouble(str));
        }
        catch (NumberFormatException ne) {
            retVal = (double) 0;
        }
        if (key.equals("Max Speed")) {
            retVal = (double) Math.round(dd.getMaxSpeed());
        }
        if (key.equals("Total Miles")) {
            String strCityMiles = dd.getCityMiles();
            String strHighwayMiles = dd.getHighwayMiles();
            try {
                double cityMiles = Math.round(Double.parseDouble(strCityMiles));
                retVal = cityMiles + Math.round(Double.parseDouble(strHighwayMiles));
            }
            catch (NumberFormatException ne) {
                retVal = (double) 0;
            }
        }
        return retVal;
    }



//    @Deprecated
//    public static void pushRemainderToCalender(Context context,long time,int repeat,String name){
//
//        // get calendar
////        Calendar cal = Calendar.getInstance();
//        Uri EVENTS_URI = Uri.parse(getCalendarUriBase() + "events");
//        ContentResolver cr = context.getContentResolver();
//
//
//// event insert
//        ContentValues values = new ContentValues();
//        values.put("calendar_id", 1);
//        values.put("title", name);
////        values.put("allDay", 0);
//        long eventStartTime = getStartOrEndTime(time,true);//1415944260000L;//time;
////        Calendar endCal = Calendar.getInstance();
////        endCal.setTimeInMillis(time);
////        endCal.set(endCal.get(Calendar.YEAR),endCal.get(Calendar.MONTH),endCal.get(Calendar.DAY_OF_MONTH),23,59);
//
//        long eventEndTime = getStartOrEndTime(time,false);//endCal.getTimeInMillis();
////        values.put(CalendarContract.Events.RRULE,"FREQ=DAILY;INTERVAL=3");
//        Log.d(TAG, "$$$  event start time " + eventStartTime + " event end time " + eventEndTime);
//        values.put("dtstart", eventStartTime); // event starts at 11 minutes from now
////        values.put("dtend", eventEndTime); // ends 60 minutes from now
//        values.put("description", "Maintenance Reminder for your car");
////        values.put("visibility", 0);
//        values.put("hasAlarm", 1);
//        Log.d(TAG, "$$$ default timezone " + TimeZone.getDefault().getID());
//        values.put(CalendarContract.Events.EVENT_TIMEZONE, Time.TIMEZONE_UTC);//TimeZone.getDefault().getID());//"US");
////        Log.d(TAG, "$$ repeat month " + repeat+" $$$ FREQ=YEARLY;INTERVAL="+repeat+";BYMONTH=1");
//        values.put(CalendarContract.Events.RRULE, "");//); //"FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO" //"FREQ=MONTHLY;INTERVAL="+repeat
//        values.put("duration","P"+repeat+"M");
//        Uri event = cr.insert(EVENTS_URI, values);
//
//        Log.d(TAG,"after inserting the event");
//
//// reminder insert
//        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase() + "reminders");
//        values = new ContentValues();
//        values.put( "event_id", Long.parseLong(event.getLastPathSegment()));
//        values.put( "method", 1 );
//        values.put( "minutes", 10 );
//        cr.insert( REMINDERS_URI, values );
//
//
//
//        Log.d(TAG,"$$$ remainder added to calender...");
//    }

    public static long getFirstDayOfMonth(long weekStartTime){
        long retVal = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(weekStartTime);
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        retVal = cal.getTimeInMillis();
        return retVal;
    }

    public static long getLastDayOfMonth(long weekEndTime){
        long retVal = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(weekEndTime);
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        retVal = cal.getTimeInMillis();
        return retVal;
    }

    private static long getStartOrEndTime(long time,boolean isStartTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        if(isStartTime)
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 00, 01);
        else
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),23,59);
        return calendar.getTimeInMillis();
    }

    private static String getTimeZone(){
        return "UTC/GMT +5:30";
    }

    private static String getCalendarUriBase() {

        String calendarUriBase;
//        Uri calendars = Uri.parse("content://calendar/calendars");
//        Cursor managedCursor = null;
//        try {
//            managedCursor = act.managedQuery(calendars, null, null, null, null);
//        } catch (Exception e) {
//        }
//        if (managedCursor != null) {
//            calendarUriBase = "content://calendar/";
//        } else {
//            calendars = Uri.parse("content://com.android.calendar/calendars");
//            try {
//                managedCursor = act.managedQuery(calendars, null, null, null, null);
//            } catch (Exception e) {
//            }
//            if (managedCursor != null) {
//                calendarUriBase = "content://com.android.calendar/";
//            }
//        }
        calendarUriBase = "content://com.android.calendar/";
        return calendarUriBase;
    }


    

}



