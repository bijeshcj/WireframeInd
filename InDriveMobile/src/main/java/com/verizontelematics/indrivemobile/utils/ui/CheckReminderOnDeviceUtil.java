package com.verizontelematics.indrivemobile.utils.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.verizontelematics.indrivemobile.models.data.RemainderCalendarEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by z684985 on 11/12/14.
 */
public class CheckReminderOnDeviceUtil {

    private static final String TAG = CheckReminderOnDeviceUtil.class.getCanonicalName();


//    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
//    public static ArrayList<String> startDates = new ArrayList<String>();
//    public static ArrayList<String> endDates = new ArrayList<String>();
//    public static ArrayList<String> descriptions = new ArrayList<String>();
    //private static Date passed;
    public static ArrayList<RemainderCalendarEvent> checkCalendarEventByDate(Context context,Long datePassed) {
        ArrayList<RemainderCalendarEvent> events = new ArrayList<RemainderCalendarEvent>();
        //passed= new Date(datePassed);
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation","rrule","duration","eventTimezone","eventEndTimezone",
                        "allDay","rdate",}, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];
        Log.d(TAG,"event count "+CNames.length);
        // fetching calendars id
//        nameOfEvent.clear();
//        startDates.clear();
//        endDates.clear();
//        descriptions.clear();
        for (int i = 0; i < CNames.length; i++) {
            Log.d(TAG,"event date already exists "+cursor.getString(4)+" rrule "+cursor.getString(6));
            printAllColumn(cursor);
            if( cursor.getString(3) != null && getDate(datePassed).compareTo(getDate(Long.parseLong(cursor.getString(3))))==0)
            {
//                nameOfEvent.add(cursor.getString(1));
//                startDates.add(getDate(Long.parseLong(cursor.getString(3))));
//                endDates.add(getDate(Long.parseLong(cursor.getString(4))));
//                descriptions.add(cursor.getString(2));
                  String eventName = cursor.getString(1);
                  long startDate = Long.parseLong(cursor.getString(3));
                RemainderCalendarEvent remainderCalendarEvent = new RemainderCalendarEvent(eventName,startDate);
                CNames[i] = cursor.getString(1);
                events.add(remainderCalendarEvent);
                Log.d(TAG, "$$$ startDates " + startDate);
            }
            cursor.moveToNext();


        }

        return events;
    }

    private static void printAllColumn(Cursor cursor){

//        calendar_id", "title", "description",
//        "dtstart", "dtend", "eventLocation","rrule","duration","eventTimezone","eventEndTimezone",
//                "allDay","rdate",
//
        Log.d(TAG,"calendar_id "+cursor.getString(0)+" title "+cursor.getString(1)+" description "+cursor.getString(2)+
        " dtstart "+cursor.getString(3)+" dtend "+cursor.getString(4)+" eventLocation "+cursor.getString(5)+" rrule "+cursor.getString(6)+
        " duration "+cursor.getString(7)+" eventTimezone "+cursor.getString(8)+" eventEndTimezone "+cursor.getString(9)+" allDay "+cursor.getString(10)+" rdate "+
                cursor.getString(11));
    }

    public static String getDate(long milliSeconds) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(
                "MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
