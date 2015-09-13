package com.verizontelematics.indrivemobile.services;

import android.content.Context;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.utils.ui.CalendarUtil;

/**
 * Created by bijesh on 10/27/2014.
 */
public class MaintenanceRemainderNotificationService {

   private static final String TAG = MaintenanceRemainderNotificationService.class.getCanonicalName();

    public void alertMaintenanceRemainder(Context context,long whtTime,int repeat,String name){
//        long atCurrentTimePlus10secs = System.currentTimeMillis()
//                + (10 * 1000), pen;
//        long remainderTime = whtTime - howManyHours(6);
////        System.out.println("$$$ remainder time "+remainderTime);
//        Intent intent = new Intent(context, MaintenanceRemainderReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context, 234324243, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, remainderTime, pendingIntent);
//        Log.d(TAG, "Remainder set at " + DateDataFormat.convertMillisToZuluFormat(remainderTime));

        CalendarUtil.pushRemainderToCalender1(context,whtTime,repeat,name);
        Toast.makeText(context,"Reminder added to Calendar",Toast.LENGTH_LONG).show();
    }

    private long howManyHours(int hrs){
        return hrs * 3600000;
    }



}
