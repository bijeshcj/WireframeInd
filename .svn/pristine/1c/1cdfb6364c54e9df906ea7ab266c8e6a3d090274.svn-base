package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;


public class SplashActivity extends Activity {

    private static String TAG = SplashActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 2000; // 2 Seconds
    private Thread timer;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i(TAG,"App started...");
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_splash));

        timer = new Thread() {
            public void run() {
                try {
                    // Display for 2 seconds
                    sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException ex) {
                    Log.e(TAG, "Exception is " + Log.getStackTraceString(ex));

                } finally {
                    // Goes to Next Activity
                    if (!isBackPressed) {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }

                }
            }
        };
        timer.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(SplashActivity.this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(SplashActivity.this).reportActivityStop(this);
    }


    @Override
    public void onBackPressed() {
        isBackPressed = true;
        timer.interrupt();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}
