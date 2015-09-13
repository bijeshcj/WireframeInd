package com.verizontelematics.indrivemobile.activity;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by Bijesh on 17-03-2015.
 */
public class InDriveBaseFragmentActivity extends FragmentActivity {

    private static final String TAG = InDriveBaseFragmentActivity.class.getCanonicalName();

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"App is on foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"App is on background");
    }
}
