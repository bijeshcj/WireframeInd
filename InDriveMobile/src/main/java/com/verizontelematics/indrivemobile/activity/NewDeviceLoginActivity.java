package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.AppController;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.UserVehicleData;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

public class NewDeviceLoginActivity extends Activity implements UIInterface {

    private static final String TAG = "NewDeviceLoginActivity";
    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device_login);

        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_new_device_reg));

        Button continueBtn = (Button) findViewById(R.id.continueBTN);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_access_app),
                        getResources().getString(R.string.label_login));
                UserVehicleData userVehicleData = new UserVehicleData();
                userVehicleData.setMobileUserID(AppController.instance().getMobileUserId());
                AuthenticateController.instance().findUsers(NewDeviceLoginActivity.this,userVehicleData);
            }
        });
        AuthenticateController.instance().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(NewDeviceLoginActivity.this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(NewDeviceLoginActivity.this).reportActivityStop(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_device_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "$$$ onDestroy ");
        AuthenticateController.instance().unregister(this);
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.FIND_USER.ordinal()) {
            if (mProgressDialog == null) {
                mProgressDialog = new CustomProgressDialog(this, "");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.show();
                }
            });
        }
    }

    @Override
    public void onError(final Operation opr) {
        Log.d(TAG, "$$$ onError  ");
        if (opr.getId() == Operation.OperationCode.FIND_USER.ordinal()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog != null)
                        mProgressDialog.hide();

                    if(opr.getInformation().equalsIgnoreCase("1411")){
                        navigateToLogin();
                    }

                }
            });

        }
    }

    private void navigateToLogin(){
        Toast.makeText(NewDeviceLoginActivity.this, getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(NewDeviceLoginActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
            }
        }, 3000);


    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.FIND_USER.ordinal()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog != null)
                        mProgressDialog.hide();
                    finish();
                    Intent intent = new Intent(NewDeviceLoginActivity.this, NewDeviceSelectionActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
