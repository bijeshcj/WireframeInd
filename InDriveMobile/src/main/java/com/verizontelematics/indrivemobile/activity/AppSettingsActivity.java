package com.verizontelematics.indrivemobile.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.AppController;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.models.GetUserPreferencesModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.UserDetail;
import com.verizontelematics.indrivemobile.models.data.UserPreferenceData;
import com.verizontelematics.indrivemobile.userprofile.UserRoleConstants;
import com.verizontelematics.indrivemobile.userprofile.utils.UserUtils;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

import java.util.Observable;
import java.util.Observer;

public class AppSettingsActivity extends FragmentActivity implements UserRoleConstants, CompoundButton.OnCheckedChangeListener, UIInterface, Observer {

    private static final String TAG = AppSettingsActivity.class.getCanonicalName();


    private RadioGroup timePeriodOneRG;
    private RadioGroup categoryOneRG;
    private RadioGroup categoryTwoRG;

    private CheckBox chkBxPushNotifications;
    private CheckBox chkBxText;
    private CheckBox chkBxEmail;


    private RadioButton mRbtThisWeek;
    private RadioButton mRbtLastWeek;
    private RadioButton mRbtThisMonth;
    private RadioButton mRbtlastMonth;
    private RadioButton mRbtCustom;
    private RadioButton mRbtTotalMiles;
    private RadioButton mRbtMaxSpeed;
    private RadioButton mRbtAvgTrip;
    private RadioButton mRbtCarbonFprint;
    private RadioButton mRbtCityMpg;
    private RadioButton mRbtHighwayMpg;
    private UserPreferenceData mUserPreferenceData;
    private CustomProgressDialog mProgressDialog;
    private StorageTransaction storageTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        setupUI();
        updateUI();
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_app_sett));
    }

    private void updateUI() {

        storageTransaction = new StorageTransaction(this);
        //Hardcoded mobileUserID now. Need to replace it.
        UserDetail userDetail = storageTransaction.getUserDetails("");
        mUserPreferenceData = userDetail.getNotificationPreferenceInfo();
        if (mUserPreferenceData != null) {
            chkBxPushNotifications.setChecked(Boolean.valueOf(mUserPreferenceData.getPushNotification()));
            chkBxText.setChecked(Boolean.valueOf(mUserPreferenceData.getSmsNotification()));
            chkBxEmail.setChecked(Boolean.valueOf(mUserPreferenceData.getEmailNotification()));
        }

//        update driving data period and update driving data category
        String[] drivingDataPrefs = AppController.instance().getAppSettingsDrivingData();
        if(drivingDataPrefs != null){
            Log.d(TAG,"$$$ drv time "+drivingDataPrefs[0]+" cat "+drivingDataPrefs[1]);
            initTimeRadioButton(drivingDataPrefs[0]);
            initCategoryRadioButton(drivingDataPrefs[1]);
        }

    }

    private void initTimeRadioButton(String timePeriod){
        if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.THIS_WEEK.toString())){
            timePeriodOneRG.check(R.id.thisWeekRB);
        }else if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.LAST_WEEK.toString())){
            timePeriodOneRG.check(R.id.lastWeekRB);
        }else if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.THIS_MONTH.toString())){
            timePeriodOneRG.check(R.id.thisMonthRB);
        }else if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.LAST_MONTH.toString())){
            timePeriodOneRG.check(R.id.lastMonthRB);
        }

    }

    private void initCategoryRadioButton(String category){
        if(category.equalsIgnoreCase(AppConstants.DrivingDataCategory.TOTAL_MILES.toString())){
            categoryOneRG.check(R.id.totalMilesRB);
            categoryTwoRG.clearCheck();
        }else if(category.equalsIgnoreCase(AppConstants.DrivingDataCategory.MAX_SPEED.toString())){
            categoryOneRG.check(R.id.maxSpeedRB);
            categoryTwoRG.clearCheck();
        }else if(category.equalsIgnoreCase(AppConstants.DrivingDataCategory.AVERAGE_TRIP.toString())){
            categoryOneRG.check(R.id.avgTripRB);
            categoryTwoRG.clearCheck();
        }


        else if(category.equalsIgnoreCase(AppConstants.DrivingDataCategory.CARBON_FOOTPRINT.toString())){
            categoryTwoRG.check(R.id.carbonFootPrintRB);
            categoryOneRG.clearCheck();
        } else if(category.equalsIgnoreCase(AppConstants.DrivingDataCategory.CITY_MPG.toString())){
            categoryTwoRG.check(R.id.cityMPGRB);
            categoryOneRG.clearCheck();
        } else if(category.equalsIgnoreCase(AppConstants.DrivingDataCategory.HIGHWAY_MPG.toString())){
            categoryTwoRG.check(R.id.highwayMPGRB);
            categoryOneRG.clearCheck();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setup();
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanup();
    }

    private void setup() {
        AuthenticateController.instance().register(this);
        AuthenticateController.instance().getUserPreferencesModel().addObserver(this);
        //Commenting the GetUserPreference call and populating the UI from kitchen sink service.
        AuthenticateController.instance().getUserPreferences(this, AuthenticateController.instance().getPrimaryUserDetail().getAccountInfo().getAccountID());
    }

    private void cleanup() {
        AuthenticateController.instance().unregister(this);
        AuthenticateController.instance().getUserPreferencesModel().deleteObserver(this);
    }


    private void setupUI() {
        setupHeaderView();
        timePeriodOneRG = (RadioGroup) this.findViewById(R.id.time_selection_layout);
        categoryOneRG = (RadioGroup) this.findViewById(R.id.category_selection_layout);
        categoryTwoRG = (RadioGroup) this.findViewById(R.id.category_selection_layout_two);

        chkBxPushNotifications = (CheckBox) findViewById(R.id.pushNotificationCB);
        chkBxText = (CheckBox) findViewById(R.id.textCB);
        chkBxEmail = (CheckBox) findViewById(R.id.emailCB);


        mRbtThisWeek = (RadioButton) findViewById(R.id.thisWeekRB);
        mRbtLastWeek = (RadioButton) findViewById(R.id.lastWeekRB);
        mRbtThisMonth = (RadioButton) findViewById(R.id.thisMonthRB);
        mRbtlastMonth = (RadioButton) findViewById(R.id.lastMonthRB);

        mRbtTotalMiles = (RadioButton) findViewById(R.id.thisWeekRB);
        mRbtMaxSpeed = (RadioButton) findViewById(R.id.thisWeekRB);
        mRbtAvgTrip = (RadioButton) findViewById(R.id.thisWeekRB);
        mRbtCarbonFprint = (RadioButton) findViewById(R.id.thisWeekRB);
        mRbtCityMpg = (RadioButton) findViewById(R.id.thisWeekRB);
        mRbtHighwayMpg = (RadioButton) findViewById(R.id.thisWeekRB);


        setUpListeners();

    }

    private void setUpListeners() {
        chkBxPushNotifications.setOnCheckedChangeListener(this);
        chkBxText.setOnCheckedChangeListener(this);
        chkBxEmail.setOnCheckedChangeListener(this);
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
        headerRL.setBackgroundColor(this.getResources().getColor(R.color.gray));
//        headerRL.setBackgroundResource(R.drawable.diagnostic_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.app_settings));
        headerBtn.setVisibility(View.VISIBLE);
        headerBtn.setText(getResources().getString(R.string.done));
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_app_navigation),
                        "AppSettingsDone");

                updateUserPreferences(String.valueOf(chkBxEmail.isChecked()), String.valueOf(chkBxPushNotifications.isChecked()), String.valueOf(chkBxText.isChecked()));
                updateLocalPreference();
            }
        });


    }

    private void updateLocalPreference(){
        AppConstants.DrivingDataTimePeriod timePeriod = AppConstants.DrivingDataTimePeriod.THIS_WEEK;
        AppConstants.DrivingDataCategory category = AppConstants.DrivingDataCategory.TOTAL_MILES;
        switch(timePeriodOneRG.getCheckedRadioButtonId()){
            case R.id.thisWeekRB:

                break;
            case R.id.lastWeekRB:
                timePeriod = AppConstants.DrivingDataTimePeriod.LAST_WEEK;
                break;
            case R.id.thisMonthRB:
                timePeriod = AppConstants.DrivingDataTimePeriod.THIS_MONTH;
                break;
            case R.id.lastMonthRB:
                timePeriod = AppConstants.DrivingDataTimePeriod.LAST_MONTH;
                break;
        }
//        System.out.println("$$4 timePerios "+timePeriod.toString());
//        set category
        switch (getCategorySelectedViewId()){
            case R.id.totalMilesRB:
                category = AppConstants.DrivingDataCategory.TOTAL_MILES;
                break;
            case R.id.maxSpeedRB:
                category = AppConstants.DrivingDataCategory.MAX_SPEED;
                break;
            case R.id.avgTripRB:
                category = AppConstants.DrivingDataCategory.AVERAGE_TRIP;
                break;
            case R.id.carbonFootPrintRB:
                category = AppConstants.DrivingDataCategory.CARBON_FOOTPRINT;
                break;
            case R.id.cityMPGRB:
                category = AppConstants.DrivingDataCategory.CITY_MPG;
                break;
            case R.id.highwayMPGRB:
                category = AppConstants.DrivingDataCategory.HIGHWAY_MPG;
                break;
        }
        AppController.instance().setAppSettingsDrivingData(timePeriod,category);
    }

    private int getCategorySelectedViewId(){
        System.out.println("$$$ categoryOneRG selected "+categoryOneRG.getCheckedRadioButtonId());
        int retValId = -1;
        retValId = categoryOneRG.getCheckedRadioButtonId();
        if(retValId == -1){
            retValId = categoryTwoRG.getCheckedRadioButtonId();
        }
        return retValId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_settings, menu);
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

    public void radioButtonClick(View view) {
        if (UserUtils.isUserInactive(this, inactiveMessage)) {
            return;
        }
//        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.thisWeekRB:
            case R.id.lastWeekRB:
            case R.id.thisMonthRB:
            case R.id.lastMonthRB:
                timePeriodOneRG.check(view.getId());
                break;


//                timePeriodOneRG.clearCheck();
//                break;

            case R.id.totalMilesRB:
            case R.id.maxSpeedRB:
            case R.id.avgTripRB:
                categoryTwoRG.clearCheck();
                categoryOneRG.check(view.getId());
                break;

            case R.id.carbonFootPrintRB:
            case R.id.cityMPGRB:
            case R.id.highwayMPGRB:
                categoryOneRG.clearCheck();
                categoryTwoRG.check(view.getId());
                break;

            default:
                break;


        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (UserUtils.isUserInactive(this, inactiveMessage)) {
            Log.d(TAG, "isChecked " + isChecked);
            if (buttonView.getId() == R.id.pushNotificationCB) {
                if (isChecked) {
                    chkBxText.setChecked(false);
                } else {
                    chkBxText.setChecked(true);
                }
            } else if (buttonView.getId() == R.id.textCB) {
                if (isChecked) {
                    chkBxPushNotifications.setChecked(false);
                } else {
                    chkBxPushNotifications.setChecked(true);
                }
            }

//        }
    }


    @Override
    public void onProgress(final Operation opr) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if (opr.getId() == Operation.OperationCode.GET_USER_PREFERENCE.ordinal()) {
                showProgressDialogFragment("");
            } else {
                showProgressDialogFragment("");
            }
        }
    });


    }

    @Override
    public void onError(final Operation opr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                String errorString;
                if (opr.getId() == Operation.OperationCode.GET_USER_PREFERENCE.ordinal()) {
                    errorString = getResources().getString(R.string.fetching_user_preferences_error);
                } else if (opr.getId() == Operation.OperationCode.UPDATE_USER_PREFERENCE.ordinal()) {
                    errorString = getResources().getString(R.string.updating_user_preferences_error);
                }
                else
                    errorString = "Error ";

                Toast lToast = Toast.makeText(AppSettingsActivity.this, errorString, Toast.LENGTH_SHORT);
                lToast.show();
            }
        });


    }

    @Override
    public void onSuccess(final Operation opr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                if (opr.getId() == Operation.OperationCode.UPDATE_USER_PREFERENCE.ordinal()) {
                    Toast lToast = Toast.makeText(AppSettingsActivity.this, getResources().getString(R.string.user_preferences_update_success), Toast.LENGTH_SHORT);
                    lToast.show();
                    finish();
                }
            }
        });

    }

    private void updateUserPreferences(String email, String pushNotification, String text) {

        if (mUserPreferenceData != null) {
            mUserPreferenceData.setEmailNotification(email);
            mUserPreferenceData.setPushNotification(pushNotification);
            mUserPreferenceData.setSmsNotification(text);
            AuthenticateController.instance().updateUserPreferences(this, mUserPreferenceData);
        }

    }

    private void showProgressDialogFragment(String aMessage) {


        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgressDialog(this, aMessage);
            mProgressDialog.show();
        }

    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void update(Observable observable, Object data) {

        if (GetUserPreferencesModel.class.isInstance(observable)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUserPreferenceData = (UserPreferenceData) AuthenticateController.instance().getUserPreferencesModel().getData();
                    populateUI();
                    updateUserPreferenceDB();
                }
            });
        }


    }

    private void updateUserPreferenceDB() {
        //Hardcoded accountID now. Need to replace it.
        storageTransaction.insertNotificationPreferenceInfo("", mUserPreferenceData);
    }

    private void populateUI() {

        chkBxPushNotifications.setChecked(Boolean.valueOf(mUserPreferenceData.getPushNotification()));
        chkBxText.setChecked(Boolean.valueOf(mUserPreferenceData.getSmsNotification()));
        chkBxEmail.setChecked(Boolean.valueOf(mUserPreferenceData.getEmailNotification()));

    }
}