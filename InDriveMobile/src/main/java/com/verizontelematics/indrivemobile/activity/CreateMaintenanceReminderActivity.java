package com.verizontelematics.indrivemobile.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.constants.UIConstants;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.fragments.DatePickerFragment;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.MaintenanceReminderData;
import com.verizontelematics.indrivemobile.models.data.RemainderCalendarEvent;
import com.verizontelematics.indrivemobile.models.data.UserDetail;
import com.verizontelematics.indrivemobile.models.data.UserPreferenceData;
import com.verizontelematics.indrivemobile.services.MaintenanceRemainderNotificationService;
import com.verizontelematics.indrivemobile.userprofile.UserRoleConstants;
import com.verizontelematics.indrivemobile.userprofile.utils.UserUtils;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.AppConstants.USER_SELECTION;
import com.verizontelematics.indrivemobile.utils.BasicUtil;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;
import com.verizontelematics.indrivemobile.utils.ui.CheckReminderOnDeviceUtil;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;
import com.verizontelematics.indrivemobile.utils.ui.InputFilterMinMax;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CreateMaintenanceReminderActivity extends FragmentActivity implements UIInterface, View.OnClickListener {

    private static final String TAG = CreateMaintenanceReminderActivity.class.getSimpleName();
    private static Calendar dateSelectedCalender = Calendar.getInstance();
    //Commenting part of removing Notify Me layout
    /*private ImageView onStateEmailIV;
    private ImageView offStateEmailIV;
    private ImageView onStateTextIV;
    private ImageView offStateTextIV;*/
    private TextView mServiceTypeTextView;
    private Button addToCalendarBTN;
    private EditText mEdtTxtMileageInterval;
    private EditText mRepeatOptionEditText;
    private EditText customReminderNameET;
    private EditText descET;
    private static TextView mTextDate;
    private Button dateBTN;
    private Button mileageIntervalBTN;
    private Button dayBTN;
    private RelativeLayout dateRL;
    private RelativeLayout mileageRL;
    private RelativeLayout repeatRL;
    //Commenting part of removing Notify Me layout
    /*private RelativeLayout emailRL;
    private RelativeLayout textRL;*/
    private RelativeLayout dayMonthRL;
    private static TextView dayMonthDetailTV;
    private USER_SELECTION mUserSelection = USER_SELECTION.DATE;
    private boolean mEditMode;
    private MaintenanceReminderData mReminderData = null;
    private CustomProgressDialog mCustomProgressDialog = null;
    private ArrayList<Integer> lstDisableServiceTypes = new ArrayList<Integer>();
    private MaintenanceReminderData mPreviousReminderData;
    private String mServiceType;
    private ArrayList<String> lstOfPredefinedServices;
    private ImageButton service_type_arrow;
    private LinearLayout service_type_container;
    private Button deleteEntryBtn;
    private static final int GET_SERVICE_TYPE = 10;
    private ArrayList<String> mServiceTypes;
    private StorageTransaction storageTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maintenance_reminder);
        //hideKeyBoard();
        storageTransaction = new StorageTransaction(this);
        parseArguments();
        setupUI();
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_add_remainder));
    }

    @Override
    protected void onResume() {
        super.onResume();
        DiagnosticController.instance().register(this);
       // hideKeyBoard();

    }

    @Override
    protected void onPause() {
        super.onPause();
        DiagnosticController.instance().unregister(this);
    }

    private void parseArguments() {

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            if ((arguments.containsKey(UIConstants.PRE_EXISTS_SERVICE_TYPE)) && (arguments.getIntegerArrayList(UIConstants.PRE_EXISTS_SERVICE_TYPE) != null)) {
                lstDisableServiceTypes = arguments.getIntegerArrayList(UIConstants.PRE_EXISTS_SERVICE_TYPE);
            }

            if(arguments.containsKey(UIConstants.BUNDLE_KEY_USER_ROLE) && (arguments.getString(UIConstants.BUNDLE_KEY_USER_ROLE) != null)){
                String userRole = arguments.getString(UIConstants.BUNDLE_KEY_USER_ROLE);
                Log.d(TAG,"$$$ userRole "+userRole);
            }

            if ((arguments.containsKey("selected_data")) && (arguments.getParcelable("selected_data") != null)) {
                mReminderData = arguments.getParcelable("selected_data");
                if (mReminderData != null) {
                    mPreviousReminderData = new MaintenanceReminderData();
                    mPreviousReminderData.set(mReminderData);
                }
                mEditMode = arguments.getBoolean("edit_mode", false);
            }
        }
    }

    private void setupUI() {

        setupHeaderView();

        ImageView infoIV = (ImageView)findViewById(R.id.btn_info);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToBeDecidedDialog dialog = new ToBeDecidedDialog(CreateMaintenanceReminderActivity.this,getResources().getString(R.string.info_reminder));
                dialog.setTitle(getResources().getString(R.string.about_reminders));
                dialog.show();

            }
        });

        // to make sure all day event.
        dateSelectedCalender.set(Calendar.HOUR_OF_DAY, 0);
        dateSelectedCalender.set(Calendar.MINUTE, 0);
        dateSelectedCalender.set(Calendar.SECOND, 0);


        mServiceTypeTextView = (TextView) this.findViewById(R.id.serviceType);
        mServiceTypeTextView.setOnClickListener(this);

        mEdtTxtMileageInterval = (EditText) this.findViewById(R.id.mileageDetailTextView);
        mEdtTxtMileageInterval.setFilters(new InputFilter[]{new InputFilterMinMax(AppConstants.MIN_MILEAGE_SIZE, AppConstants.MAX_MILEAGE_SIZE, this, USER_SELECTION.MILEAGE)});


        addToCalendarBTN = (Button) this.findViewById(R.id.addCalenderBtn);
        addToCalendarBTN.setOnClickListener(this);

        deleteEntryBtn = (Button) this.findViewById(R.id.deleteAlertBtn);
        deleteEntryBtn.setVisibility(View.INVISIBLE);
        deleteEntryBtn.setOnClickListener(this);

        service_type_arrow = (ImageButton) this.findViewById(R.id.service_type_arrow_btn);
        service_type_arrow.setOnClickListener(this);

        service_type_container = (LinearLayout) this.findViewById(R.id.service_type_container);
        service_type_container.setOnClickListener(this);

        mRepeatOptionEditText = (EditText) this.findViewById(R.id.txtViewRepeatOptions);
        mRepeatOptionEditText.setFilters(new InputFilter[]{new InputFilterMinMax(AppConstants.MIN_FREQUENCY_SIZE, AppConstants.MAX_FREQUENCY_SIZE, this,USER_SELECTION.DATE)});

        //Commenting part of removing Notify Me layout
        /*RelativeLayout emailSwitchRL = (RelativeLayout) this.findViewById(R.id.emailSwitch);
        onStateEmailIV = (ImageView) this.findViewById(R.id.onState);
        offStateEmailIV = (ImageView) this.findViewById(R.id.offState);
        emailSwitchRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offStateEmailIV.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateEmailIV.setVisibility(View.VISIBLE);
                    offStateEmailIV.setVisibility(View.INVISIBLE);
                } else if (offStateEmailIV.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onStateEmailIV.setVisibility(View.INVISIBLE);
                    offStateEmailIV.setVisibility(View.VISIBLE);
                } else if (onStateEmailIV.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateEmailIV.setVisibility(View.INVISIBLE);
                    offStateEmailIV.setVisibility(View.VISIBLE);
                } else {
                    //turning the state as OFF
                    onStateEmailIV.setVisibility(View.VISIBLE);
                    offStateEmailIV.setVisibility(View.INVISIBLE);
                }

                // validation for email/text by comparing the states of both
                if ((onStateTextIV.getVisibility() == View.INVISIBLE) && (onStateEmailIV.getVisibility() == View.INVISIBLE)) {

                    showAlertDialog(getResources().getString(R.string.reminder), getResources().getString(R.string.alert_select_email_text));
                    //turning the state as ON
                    onStateEmailIV.setVisibility(View.VISIBLE);
                    offStateEmailIV.setVisibility(View.INVISIBLE);
                }


            }
        });


        RelativeLayout textSwitchRL = (RelativeLayout) this.findViewById(R.id.textSwitch);
        onStateTextIV = (ImageView) this.findViewById(R.id.onStateText);
        offStateTextIV = (ImageView) this.findViewById(R.id.offStateText);
        textSwitchRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offStateTextIV.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateTextIV.setVisibility(View.VISIBLE);
                    offStateTextIV.setVisibility(View.INVISIBLE);
                } else if (offStateTextIV.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onStateTextIV.setVisibility(View.INVISIBLE);
                    offStateTextIV.setVisibility(View.VISIBLE);
                } else if (onStateTextIV.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateTextIV.setVisibility(View.INVISIBLE);
                    offStateTextIV.setVisibility(View.VISIBLE);
                } else {
                    //turning the state as OFF
                    onStateTextIV.setVisibility(View.VISIBLE);
                    offStateTextIV.setVisibility(View.INVISIBLE);
                }

                // validation for email/text by comparing the states of both
                if ((onStateTextIV.getVisibility() == View.INVISIBLE) && (onStateEmailIV.getVisibility() == View.INVISIBLE)) {
                    showAlertDialog(getResources().getString(R.string.reminder), getResources().getString(R.string.alert_select_email_text));
                    //turning the state as ON
                    onStateTextIV.setVisibility(View.VISIBLE);
                    offStateTextIV.setVisibility(View.INVISIBLE);
                }
            }
        });

        setNotificationSwitchValue(true, SWITCH_SELECTION.EMAIL);
        setNotificationSwitchValue(true, SWITCH_SELECTION.TEXT);*/

        updateLists();

        customReminderNameET = (EditText) this.findViewById(R.id.alertNameET);
        if (mServiceTypeTextView.getText().equals("Custom"))
            customReminderNameET.setVisibility(View.VISIBLE);
        else
            customReminderNameET.setVisibility(View.GONE);

        // Alert Details
        descET = (EditText) this.findViewById(R.id.alertDescET);
        // Alert Date
        mTextDate = (TextView) this.findViewById(R.id.dateDetailTV);
        @SuppressLint("SimpleDateFormat") String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        mTextDate.setText(currentDate);
        mTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        dateBTN = (Button) this.findViewById(R.id.dateBTN);
        dateBTN.setOnClickListener(this);

        mileageIntervalBTN = (Button) this.findViewById(R.id.mileageIntervalBTN);
        mileageIntervalBTN.setOnClickListener(this);

        dayBTN = (Button) this.findViewById(R.id.dayMonthBTN);
        dayBTN.setOnClickListener(this);

        dateRL = (RelativeLayout) this.findViewById(R.id.dateRL);
        dateRL.setOnClickListener(this);

        mileageRL = (RelativeLayout) this.findViewById(R.id.mileageRL);
        dayMonthRL = (RelativeLayout) this.findViewById(R.id.dayMonthRL);
        dayMonthRL.setOnClickListener(this);

        repeatRL = (RelativeLayout) this.findViewById(R.id.repeatLayout);
        //Commenting part of removing Notify Me layout
        /*emailRL = (RelativeLayout) this.findViewById(R.id.emailLayout);
        textRL = (RelativeLayout) this.findViewById(R.id.textLayout);*/

        dayMonthDetailTV = (TextView) this.findViewById(R.id.dayMonthDetailTV);

        updateFields();

    }

    private void updateLists() {

        String[] predefinedServiceTypes = this.getResources().getStringArray(R.array.maintenance_alert_type);
        String[] serviceTypes = this.getResources().getStringArray(R.array.maintenance_service_types);
        lstOfPredefinedServices = new ArrayList<String>(Arrays.asList(predefinedServiceTypes));
        mServiceTypes = new ArrayList<String>(Arrays.asList(serviceTypes));
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
        headerRL.setBackgroundResource(R.drawable.diagnostic_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        if (mEditMode) {
            headerTitleTV.setText(this.getResources().getString(R.string.edit_alert_entry));
        } else {
            headerTitleTV.setText(this.getResources().getString(R.string.create_alert_entry));
        }

        headerBtn.setText(this.getResources().getString(R.string.done));
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isUserInactive(CreateMaintenanceReminderActivity.this, UserRoleConstants.inactiveMessage)){
                    return;
                }
                BasicUtil.hideKeyboard(CreateMaintenanceReminderActivity.this);
                save();
            }
        });


    }

    private void save() {


        if (mServiceTypeTextView == null
                || customReminderNameET == null
                || descET == null
                || mTextDate == null
                || (mEditMode && mReminderData == null))
            return;


        // Service Type
        String serviceType = mServiceType;
        if (!mEditMode && (serviceType == null || serviceType.isEmpty())) {
            Toast.makeText(this, getResources().getString(R.string.alert_select_service_type), Toast.LENGTH_SHORT).show();
            return;
        }

        String serviceName = mServiceTypeTextView.getText().toString();

        // Reminder Name
        String reminderName = "";
        if (lstOfPredefinedServices.get(lstOfPredefinedServices.size() - 1).equalsIgnoreCase(mServiceTypeTextView.getText().toString())) {
            reminderName = customReminderNameET.getText().toString();
            // need to verify and fix appropriate
            serviceName = reminderName;
            if (reminderName.isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.alert_fill_custom_reminder), Toast.LENGTH_SHORT).show();
                if (customReminderNameET.requestFocus()) {
                    ((InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                }
                return;
            }
        }

        // Description
        String reminderDescription = descET.getText().toString();

        // Reminder Date
        long reminderDate = 0;

        // mileage interval
        double reminderConfigMiles = 0;

        // configuration months
        long reminderConfigMonths = 0;

        //Added validation when the mileage button selected
        if (mUserSelection == USER_SELECTION.MILEAGE) {

            String mileage = mEdtTxtMileageInterval.getText().toString().trim();
            if (!mileage.isEmpty()) {
                reminderConfigMiles = Long.parseLong(mileage);
            }

            if (reminderConfigMiles <= 0) {

                showAlertDialog(getResources().getString(R.string.reminder), getResources().getString(R.string.alert_enter_mileage_interval));
                return;

            }

        } else {
            //Added validation when the date button is selected.
            String repeatMonthStr = mRepeatOptionEditText.getText().toString().trim();
            if (!repeatMonthStr.isEmpty()) {
                reminderConfigMonths = Long.parseLong(repeatMonthStr);
            }

            if (reminderConfigMonths <= 0) {
                showAlertDialog(getResources().getString(R.string.reminder),getResources().getString(R.string.alert_frequency_validation));
                return;
            }
            reminderDate = dateSelectedCalender.getTimeInMillis();
        }

        //Commenting part of removing Notify Me layout
        /*// email notification flag
        boolean emailNotificationFlag = (onStateEmailIV.getVisibility() == View.VISIBLE);

        // sms notification flag
        boolean smsNotificationFlag = (onStateTextIV.getVisibility() == View.VISIBLE);

        //Added validation for email and text
        if (!emailNotificationFlag && !smsNotificationFlag) {
            Toast.makeText(this, getResources().getString(R.string.alert_select_email_text), Toast.LENGTH_SHORT).show();
            return;
        }*/


        //ToDo: Integrate it from db as per the user details. Right now it is hardcoded to false.
        boolean emailNotificationFlag = false;
        boolean smsNotificationFlag = false;
        //Hardcoded mobileUserID now. Need to replace it.
        UserDetail userDetail = storageTransaction.getUserDetails("");
        UserPreferenceData mUserPreferenceData = userDetail.getNotificationPreferenceInfo();
        if (mUserPreferenceData != null) {
            smsNotificationFlag = Boolean.valueOf(mUserPreferenceData.getSmsNotification());
            emailNotificationFlag = Boolean.valueOf(mUserPreferenceData.getEmailNotification());
        }

        if (!mEditMode) {
            MaintenanceReminderData maintenanceReminderData = new MaintenanceReminderData(reminderDescription, smsNotificationFlag, serviceType, 0, 0.0, reminderDate, "", "", reminderName, serviceName, "", 0, emailNotificationFlag, 0);

            // if serviceType is custom set serviceName as reminder name
            // temp fix need to be removed later.
            if (serviceType.equalsIgnoreCase("Custom")) {
                maintenanceReminderData.setServiceName(reminderName);
            }
            if (mUserSelection == USER_SELECTION.DATE) {
                // reset config miles
                maintenanceReminderData.setReminderConfigMiles(null);
                //set date and repeat months
                maintenanceReminderData.setReminderDate(reminderDate);
                maintenanceReminderData.setReminderConfigMonths(reminderConfigMonths);
            } else {
                maintenanceReminderData.setReminderDate(0);
                maintenanceReminderData.setReminderConfigMonths(null);
                maintenanceReminderData.setReminderConfigMiles(reminderConfigMiles);
            }

            onCreateAlertLog(maintenanceReminderData);

        } else {
            // set Description
            mReminderData.setReminderDescription(reminderDescription);
            // set date or millage intervals
            if (mUserSelection == USER_SELECTION.MILEAGE) {
                // reset date and repeat months.
                mReminderData.setReminderDate(0);
                mReminderData.setReminderConfigMonths(null);
                // reset to millage intervals to 0
                mReminderData.setReminderConfigMiles(reminderConfigMiles);

            } else {
                // reset to millage intervals to 0
                mReminderData.setReminderConfigMiles(null);
                // set date and repeat months.
                mReminderData.setReminderDate(reminderDate);
                mReminderData.setReminderConfigMonths(reminderConfigMonths);

            }
            // set email notification flag
            mReminderData.setNotificationFlagEmail(emailNotificationFlag);
            // set sms notification flag
            mReminderData.setNotificationFlagSms(smsNotificationFlag);

            onEditAlertLog(mReminderData);
        }
    }

    private void updateFields() {

        if (mEditMode) {

            if (mReminderData == null) {
                finish();
                return;
            }

            populateUIElements(mReminderData);
            mServiceTypeTextView.setEnabled(false);
            service_type_arrow.setEnabled(false);
            service_type_container.setEnabled(false);
            customReminderNameET.setHint(customReminderNameET.getText());
            customReminderNameET.setEnabled(false);
            deleteEntryBtn.setVisibility(View.VISIBLE);
            if ((mServiceTypeTextView.getText()).equals(getResources().getStringArray(R.array.maintenance_alert_type)[2])) {
                // enabling the service type edit text for Custom and hiding for default ones in the edit mode
                customReminderNameET.setVisibility(EditText.VISIBLE);
            } else {
                customReminderNameET.setVisibility(EditText.GONE);

            }

        }

    }

    private void onCreateAlertLog(MaintenanceReminderData reminderData) {
        Log.d(TAG,"defect onCreateAlertLog");
        ArrayList<MaintenanceReminderData> maintenanceReminderData = new ArrayList<MaintenanceReminderData>();
        maintenanceReminderData.add(reminderData);
        DiagnosticController.instance().addMaintenanceReminders(this, maintenanceReminderData);
    }

    private void onEditAlertLog(MaintenanceReminderData reminderData) {

        if (!isDataChanged()) {
            finish();
            return;
        }

        ArrayList<MaintenanceReminderData> maintenanceReminderData = new ArrayList<MaintenanceReminderData>();
        maintenanceReminderData.add(reminderData);
        DiagnosticController.instance().updateMaintenanceReminders(this, maintenanceReminderData);
    }

    private boolean isDataChanged() {
        if (mReminderData == null)
            return false;
        // compare mReminderData and mPreviousReminderData and return appropriate value

        // required editable fields compare.
        return (!(mReminderData.getReminderDate() == mPreviousReminderData.getReminderDate()
                && (mReminderData.isNotificationFlagSms() == mPreviousReminderData.isNotificationFlagSms())
                && (mReminderData.isNotificationFlagEmail() == mPreviousReminderData.isNotificationFlagEmail())
                && (mReminderData.getReminderConfigMonths() == mPreviousReminderData.getReminderConfigMonths())
                && (mReminderData.getReminderConfigMiles() == mPreviousReminderData.getReminderConfigMiles())
                && (((mReminderData.getReminderDescription() == null || mReminderData.getReminderDescription().isEmpty()) && (mPreviousReminderData.getReminderDescription() == null || mPreviousReminderData.getReminderDescription().isEmpty()))
                || ((mReminderData.getReminderDescription() != null) && mReminderData.getReminderDescription().equals(mPreviousReminderData.getReminderDescription())))
        ));


    }


    private void populateUIElements(MaintenanceReminderData reminderData) {

        if (reminderData == null)
            return;

        if (reminderData.getServiceType().equalsIgnoreCase("Custom")) {
            customReminderNameET.setVisibility(View.VISIBLE);
            customReminderNameET.setText(reminderData.getReminderName());
            mServiceTypeTextView.setText(getResources().getString(R.string.custom));

        } else {
            customReminderNameET.setVisibility(View.GONE);
            mServiceTypeTextView.setText(reminderData.getServiceName());
        }
        // Description
        descET.setText(reminderData.getReminderDescription() == null ? "" : reminderData.getReminderDescription());

        // Select Date or Mileage option
        mUserSelection = (reminderData.getReminderDate() <= 0) ? USER_SELECTION.MILEAGE : USER_SELECTION.DATE;
        if (mUserSelection.equals(USER_SELECTION.MILEAGE)) {
            onMileageSelected();
            mEdtTxtMileageInterval.setVisibility(View.VISIBLE);
            long mileageAsLong = (long)reminderData.getReminderConfigMiles();
            mEdtTxtMileageInterval.setText(""+mileageAsLong);//String.valueOf(reminderData.getReminderConfigMiles())
            addToCalendarBTN.setVisibility(View.GONE);

        } else {
            // Alert Date
            @SuppressLint("SimpleDateFormat") SimpleDateFormat mFormatter = new SimpleDateFormat("MM/dd/yyyy");
            mTextDate.setText(DateDataFormat.convertMillisAsDateString(this, reminderData.getReminderDate(), mFormatter, false));
            mRepeatOptionEditText.setText("" + reminderData.getReminderConfigMonths());
            addToCalendarBTN.setVisibility(View.VISIBLE);
            dateSelectedCalender.setTimeInMillis(reminderData.getReminderDate());

        }

        //Commenting part of removing Notify Me layout
        /*// Email Notification flag
        setNotificationSwitchValue(reminderData.isNotificationFlagEmail(), SWITCH_SELECTION.EMAIL);

        // sms Notification flag
        setNotificationSwitchValue(reminderData.isNotificationFlagSms(), SWITCH_SELECTION.TEXT);*/

//      Add  Calendar visibility
        if (isCalendarEventExists(isCustomType(reminderData, reminderData.getServiceName()), reminderData.getReminderDate())) {
            //addToCalendarBTN.setEnabled(false);
        }


    }

    private boolean isCalendarEventExists(String serviceName, long reminderTime) {
        boolean retFlag = false;
        ArrayList<RemainderCalendarEvent> previousReminders = CheckReminderOnDeviceUtil.checkCalendarEventByDate(this, reminderTime);
        if (previousReminders != null) {
            for (RemainderCalendarEvent event : previousReminders) {
                if (serviceName.equals(event.getTitle())) {
                    return true;
                }
            }
        }

        return retFlag;
    }

    private String isCustomType(MaintenanceReminderData maintenanceReminderData, String serviceType) {
        if (serviceType.equals("Custom")) {
            return maintenanceReminderData.getReminderName();
        } else
            return serviceType;
    }


    private void onMileageSelected() {

        dateRL.setVisibility(View.INVISIBLE);
        mileageRL.setVisibility(View.VISIBLE);
        dayMonthRL.setVisibility(View.INVISIBLE);
        repeatRL.setVisibility(View.GONE);
        //Commenting part of removing Notify Me layout
       /* emailRL.setVisibility(View.VISIBLE);
        textRL.setVisibility(View.VISIBLE);*/
        addToCalendarBTN.setVisibility(View.GONE);
        setSelectedEffect(R.drawable.bg_button_selected, R.drawable.button_press, R.drawable.button_press);
    }

    private void setSelectedEffect(int dateBtnColor, int mileageBtnColor, int dayMonthColor) {

        dateBTN.setBackgroundResource(dateBtnColor);
        mileageIntervalBTN.setBackgroundResource(mileageBtnColor);
        dayBTN.setBackgroundResource(dayMonthColor);
    }

    public static void setDateUponCancel(Calendar calendar) {
        if (calendar == null)
            return;
        int year = 0, monthOfYear = 0, dayOfMonth = 0;
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Log.v(TAG, "$$$ YEAR : Month of Year : dayOfMonth " + year + "  :  " + monthOfYear + "  :  " + dayOfMonth);
        mTextDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
        dayMonthDetailTV.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
        dateSelectedCalender = Calendar.getInstance();
        dateSelectedCalender.set(Calendar.YEAR, year);
        dateSelectedCalender.set(Calendar.MONTH, monthOfYear);
        dateSelectedCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }


    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putString("Screen","REMINDER");
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        //Fix for QC Defect 33806
        args.putLong("maxDate", calender.getTimeInMillis());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(dateListener);
        date.show(getSupportFragmentManager(), "Date Picker");

    }

    //Commenting part of removing Notify Me layout
   /* private void setNotificationSwitchValue(boolean notificationFlag, SWITCH_SELECTION selection) {

        if (selection == SWITCH_SELECTION.EMAIL) {
            if (notificationFlag) {
                onStateEmailIV.setVisibility(View.VISIBLE);
                offStateEmailIV.setVisibility(View.GONE);
            } else {
                onStateEmailIV.setVisibility(View.GONE);
                offStateEmailIV.setVisibility(View.VISIBLE);
            }
        } else {
            if (notificationFlag) {
                onStateTextIV.setVisibility(View.VISIBLE);
                offStateTextIV.setVisibility(View.GONE);
            } else {
                onStateTextIV.setVisibility(View.GONE);
                offStateTextIV.setVisibility(View.VISIBLE);
            }
        }

    }*/


    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                })
                .show();
    }

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            Log.v("YEAR : Month: day", year + "  :  " + monthOfYear + "  :  " + dayOfMonth);
            mTextDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
            dayMonthDetailTV.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
            dateSelectedCalender = Calendar.getInstance();
            dateSelectedCalender.set(Calendar.YEAR, year);
            dateSelectedCalender.set(Calendar.MONTH, monthOfYear);
            dateSelectedCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_maintenance_reminder, menu);
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
    public void onProgress(Operation opr) {

        if (opr == null)
            return;

        if (opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            return;
        }
        String lAddMaintenanceLogMessage = getResources().getString(R.string.adding_maintenance_remainder);//"Adding Maintenance Reminder";
        String lEditMaintenanceLogMessage = getResources().getString(R.string.editing_maintenance_reminder);//"Editing Maintenance Reminder";
        String lDeleting = getResources().getString(R.string.deleting_maintenance_reminder);//"Deleting Maintenance Reminder";
        if (opr.getId() == Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            showProgressDialogFragment("");
        } else if (opr.getId() == Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()) {
            showProgressDialogFragment("");
        } else if (opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()) {
            showProgressDialogFragment("");
        }

    }

    @Override
    public void onError(Operation opr) {
        if (opr == null)
            return;

        if (opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            return;
        }

        dismissProgressDialog();
        Toast lToast = Toast.makeText(this, getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT);
        lToast.show();

    }

    @Override
    public void onSuccess(Operation opr) {

        if (opr == null)
            return;
        if (opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            return;
        }

        dismissProgressDialog();

        //Informing user about the reminder type
        String operationMsg = "";
        if (opr.getId() == Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()) {
            operationMsg = getResources().getString(R.string.successfully_updated) + " "; // "You have successfully updated"
        } else if (opr.getId() == Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            operationMsg = getResources().getString(R.string.successfully_added) + " "; //"You have successfully added"
        } else if (opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()) {
            Intent deleteIntent = new Intent();
            deleteIntent.setAction("deleted");
            if (getParent() == null)
                setResult(RESULT_OK, deleteIntent);
            else {
                getParent().setResult(RESULT_OK, deleteIntent);
            }
            finish();
            return;
        } else {
            return;
        }
        String message = (mUserSelection == USER_SELECTION.DATE) ? getResources().getString(R.string.reminder_by_date_new_1)
                : getResources().getString(R.string.reminder_by_mileage_new);
        new AlertDialog.Builder(this)
                .setTitle(R.string.reminder)
                .setMessage(operationMsg + message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent updatedReminderData = new Intent();
                        updatedReminderData.putExtra("reminder_data", mReminderData);
                        updatedReminderData.setAction("updated");
                        if (getParent() == null)
                            setResult(RESULT_OK, updatedReminderData);
                        else {
                            getParent().setResult(RESULT_OK, updatedReminderData);
                        }
                        finish();
                    }
                })
                .show();

    }

    private void showProgressDialogFragment(String aMessage) {


        if (mCustomProgressDialog == null) {
            mCustomProgressDialog = new CustomProgressDialog(this, aMessage);
            mCustomProgressDialog.show();
        }

    }

    public void dismissProgressDialog() {
        if (mCustomProgressDialog != null) {
            mCustomProgressDialog.dismiss();
            mCustomProgressDialog = null;
        }
    }

    private void showClickedView(int clickedBtnID) {

        if (clickedBtnID == R.id.dateBTN) {

            dateRL.setVisibility(View.VISIBLE);
            mileageRL.setVisibility(View.INVISIBLE);
            dayMonthRL.setVisibility(View.INVISIBLE);
            repeatRL.setVisibility(View.VISIBLE);
            addToCalendarBTN.setVisibility(View.VISIBLE);

            setSelectedEffect(R.drawable.button_press, R.drawable.bg_button_selected, R.drawable.button_press);

        } else if (clickedBtnID == R.id.mileageIntervalBTN) {
            onMileageSelected();
        } else {
            dateRL.setVisibility(View.INVISIBLE);
            mileageRL.setVisibility(View.INVISIBLE);
            dayMonthRL.setVisibility(View.VISIBLE);
            setSelectedEffect(R.drawable.button_press, R.drawable.button_press, R.drawable.bg_button_selected);
        }


    }

    public void hideKeyBoard()
    {
        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dateBTN:
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                        "DiagnosticsReminderDate");
                BasicUtil.hideKeyboard(this);
                showClickedView(R.id.dateBTN);
                mUserSelection = USER_SELECTION.DATE;
                break;
            case R.id.mileageIntervalBTN:
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                        "DiagnosticsReminderMileage");
                BasicUtil.hideKeyboard(this);
                showClickedView(R.id.mileageIntervalBTN);
                mUserSelection = USER_SELECTION.MILEAGE;
                break;
            case R.id.dayMonthBTN:
                showClickedView(R.id.dayMonthBTN);
                break;
            case R.id.dateRL:
                showDatePicker();
                break;
            case R.id.dayMonthRL:
                showDatePicker();
                break;
            case R.id.serviceType:
            case R.id.service_type_arrow_btn:
            case R.id.service_type_container:
                BasicUtil.hideKeyboard(this);
                Intent serviceTypeIntent = new Intent(CreateMaintenanceReminderActivity.this, ServiceTypeActivity.class);
                serviceTypeIntent.putStringArrayListExtra("Service_Types", lstOfPredefinedServices);
                serviceTypeIntent.putIntegerArrayListExtra("Disabled_Service_Types", lstDisableServiceTypes);
                startActivityForResult(serviceTypeIntent, GET_SERVICE_TYPE);
                break;
            case R.id.deleteAlertBtn:
                showConfirmation();
                break;
            case R.id.addCalenderBtn:
                if (mRepeatOptionEditText.getText().length() == 0)
                    Toast.makeText(this, getResources().getString(R.string.alert_enter_frequency), Toast.LENGTH_SHORT).show();

                else if (mServiceTypeTextView.getText().length() == 0)
                    Toast.makeText(this, getResources().getString(R.string.alert_enter_service_type), Toast.LENGTH_SHORT).show();

                else
                    addCalendarRemainder();

                break;

            default:
                break;
        }

    }

    private void addCalendarRemainder() {

        long remainderTime = 0;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = formatter.parse(mTextDate.getText().toString());

            remainderTime = date.getTime();
        } catch (Exception e) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        }


        //Long remainderTime = mTextDate.getText().toString();
        Log.d(TAG, "$$$ remainder time " + remainderTime);
        if (!isCalendarEventExists(getRemainderName(), remainderTime)) {

            new MaintenanceRemainderNotificationService().alertMaintenanceRemainder(this.getApplicationContext(),
                    remainderTime, Integer.parseInt(mRepeatOptionEditText.getText().toString().trim()), getRemainderName());
        } else
            Toast.makeText(this, getResources().getString(R.string.reminder_already_exists), Toast.LENGTH_SHORT).show();


    }

    private String getRemainderName() {

        if ((mServiceTypeTextView.getText().toString().equalsIgnoreCase("Custom"))) {
            return customReminderNameET.getText().toString();
        } else
            return mServiceTypeTextView.getText().toString();


    }

    private void showConfirmation() {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.delete_maintenance_reminder_header))
                .setMessage(getResources().getString(R.string.alert_delete))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        deleteAlert();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void deleteAlert() {

        ArrayList<String> serviceIds = new ArrayList<String>();
        serviceIds.add(mReminderData.getReminderID());
        DiagnosticController.instance().deleteMaintenanceReminders(this, serviceIds);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_SERVICE_TYPE) {
            if (resultCode == RESULT_OK) {
                String selectedValue = data.getStringExtra("SelectedServiceType");
                int position = data.getIntExtra("Position", -1);
                mServiceTypeTextView.setText(selectedValue);
                if (mServiceTypes != null)
                    mServiceType = mServiceTypes.get(position);
                if (position == (lstOfPredefinedServices.size() - 1)) {
                    // 2 is for Custom.
                    customReminderNameET.setVisibility(View.VISIBLE);
                    customReminderNameET.setText("");
                    if (customReminderNameET.requestFocus()) {
                        ((InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                        BasicUtil.showKeyboard(this);
                    }
                } else {
                    customReminderNameET.setVisibility(EditText.GONE);
                    BasicUtil.hideKeyboard(this);
                }
            }
        }

    }
}
