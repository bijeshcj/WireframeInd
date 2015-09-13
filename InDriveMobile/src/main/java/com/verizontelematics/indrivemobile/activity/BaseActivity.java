package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.verizontelematics.indrivemobile.controllers.AppController;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.userprofile.UserFactory;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;
import com.verizontelematics.indrivemobile.utils.phone.SessionManager;

/**
 * Created by bijesh on 8/20/2014.
 */
public class BaseActivity extends Activity implements SessionManager.OnSessionExpireListener{

    private static final String TAG = BaseActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.d(TAG, "session onSessionExpire");
        if (AppController.instance().getStayLoggedSetting()) {
            AuthenticateController.instance().refresh(this, AppController.instance().getMobileUserId(), AppController.instance().getPasword());
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
