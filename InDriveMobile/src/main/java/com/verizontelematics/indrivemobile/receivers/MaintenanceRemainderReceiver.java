package com.verizontelematics.indrivemobile.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;

/**
 * Created by bijesh on 10/27/2014.
 */
public class MaintenanceRemainderReceiver extends BroadcastReceiver {

    private static final String TAG = MaintenanceRemainderReceiver.class.getCanonicalName();
    private static final int NOTIFY_APP_ID=1337;
    private static final int TENTATIVE = 0;


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Maintenance Remainder!!!!.",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
//        Vibrator vibrator = (Vibrator) context
//                .getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(2000);
        popupNotification(context);
    }


    private void popupNotification(Context context){
        final NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note = new Notification(R.drawable.app_logo,
                "Maintenance Remainder message!",
                System.currentTimeMillis());

        // This pending intent will open after notification click
        PendingIntent i=PendingIntent.getActivity(context, 0,
                new Intent(context, HomeActivity.class),
                0);

        note.setLatestEventInfo(context, "Maintenance Remainder message",
                "Maintenance Remainder for your car", i);

        //After uncomment this line you will see number of notification arrived
        //note.number=2;
        notificationManager.notify(NOTIFY_APP_ID, note);

//        pushRemainderToCalender(context);

    }


//    private void openCalenderIntent(Context context){
//        Calendar cal = Calendar.getInstance();
//        Intent intent = new Intent(Intent.ACTION_EDIT);
//        intent.setType("vnd.android.cursor.item/event");
//        intent.putExtra("beginTime", cal.getTimeInMillis());
//        intent.putExtra("allDay", false);
//        intent.putExtra("rrule", "FREQ=DAILY");
//        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
//        intent.putExtra("title", "A Test Event from android app");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }


}
