package com.verizontelematics.indrivemobile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.controllers.AppController;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.userprofile.UserFactory;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;
import com.verizontelematics.indrivemobile.utils.phone.SessionManager;

/**
 * Created by bijesh on 8/21/2014.
 */
@SuppressLint("Registered")
public class BaseFragmentActivity extends ActionBarActivity implements SessionManager.OnSessionExpireListener{

    private static final String TAG = BaseFragmentActivity.class.getCanonicalName();
    protected void setActionBarTextColor() {

        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView titleTV = (TextView) findViewById(actionBarTitleId);
            if (titleTV != null) {
                titleTV.setTextColor(Color.WHITE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionManager.getInstance().setOnSessionExpireListener(this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        SessionManager.getInstance().interrupt();
    }

    @Override
    public void onSessionExpire() {
        Log.d(TAG,"session onSessionExpire");
        if (AppController.instance().getStayLoggedSetting()) {
            AuthenticateController.instance().refresh(this, AppController.instance().getMobileUserId(), AppController.instance().getPasword());
            return;
        }
        if(AppController.instance().isAppBackground()){
            Log.d(TAG,"App in Background");
            logOut();
            return;
        }
        logOut();
    }
    public void logOut() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        UserFactory.clearFactoryInstance();
        InDrivePreference.getInstance().setBooleanData("login", false);
    }
}
