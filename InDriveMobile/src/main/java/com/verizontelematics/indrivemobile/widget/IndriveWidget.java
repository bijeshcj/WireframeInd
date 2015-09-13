package com.verizontelematics.indrivemobile.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.constants.WidgetConstants;
import com.verizontelematics.indrivemobile.widget.service.IndriveWidgetUpdateService;

/**
 * Created by bijesh on 10/15/2014.
 */
public class IndriveWidget extends AppWidgetProvider implements WidgetConstants {


    private static final String TAG = "IndriveWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(TAG, "onUpdate()");
        Intent intent = new Intent(context, HomeActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(SHOW_POSITION, POSITION);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_home);
        remoteViews.setOnClickPendingIntent(R.id.relLayout_top, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);
        Toast.makeText(context, "Indrive widget added", Toast.LENGTH_SHORT).show();

        ComponentName thisWidget = new ComponentName(context,IndriveWidget.class);
        int[] allWidgetsIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent serviceIntent = new Intent(context.getApplicationContext(), IndriveWidgetUpdateService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetsIds);
        context.startService(serviceIntent);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
