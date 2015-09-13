package com.verizontelematics.indrivemobile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class CloudMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "CloudMessageReceiver";

    public CloudMessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                CloudMessageServer.class.getName());
        // Start the service, keeping the device awake while it is launching.
        Bundle extras = intent.getExtras();
        if (extras == null)
            return;
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        Log.d(TAG, "GCM message received "+intent.getAction()+" messageType "+messageType+" Message "+extras.toString());
        Log.d(TAG," gcm "+extras.getString("key1")+" gcm1  "+extras.getString("message"));
        // Parse the intent and start service which actually create pending intent.
//        startWakefulService(context, (intent.setComponent(comp)));
        context.startService((intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
