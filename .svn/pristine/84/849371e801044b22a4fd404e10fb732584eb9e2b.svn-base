package com.verizontelematics.indrivemobile.widget.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.constants.WidgetConstants;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.models.VehicleHealthModel;
import com.verizontelematics.indrivemobile.models.data.VehiclePartData;
import com.verizontelematics.indrivemobile.utils.config.Config;
import com.verizontelematics.indrivemobile.widget.IndriveWidget;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by bijesh on 10/16/2014.
 */
public class IndriveWidgetUpdateService extends Service implements Observer {

    private static final String TAG = IndriveWidgetUpdateService.class.getCanonicalName();

    int[] allWidgetsIds;
    ComponentName indriveWidget;
    AppWidgetManager appWidgetManager;
    private Handler mHandler;
    private Handler mFetchDataHandler;
    private RemoteViews remoteViews;
    private int mViewId;
    private int mSrcId;
    private int mWidgetId;
    private static final long FIVE_MINUTES =  ((1000 * 60) * 5);
    private boolean isFirstUpdate = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Config.getInstance(this);
        // setup all callback listeners
        setupCallbacks();

    }

    private void setupCallbacks() {


        final IntentFilter myFilter = new IntentFilter(WidgetConstants.ACTION_TO_WIDGET_SERVICE);
        registerReceiver(communicationReceiver, myFilter);
        // register for vehicle health model.
        DiagnosticController.instance().getVehicleHealthModel().addObserver(this);
    }

    private void cleanup() {
        unregisterReceiver(communicationReceiver);
        // un register for vehicle health model.
        DiagnosticController.instance().getVehicleHealthModel().deleteObserver(this);
        mHandler.removeCallbacks(animRunnable);
        mFetchDataHandler.removeCallbacks(fetchRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG," started..." );
        processCommunication(intent);
        DiagnosticController.instance().getVehicleHealth(getApplicationContext());
        mHandler = new Handler();
        mFetchDataHandler = new Handler();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    BroadcastReceiver communicationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

           if(intent.getAction().equals(WidgetConstants.ACTION_TO_WIDGET_SERVICE)){


               sendDataToWidget(intent.getStringExtra("data"));
           }

        }
    };

    private void processCommunication(Intent intent){
        if (intent == null)
            return;

        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        allWidgetsIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        indriveWidget = new ComponentName(getApplicationContext(),IndriveWidget.class);
        // stub code
        VehicleHealthModel model = (VehicleHealthModel) DiagnosticController.instance().getVehicleHealthModel();
        if (model != null)
            sendDataToWidget(model.getOverallHealthStatus());
        // stub code ended.
//        int[] allWidgetsIdFromAppWidgetManager = appWidgetManager.getAppWidgetIds(indriveWidget);
//
//        for (int widgetId : allWidgetsIds) {
//            // create some random data
//            //int number = (new Random().nextInt(100));
//
//            RemoteViews remoteViews = new RemoteViews(this
//                    .getApplicationContext().getPackageName(),
//                    R.layout.widget_home);
//            Log.w(TAG, String.valueOf(number));
//            // Set the text
//            remoteViews.setTextViewText(R.id.diagSummaryTV,
//                    "Random: " + String.valueOf(number));
//
//            // Register an onClickListener
//            Intent clickIntent = new Intent(this.getApplicationContext(),
//                    IndriveWidget.class);
//
//            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
//                    allWidgetsIds);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.diagSummaryTV, pendingIntent);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//        }
    }

    private void sendDataToWidget(String status){
        if (status == null || allWidgetsIds == null)
            return;
        for (int widgetId : allWidgetsIds) {
            // create some random data
            //int number = (new Random().nextInt(100));

            remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.widget_home);
            Log.w(TAG, " Status "+status);
            // vehicle x-ray image with status.
            remoteViews.setTextViewText(R.id.diagnosticsSummaryTV, status);
            if (status.equalsIgnoreCase(VehiclePartData.STATUS_ERROR)) {

                remoteViews.setTextColor(R.id.diagnosticsSummaryTV, 0xFFFF0000);
//                remoteViews.setImageViewResource(R.id.mainRL,R.drawable.widget_trans);
                remoteViews.setImageViewResource(R.id.vehicleHealthIV, R.drawable.port_sedan_base_red);

                this.mViewId = R.id.vehicleHealthIV;
                this.mSrcId = R.drawable.port_sedan_base_red;
                this.mWidgetId = widgetId;
                fetchVehicleDataPeriodically();
//                animate(R.id.vehicleHealthIV, R.drawable.port_sedan_base_red);
            }
            else if (status.equalsIgnoreCase(VehiclePartData.STATUS_WARNING)) {
                remoteViews.setTextColor(R.id.diagnosticsSummaryTV, 0xFFFFDD00);
//                remoteViews.setImageViewResource(R.id.mainRL,R.drawable.widget_trans);
                //remoteViews.setImageViewResource(R.id.vehicleHealthIV, R.drawable.port_sedan_base_yellow);
                remoteViews.setImageViewResource(R.id.vehicleHealthIV, R.drawable.port_sedan_base_yellow);

                this.mViewId = R.id.vehicleHealthIV;
                this.mSrcId = R.drawable.port_sedan_base_yellow;
                this.mWidgetId = widgetId;
                fetchVehicleDataPeriodically();
//                animate(R.id.vehicleHealthIV, R.drawable.port_sedan_base_yellow);
            }

            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(),
                    IndriveWidget.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    allWidgetsIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.diagnosticsSummaryTV, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onDestroy() {
        // clean all callback listeners.
        cleanup();
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (VehicleHealthModel.class.isInstance(observable)) {
            VehicleHealthModel model = (VehicleHealthModel) DiagnosticController.instance().getVehicleHealthModel();
            if(isFirstUpdate) {
                Log.d(TAG,"$$$ update for the first time");
                sendDataToWidget(model.getOverallHealthStatus());
                isFirstUpdate = false;
            }else{
                Log.d(TAG," $$$ updates later");
                if (model != null)
                    updateAppWidget(model.getOverallHealthStatus());
            }
        }
    }

    private void fetchVehicleDataPeriodically(){
        Log.d(TAG," fetching data from the server ");
        DiagnosticController.instance().getVehicleHealth(getApplicationContext());
        if(mFetchDataHandler != null)
         mFetchDataHandler.postDelayed(fetchRunnable,FIVE_MINUTES);
    }

    Runnable fetchRunnable = new Runnable() {
        @Override
        public void run() {
//             Log.d(TAG,"animation started ");
            fetchVehicleDataPeriodically();
            mFetchDataHandler.postDelayed(fetchRunnable,FIVE_MINUTES);
        }
    };

    private void updateAppWidget(String status){
        Log.d(TAG,"$$$ updateAppWidget status "+status);
        if (remoteViews == null) {
            remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.widget_home);
        }
        remoteViews.setTextViewText(R.id.diagnosticsSummaryTV, status);
        if (status.equalsIgnoreCase(VehiclePartData.STATUS_ERROR)) {

            remoteViews.setTextColor(R.id.diagnosticsSummaryTV, 0xFFFF0000);
            remoteViews.setImageViewResource(R.id.vehicleHealthIV, R.drawable.port_sedan_base_red);
//            fetchVehicleDataPeriodically();
//                animate(R.id.vehicleHealthIV, R.drawable.port_sedan_base_red);
        }
        else if (status.equalsIgnoreCase(VehiclePartData.STATUS_WARNING)) {
            remoteViews.setTextColor(R.id.diagnosticsSummaryTV, 0xFFFFDD00);
            remoteViews.setImageViewResource(R.id.vehicleHealthIV, R.drawable.port_sedan_base_yellow);
//            fetchVehicleDataPeriodically();
//                animate(R.id.vehicleHealthIV, R.drawable.port_sedan_base_yellow);
        }
        appWidgetManager.updateAppWidget(mWidgetId, remoteViews);
    }



    volatile boolean animFlag;
    private void animate(int viewId,int srcId){
//        Log.d(TAG,"animFlag "+animFlag);
        if(animFlag) {
            remoteViews.setImageViewResource(viewId, R.drawable.port_sedan_base_red);
            animFlag = false;
        }
        else {
            remoteViews.setImageViewResource(viewId, R.drawable.port_sedan_base_yellow);
            animFlag = true;
        }
//        DiagnosticController.instance().getVehicleHealth(getApplicationContext());
        appWidgetManager.updateAppWidget(mWidgetId, remoteViews);
        mHandler.postDelayed(animRunnable,FIVE_MINUTES);
    }

    Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
//             Log.d(TAG,"animation started ");
            animate(mViewId,mSrcId);
            mHandler.postDelayed(animRunnable,FIVE_MINUTES);
        }
    };
}
