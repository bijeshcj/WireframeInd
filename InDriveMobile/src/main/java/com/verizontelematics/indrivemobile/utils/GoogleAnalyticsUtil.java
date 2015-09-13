package com.verizontelematics.indrivemobile.utils;


import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.verizontelematics.indrivemobile.IndriveApplication;

/**
 * Created by bijesh on 5/4/2015.
 */
public class GoogleAnalyticsUtil {

    private static final String TAG = GoogleAnalyticsUtil.class.getCanonicalName();


    public enum Modules{
        HOME_SCREEN,
        FAQs
    }


    public void trackScreens(IndriveApplication application,String screenName){
        Tracker mTracker = application.getTracker(IndriveApplication.TrackerName.APP_TRACKER);
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.AppViewBuilder().build());
        Log.d(TAG,"Screen tracked" );
    }

     public void trackEvents(IndriveApplication application,String category,String label){
          Tracker t = application.getTracker(
                  IndriveApplication.TrackerName.APP_TRACKER);
// Build and send an Event.
          t.send(new HitBuilders.EventBuilder()
                  .setCategory(category)
                  .setAction("touch")
                  .setLabel(label)
                  .build());
         Log.d(TAG,"Action Event tracked");
      }


    public void trackListItemsEvents(IndriveApplication application,String category,Modules module,int position){
        Tracker t = application.getTracker(
                IndriveApplication.TrackerName.APP_TRACKER);
// Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction("touch")
                .setLabel(getLabel(module,position))
                .build());
        Log.d(TAG,"List item Event tracked");
    }


    private String getLabel(Modules module,int position){
        switch (module){
            case HOME_SCREEN:
                return getHomeScreenListnames(position);
            case FAQs:
                return getFaqsListnames(position);
        }
        return "";
    }


    private String getFaqsListnames(int pos){
        switch (pos){
            case 0:
                return "FAQsWhatis";
            case 1:
                return "FAQsLocationAlerts";
            case 2:
                return "FAQPackages";
            case 3:
                return "FAQsConnect";
            case 4:
                return "FAQsdiscount";
        }
        return "";
    }


    private String getHomeScreenListnames(int pos){
        switch (pos){
            case 0:
                return "HomeAlertSettings";
            case 1:
                return "HomeLocation";
            case 2:
                return "HomeDrivingData";
            case 3:
                return "HomeDiagnostics";
            case 4:
                return "HomeEmergency";
        }
        return "";
    }









}
