package com.verizontelematics.indrivemobile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.constants.UIConstants;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.models.MaintenanceAlertModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.MaintenanceReminderData;
import com.verizontelematics.indrivemobile.models.data.RemainderCalendarEvent;
import com.verizontelematics.indrivemobile.services.MaintenanceRemainderNotificationService;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.BasicUtil;
import com.verizontelematics.indrivemobile.utils.ui.CheckReminderOnDeviceUtil;
import com.verizontelematics.indrivemobile.utils.ui.CustomOptionListFragment;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;
import com.verizontelematics.indrivemobile.utils.ui.InputFilterMinMax;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Priyanga on 8/28/2014.
 */
public class CreateMaintenanceReminderFragment extends BaseSubUIFragment implements View.OnClickListener, HomeActivity.CustomTopBarItemsClickListener, UIConstants, UIInterface {

    private static final String TAG = CreateMaintenanceReminderFragment.class.getCanonicalName();

    // Model
    MaintenanceAlertModel mMaintenanceAlertModel = new MaintenanceAlertModel();
    private List<String> lstOfPredefinedServices;
    private long mRemainderDate;
    private boolean txtChanged = false;


    private long mEdAlertDate;
    private static Calendar dateSelectedCalender = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {


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

    public static void setDateUponCancel(Calendar calendar){
        int year,monthOfYear,dayOfMonth;
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Log.v(TAG, "$$$ YEAR : Month of Year : dayOfMonth "+year + "  :  " + monthOfYear + "  :  " + dayOfMonth);
        mTextDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
        dayMonthDetailTV.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
        dateSelectedCalender = Calendar.getInstance();
        dateSelectedCalender.set(Calendar.YEAR, year);
        dateSelectedCalender.set(Calendar.MONTH, monthOfYear);
        dateSelectedCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    //private Spinner maintenanceTypeSpinner;
    // UI elements
    private EditText mETAlertName, mEdtTxtMileageInterval;
    private EditText mETDetail;
    private static TextView mTextDate;
    private boolean mEditMode = false;
    private Button dateBTN, addToCalendarBTN;
    private Button mileageIntervalBTN;
    private Button dayBTN;
    private RelativeLayout dateRL;

    //Service Types
    private RelativeLayout mileageRL, repeatRL, emailRL, textRL;
    private ImageView onStateEmail, offStateEmail, onStateText, offStateText;
    private RelativeLayout dayMonthRL;
    private static TextView dayMonthDetailTV;
    private StorageTransaction transaction;
    private TextView mServiceTypeTextView;
    private EditText mRepeatOptionEditText;
    private USER_SELECTION mUserSelection = USER_SELECTION.DATE;
    private ArrayList<Integer> lstDisableServiceTypes = new ArrayList<Integer>();
    private MaintenanceReminderData mReminderData = null;
    private List<String> mServiceTypes;
    private String mServiceType;
    private ProgressDialogFragment mProgressDialogFragment = null;
    private MaintenanceReminderData mPreviousReminderData;


    public CreateMaintenanceReminderFragment() {
//        Empty constructor

    }


    @Override
    public void onProgress(final Operation opr) {
        if (getActivity() == null || opr == null)
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
        if (getActivity() == null || opr == null)
            return;

        if (opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            return;
        }
        if (mProgressDialogFragment != null) {
            mProgressDialogFragment.dismiss();
            mProgressDialogFragment = null;
        }
        Toast lToast = Toast.makeText(getActivity(), getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT);
        lToast.show();
    }

    @Override
    public void onSuccess(final Operation opr) {
        if (getActivity() == null || opr == null)
            return;
        if (opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()
                && opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            return;
        }
        if (mProgressDialogFragment != null) {
            mProgressDialogFragment.dismiss();
            mProgressDialogFragment = null;
        }

        //Informing user about the reminder type
        String operationMsg;
        if (opr.getId() == Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS.ordinal()) {
            //operationMsg = " "+getResources().getString(R.string.updated);
            operationMsg = getResources().getString(R.string.successfully_updated) + " "; // "You have successfully updated"
        } else if (opr.getId() == Operation.OperationCode.ADD_MAINTENANCE_REMINDERS.ordinal()) {
            //operationMsg = " "+getResources().getString(R.string.created);
            operationMsg = getResources().getString(R.string.successfully_added) + " "; //"You have successfully added"
        } else if (opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()){
            // remove current fragment.
            removeFragment();
            // on delete
            // remove detailed view fragment.
            removeFragment();
            return;
        }
        else {
            return;
        }
        String message = (mUserSelection == USER_SELECTION.DATE) ? getResources().getString(R.string.reminder_by_date_new_1)
                : getResources().getString(R.string.reminder_by_mileage_new);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.maintenance_reminders)
                .setMessage(operationMsg + message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        removeFragment();
                    }
                })
                .show();

    }


   public enum USER_SELECTION {
        DATE("DATE"),
        MILEAGE("MILEAGE");

        USER_SELECTION(String selection) {
            this.selection = selection;
        }

        private String selection;

        public String getSelection() {
            return selection;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_createmaintenance_alert, container, false);


        transaction = new StorageTransaction(getActivity());
        if (getArguments() != null) {
            mReminderData = getArguments().getParcelable("selected_data");
            if (mReminderData != null) {
                mPreviousReminderData = new MaintenanceReminderData();
                mPreviousReminderData.set(mReminderData);
            }
            mEditMode = getArguments().getBoolean("edit_mode", false);
            lstDisableServiceTypes = (ArrayList<Integer>) getArguments().getSerializable(PRE_EXISTS_SERVICE_TYPE);

        }
        setupUIElements(rootView);
        setHasOptionsMenu(true);
        setListenersForSaveCancel();

        return rootView;
    }

    private void setListenersForSaveCancel() {
        ((HomeActivity) getActivity()).addToDoneButtonClickHandlers(this);
        ((HomeActivity) getActivity()).addToLeftArrowClickHandlers(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DiagnosticController.instance().register(this);
        ((HomeActivity) activity).showTabBar(false);
        ((HomeActivity) activity).setCustomActionBarView(activity.getResources().getString(R.string.create_alert_entry), true, false, true,false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProgressDialogFragment = null;
        DiagnosticController.instance().unregister(this);
        if (!mEditMode)
        ((HomeActivity) getActivity()).showTabBar(true);
        ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[1], true, false, true,true);
    }


    @SuppressLint("SimpleDateFormat")
    private void setupUIElements(View rootView) {

        // to make sure all day event.
        dateSelectedCalender.set(Calendar.HOUR_OF_DAY, 0);
        dateSelectedCalender.set(Calendar.MINUTE, 0);
        dateSelectedCalender.set(Calendar.SECOND, 0);

        //Default Array List
        String[] predefinedRepeatOptions = getActivity().getResources().getStringArray(R.array.repeat_options);
        List<String> lstOfPredefinedRepeatOptions = Arrays.asList(predefinedRepeatOptions);

        // Alert Type
        mServiceTypeTextView = (TextView) rootView.findViewById(R.id.serviceType);
        mEdtTxtMileageInterval = (EditText) rootView.findViewById(R.id.mileageDetailTextView);
        addToCalendarBTN = (Button) rootView.findViewById(R.id.addCalenderBtn);
        addToCalendarBTN.setOnClickListener(this);
        Button deleteEntryBtn = (Button) rootView.findViewById(R.id.deleteAlertBtn);
        deleteEntryBtn.setVisibility(View.INVISIBLE);
        deleteEntryBtn.setOnClickListener(this);
        mServiceTypeTextView.setOnClickListener(this);
        ImageButton service_type_arrow = (ImageButton) rootView.findViewById(R.id.service_type_arrow_btn);
        service_type_arrow.setOnClickListener(this);
        LinearLayout service_type_container = (LinearLayout) rootView.findViewById(R.id.service_type_container);
        service_type_container.setOnClickListener(this);
        mRepeatOptionEditText = (EditText) rootView.findViewById(R.id.txtViewRepeatOptions);
        //mRepeatOptionEditText.setText("12");
        mRepeatOptionEditText.setFilters(new InputFilter[]{new InputFilterMinMax(AppConstants.MIN_FREQUENCY_SIZE, AppConstants.MAX_FREQUENCY_SIZE, getActivity(), AppConstants.USER_SELECTION.DATE)});
        mEdtTxtMileageInterval.setFilters(new InputFilter[]{new InputFilterMinMax(AppConstants.MIN_MILEAGE_SIZE, AppConstants.MAX_MILEAGE_SIZE, getActivity(), AppConstants.USER_SELECTION.MILEAGE)});
        RelativeLayout switchLayout = (RelativeLayout) rootView.findViewById(R.id.emailSwitch);
        onStateEmail = (ImageView) rootView.findViewById(R.id.onState);
        offStateEmail = (ImageView) rootView.findViewById(R.id.offState);
        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offStateEmail.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateEmail.setVisibility(View.VISIBLE);
                    offStateEmail.setVisibility(View.INVISIBLE);
                } else if (offStateEmail.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onStateEmail.setVisibility(View.INVISIBLE);
                    offStateEmail.setVisibility(View.VISIBLE);
                } else if (onStateEmail.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateEmail.setVisibility(View.INVISIBLE);
                    offStateEmail.setVisibility(View.VISIBLE);
                } else if (onStateEmail.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onStateEmail.setVisibility(View.VISIBLE);
                    offStateEmail.setVisibility(View.INVISIBLE);
                }

                // validation for email/text by comparing the states of both
                if ((onStateText.getVisibility() == View.INVISIBLE) && (onStateEmail.getVisibility() == View.INVISIBLE)) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getResources().getString(R.string.maintenance_reminders))
                            .setMessage(getResources().getString(R.string.alert_select_email_text))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {


                                }
                            })
                            .show();
                    //turning the state as ON
                    onStateEmail.setVisibility(View.VISIBLE);
                    offStateEmail.setVisibility(View.INVISIBLE);
                }
            }
        });

        RelativeLayout switchLayoutText = (RelativeLayout) rootView.findViewById(R.id.textSwitch);
        onStateText = (ImageView) rootView.findViewById(R.id.onStateText);
        offStateText = (ImageView) rootView.findViewById(R.id.offStateText);
        switchLayoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offStateText.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateText.setVisibility(View.VISIBLE);
                    offStateText.setVisibility(View.INVISIBLE);
                } else if (offStateText.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onStateText.setVisibility(View.INVISIBLE);
                    offStateText.setVisibility(View.VISIBLE);
                } else if (onStateText.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateText.setVisibility(View.INVISIBLE);
                    offStateText.setVisibility(View.VISIBLE);
                } else if (onStateText.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onStateText.setVisibility(View.VISIBLE);
                    offStateText.setVisibility(View.INVISIBLE);
                }

                // validation for email/text by comparing the states of both
                if ((onStateText.getVisibility() == View.INVISIBLE) && (onStateEmail.getVisibility() == View.INVISIBLE)) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getResources().getString(R.string.maintenance_reminders))
                            .setMessage(getResources().getString(R.string.alert_select_email_text))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {


                                }
                            })
                            .show();
                    //turning the state as ON
                    onStateText.setVisibility(View.VISIBLE);
                    offStateText.setVisibility(View.INVISIBLE);
                }
            }
        });


        setEmailNotificationSwitchValue(true);
        setSmsNotificationSwitchValue(true);


        // AlertName
        mETAlertName = (EditText) rootView.findViewById(R.id.alertNameET);
        if (mServiceTypeTextView.getText().equals("Custom"))
            mETAlertName.setVisibility(View.VISIBLE);
        else
            mETAlertName.setVisibility(View.GONE);
        //UiStateHelper.disableView(mETAlertName);

        // Alert Details
        mETDetail = (EditText) rootView.findViewById(R.id.alertDescET);
        // Alert Date
        mTextDate = (TextView) rootView.findViewById(R.id.dateDetailTV);
        String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        mTextDate.setText(currentDate);
        mTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }


        });


        // Since all the service types are static we do not need this anymore.
        updateSpinnerForServiceType();




        dateBTN = (Button) rootView.findViewById(R.id.dateBTN);
        mileageIntervalBTN = (Button) rootView.findViewById(R.id.mileageIntervalBTN);
        mileageIntervalBTN.setOnClickListener(this);
        dayBTN = (Button) rootView.findViewById(R.id.dayMonthBTN);
        dateBTN.setOnClickListener(this);
        mileageIntervalBTN.setOnClickListener(this);
        dayBTN.setOnClickListener(this);

        dateRL = (RelativeLayout) rootView.findViewById(R.id.dateRL);
        mileageRL = (RelativeLayout) rootView.findViewById(R.id.mileageRL);
        dayMonthRL = (RelativeLayout) rootView.findViewById(R.id.dayMonthRL);
        repeatRL = (RelativeLayout) rootView.findViewById(R.id.repeatLayout);
        emailRL = (RelativeLayout) rootView.findViewById(R.id.emailLayout);
        textRL = (RelativeLayout) rootView.findViewById(R.id.textLayout);


        dateRL.setOnClickListener(this);
        dayMonthRL.setOnClickListener(this);

        dayMonthDetailTV = (TextView) rootView.findViewById(R.id.dayMonthDetailTV);


        // update fields with selected
        if (mEditMode) {
            ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.edit_alert_entry), true, false, true,true);


            updateFields();

            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
                switchLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (offStateEmail.getVisibility() == View.VISIBLE) {
                            //turning the state as ON
                            onStateEmail.setVisibility(View.VISIBLE);
                            offStateEmail.setVisibility(View.INVISIBLE);
                        } else if (offStateEmail.getVisibility() == View.INVISIBLE) {
                            //turning the state as OFF
                            onStateEmail.setVisibility(View.INVISIBLE);
                            offStateEmail.setVisibility(View.VISIBLE);
                        } else if (onStateEmail.getVisibility() == View.VISIBLE) {
                            //turning the state as ON
                            onStateEmail.setVisibility(View.INVISIBLE);
                            offStateEmail.setVisibility(View.VISIBLE);
                        } else if (onStateEmail.getVisibility() == View.INVISIBLE) {
                            //turning the state as OFF
                            onStateEmail.setVisibility(View.VISIBLE);
                            offStateEmail.setVisibility(View.INVISIBLE);
                        }

                        // validation for email/text by comparing the states of both
                        if ((onStateText.getVisibility() == View.INVISIBLE) && (onStateEmail.getVisibility() == View.INVISIBLE)||
                                (offStateText.getVisibility() == View.VISIBLE) && (offStateEmail.getVisibility() == View.VISIBLE)) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(getResources().getString(R.string.maintenance_reminders))
                                    .setMessage(getResources().getString(R.string.alert_select_email_text))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {


                                        }
                                    })
                                    .show();
                            //turning the state as ON
                            onStateEmail.setVisibility(View.VISIBLE);
                            offStateEmail.setVisibility(View.INVISIBLE);
                        }
                    }
                });


                switchLayoutText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (offStateText.getVisibility() == View.VISIBLE) {
                            //turning the state as ON
                            onStateText.setVisibility(View.VISIBLE);
                            offStateText.setVisibility(View.INVISIBLE);
                        } else if (offStateText.getVisibility() == View.INVISIBLE) {
                            //turning the state as OFF
                            onStateText.setVisibility(View.INVISIBLE);
                            offStateText.setVisibility(View.VISIBLE);
                        } else if (onStateText.getVisibility() == View.VISIBLE) {
                            //turning the state as ON
                            onStateText.setVisibility(View.INVISIBLE);
                            offStateText.setVisibility(View.VISIBLE);
                        } else if (onStateText.getVisibility() == View.INVISIBLE) {
                            //turning the state as OFF
                            onStateText.setVisibility(View.VISIBLE);
                            offStateText.setVisibility(View.INVISIBLE);
                        }

                        // validation for email/text by comparing the states of both
                        if ((onStateText.getVisibility() == View.INVISIBLE) && (onStateEmail.getVisibility() == View.INVISIBLE)||
                                (offStateText.getVisibility() == View.VISIBLE) && (offStateEmail.getVisibility() == View.VISIBLE)) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(getResources().getString(R.string.maintenance_reminders))
                                    .setMessage(getResources().getString(R.string.alert_select_email_text))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {


                                        }
                                    })
                                    .show();
                            //turning the state as ON
                            onStateText.setVisibility(View.VISIBLE);
                            offStateText.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            //maintenanceTypeSpinner.setEnabled(false);
            mServiceTypeTextView.setEnabled(false);
            service_type_arrow.setEnabled(false);
            service_type_container.setEnabled(false);
            mETAlertName.setHint(mETAlertName.getText());
//           UiStateHelper.disableView(mETAlertName);
            mETAlertName.setEnabled(false);
            deleteEntryBtn.setVisibility(View.VISIBLE);
//            mETAlertName.setFocusable(false);
            if ((mServiceTypeTextView.getText()).equals(getResources().getStringArray(R.array.maintenance_alert_type)[2])) {
                // enabling the service type edit text for Custom and hiding for default ones in the edit mode
                mETAlertName.setVisibility(EditText.VISIBLE);
            } else {
                mETAlertName.setVisibility(EditText.GONE);

            }
            /*if (isCalendarEventExists(isCustomType(reminderData, reminderData.getServiceName()), reminderData.getReminderDate())) {
                addToCalendarBTN.setEnabled(false);
            }
            else
                addToCalendarBTN.setEnabled(true);*/


        }
    }

    /*private void onRepeatOptionClicked(){
        Log.d(TAG, "onClick Repeat option ");

        CustomOptionListFragment fragment = CustomOptionListFragment.createInstance(lstOfPredefinedRepeatOptions,null);

        fragment.setOnItemSelectedListener(new CustomOptionListFragment.OnItemSelected(){
            @Override
            public void onItemSelected(int position, String value) {
                mRepeatOptionTextView.setText(value);
            }
        });
        fragment.show(getFragmentManager(), mContainerId);
    }*/

    private void updateSpinnerForServiceType() {

        //Default Array List
        String[] predefinedServiceTypes = getActivity().getResources().getStringArray(R.array.maintenance_alert_type);
        String[] serviceTypes = getActivity().getResources().getStringArray(R.array.maintenance_service_types);
        lstOfPredefinedServices = Arrays.asList(predefinedServiceTypes);

        setSpinnerData(lstOfPredefinedServices);
        mServiceTypes = Arrays.asList(serviceTypes);
    }


    private void setSpinnerData(List<String> mServiceTypes) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, mServiceTypes);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        //maintenanceTypeSpinner.setAdapter(dataAdapter);
    }


    private void updateFields() {

        if (mReminderData == null) {
            removeFragment();
            return;
        }
        //mPreviousReminderData = mReminderData;
        populateUIElements(mReminderData);
    }

    private MaintenanceReminderData getMaintenanceReminderData(int pos) {
        return transaction.getMaintenanceReminder(pos);
    }

    private void populateUIElements(MaintenanceReminderData reminderData) {
        if (reminderData == null)
            return;

        if (reminderData.getServiceType().equalsIgnoreCase("Custom")) {
            mETAlertName.setVisibility(View.VISIBLE);
            mServiceTypeTextView.setText(getResources().getString(R.string.custom));
            mETAlertName.setText(reminderData.getReminderName());
        } else {
            mETAlertName.setVisibility(View.GONE);
            mServiceTypeTextView.setText(reminderData.getServiceName());
            mETAlertName.setText(reminderData.getServiceName());
        }
        // Description
        mETDetail.setText(reminderData.getReminderDescription() == null ? "" : reminderData.getReminderDescription());

        // Select Date or Mileage option
        mUserSelection = (reminderData.getReminderDate() <= 0) ? USER_SELECTION.MILEAGE : USER_SELECTION.DATE;
        if (mUserSelection.equals(USER_SELECTION.MILEAGE)) {
            onMileageSelected();
            mEdtTxtMileageInterval.setVisibility(View.VISIBLE);
            mEdtTxtMileageInterval.setText(String.valueOf(reminderData.getReminderConfigMiles()));
            addToCalendarBTN.setVisibility(View.GONE);

        } else {
            // Alert Date
            @SuppressLint("SimpleDateFormat") SimpleDateFormat mFormatter = new SimpleDateFormat("MM/dd/yyyy");
            mTextDate.setText(DateDataFormat.convertMillisAsDateString(getActivity(), reminderData.getReminderDate(), mFormatter, false));
            mRepeatOptionEditText.setText("" + reminderData.getReminderConfigMonths());
            addToCalendarBTN.setVisibility(View.VISIBLE);
            dateSelectedCalender.setTimeInMillis(reminderData.getReminderDate());

        }

        // Email Notification flag
        setEmailNotificationSwitchValue(reminderData.isNotificationFlagEmail());

        // sms Notification flag
        setSmsNotificationSwitchValue(reminderData.isNotificationFlagSms());

//      Add  Calendar visibility
        if (isCalendarEventExists(isCustomType(reminderData, reminderData.getServiceName()), reminderData.getReminderDate())) {
                     //addToCalendarBTN.setEnabled(false);
        }


    }

    private String isCustomType(MaintenanceReminderData maintenanceReminderData, String serviceType) {
        if (serviceType.equals("Custom")) {
            Log.d(TAG, "$$$ serviceType " + maintenanceReminderData.getReminderName());
            return maintenanceReminderData.getReminderName();
        } else
            return serviceType;
    }


    private void setSmsNotificationSwitchValue(boolean notificationFlagSms) {
        // Custom switch control require redesign.
        if (notificationFlagSms) {
            onStateText.setVisibility(View.VISIBLE);
            offStateText.setVisibility(View.GONE);
        } else {
            onStateText.setVisibility(View.GONE);
            offStateText.setVisibility(View.VISIBLE);
        }
    }


    private void setEmailNotificationSwitchValue(boolean notificationFlagEmail) {
        // Custom switch control require redesign.
        if (notificationFlagEmail) {
            onStateEmail.setVisibility(View.VISIBLE);
            offStateEmail.setVisibility(View.GONE);
        } else {
            onStateEmail.setVisibility(View.GONE);
            offStateEmail.setVisibility(View.VISIBLE);
        }
    }



    private USER_SELECTION getUserSelection(String selection) {
        if (selection.equals(USER_SELECTION.MILEAGE.toString())) {
            return USER_SELECTION.MILEAGE;
        } else {
            return USER_SELECTION.DATE;
        }
    }

    private int getAlertTypeIndex(String aType) {

        int size = lstOfPredefinedServices.size();
        for (int i = 0; i < size; i++) {
            if (lstOfPredefinedServices.get(i).equalsIgnoreCase(aType))
                return i;
        }
        return (size - 1);

    }

    public void save() {


        if (mServiceTypeTextView == null
                || mETAlertName == null
                || mETDetail == null
                || mTextDate == null
                || getActivity() == null
                || (mEditMode && mReminderData == null))
            return;


        // Service Type
        String serviceType = mServiceType;
        if (!mEditMode && (serviceType == null || serviceType.isEmpty())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.alert_select_service_type), Toast.LENGTH_SHORT).show();
            return;
        }

        String serviceName = mServiceTypeTextView.getText().toString();

        // Reminder Name
        String reminderName = "";
        if (lstOfPredefinedServices.get(lstOfPredefinedServices.size() - 1).equalsIgnoreCase(mServiceTypeTextView.getText().toString())) {
            reminderName = mETAlertName.getText().toString();
            // need to verify and fix appropriate
            serviceName = reminderName;
            if (reminderName.isEmpty()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.alert_fill_custom_reminder), Toast.LENGTH_SHORT).show();
                if (mETAlertName.requestFocus()) {
                    ((InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                }
                return;
            }
        }

        // Description
        String reminderDescription = mETDetail.getText().toString();

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

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.maintenance_reminders))
                        .setMessage(getResources().getString(R.string.alert_enter_mileage_interval))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {


                            }
                        })
                        .show();
                return;

            }

        } else {
            //Added validation when the date button is selected.
            String repeatMonthStr = mRepeatOptionEditText.getText().toString().trim();
            if (!repeatMonthStr.isEmpty()) {
                reminderConfigMonths = Long.parseLong(repeatMonthStr);
            }

            if (reminderConfigMonths <= 0) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.maintenance_reminders))
                        .setMessage(getResources().getString(R.string.alert_frequency_validation))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {


                            }
                        })
                        .show();
                return;
            }
            reminderDate = dateSelectedCalender.getTimeInMillis();
        }

        // email notification flag
        boolean emailNotificationFlag = (onStateEmail.getVisibility() == View.VISIBLE);

        // sms notification flag
        boolean smsNotificationFlag = (onStateText.getVisibility() == View.VISIBLE);

        //Added validation for email and text
        if (!emailNotificationFlag && !smsNotificationFlag) {
            Toast.makeText(getActivity(), getResources().getString(R.string.alert_select_email_text), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mEditMode) {
            MaintenanceReminderData maintenanceReminderData = new MaintenanceReminderData(reminderDescription, smsNotificationFlag, serviceType, 0, 0.0, reminderDate, "", "", reminderName, serviceName, "", 0, emailNotificationFlag, 0);

            // if service Type is custom set serviceName as reminder name
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

    private boolean isDataChanged() {
        if (mReminderData == null)
            return false;
        // compare mReminderData and mPreviousReminderData and return appropriate value
       /* boolean isEqual = false;


        if ((mReminderData.getReminderDescription() != null && (mReminderData.getReminderDescription().equals(mPreviousReminderData.getReminderDescription())))
                && ((mReminderData.isNotificationFlagSms() == mPreviousReminderData.isNotificationFlagSms()))
                && (mReminderData.getServiceType() != null && (mReminderData.getServiceType().equals(mPreviousReminderData.getServiceType()))) &&
                //((mReminderData.getReminderDate() == mPreviousReminderData.getReminderDate())) &&
                (mReminderData.getReminderConfigMiles() == mPreviousReminderData.getReminderConfigMiles()) &&
                (mReminderData.getLastNotificationDate() == mPreviousReminderData.getLastNotificationDate()) &&
                (mReminderData.getSmsPhoneNumber() != null && (mReminderData.getSmsPhoneNumber().equals(mPreviousReminderData.getSmsPhoneNumber()))) &&
                (mReminderData.getEmail() != null && (mReminderData.getEmail().equals(mPreviousReminderData.getEmail())))
//                    &&((mReminderData.getReminderName()!=null &&(mReminderData.getReminderName().equals(mPreviousReminderData.getReminderName()))))
                && (mReminderData.getServiceName() != null && (mReminderData.getServiceName().equals(mPreviousReminderData.getServiceName()))) &&
                (mReminderData.getReminderID() != null && (mReminderData.getReminderID().equals(mPreviousReminderData.getReminderID()))) &&
                (mReminderData.getLastOdometer() == mPreviousReminderData.getLastOdometer()) &&
                (mReminderData.isNotificationFlagEmail() == mPreviousReminderData.isNotificationFlagEmail() &&
                        (mReminderData.getReminderConfigMonths() == mPreviousReminderData.getReminderConfigMonths()))) {

            if (((mReminderData.getReminderName() == null) && (mPreviousReminderData.getReminderName() == null)) || (mReminderData.getReminderName().equals(mPreviousReminderData.getReminderName()))) {

                if (((mReminderData.getReminderDescription() == null) && (mPreviousReminderData.getReminderDescription() == null)) || (mReminderData.getReminderDescription().equals(mPreviousReminderData.getReminderDescription()))) {
                    isEqual = true;
                }

            }
        }
        return !isEqual;*/
        // required editable fields compare.
        return (! ( mReminderData.getReminderDate() == mPreviousReminderData.getReminderDate()
                   && (mReminderData.isNotificationFlagSms() == mPreviousReminderData.isNotificationFlagSms())
                   && (mReminderData.isNotificationFlagEmail() == mPreviousReminderData.isNotificationFlagEmail())
                   && (mReminderData.getReminderConfigMonths() == mPreviousReminderData.getReminderConfigMonths())
                   && (mReminderData.getReminderConfigMiles() == mPreviousReminderData.getReminderConfigMiles())
                   && (((mReminderData.getReminderDescription() == null || mReminderData.getReminderDescription().isEmpty())&& (mPreviousReminderData.getReminderDescription() == null || mPreviousReminderData.getReminderDescription().isEmpty()))
                       || ((mReminderData.getReminderDescription() != null) && mReminderData.getReminderDescription().equals(mPreviousReminderData.getReminderDescription())))
               ));


    }

    public void deleteAlert() {

        ArrayList<String> serviceIds = new ArrayList<String>();
        serviceIds.add(mReminderData.getReminderID());
        DiagnosticController.instance().deleteMaintenanceReminders(getActivity(), serviceIds);
    }

    private void onCreateAlertLog(MaintenanceReminderData reminderData) {

        ArrayList<MaintenanceReminderData> maintenanceReminderData = new ArrayList<MaintenanceReminderData>();
        maintenanceReminderData.add(reminderData);
        DiagnosticController.instance().addMaintenanceReminders(getActivity(), maintenanceReminderData);
        //showProgressDialogFragment("Adding Maintenance Alert");
    }

    private void onEditAlertLog(MaintenanceReminderData reminderData) {

        if (!isDataChanged()) {
            removeFragment();
            return;
        }

//        transaction.updateMaintenanceReminder(pos, data);
//        showProgressDialogFragment("Editing Maintenance Alert");
        ArrayList<MaintenanceReminderData> maintenanceReminderData = new ArrayList<MaintenanceReminderData>();
        maintenanceReminderData.add(reminderData);
        DiagnosticController.instance().updateMaintenanceReminders(getActivity(), maintenanceReminderData);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mProgressDialogFragment = null;
        DiagnosticController.instance().unregister(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressDialogFragment = null;
        DiagnosticController.instance().unregister(this);
        ((HomeActivity) getActivity()).removeFromDoneButtonHandlers(this);
        ((HomeActivity) getActivity()).removeFromLeftArrowHandlers(this);
        ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.create_log_entry), false, true, true,false);
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putLong("minDate", calender.getTimeInMillis());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(dateListener);
        date.show(getFragmentManager(), "Date Picker");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dateBTN:
                BasicUtil.hideKeyboard(getActivity());
                showClickedView(R.id.dateBTN);
                mUserSelection = USER_SELECTION.DATE;
                break;
            case R.id.mileageIntervalBTN:
                BasicUtil.hideKeyboard(getActivity());
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
                Log.d(TAG, "onClick Service Type ");
                // Edit Alert
                BasicUtil.hideKeyboard(getActivity());
                CustomOptionListFragment fragment = CustomOptionListFragment.createInstance(lstOfPredefinedServices, lstDisableServiceTypes, getHomeFragment());
                fragment.setOnItemSelectedListener(new CustomOptionListFragment.OnItemSelected() {
                    @Override
                    public void onItemSelected(int position, String value) {
                        if (mServiceTypes != null)
                            mServiceType = mServiceTypes.get(position);
                        mServiceTypeTextView.setText(value);
                        if (position == (lstOfPredefinedServices.size() - 1)) {
                            // 2 is for Custom.
                            mETAlertName.setVisibility(View.VISIBLE);
                            mETAlertName.setText("");
//                            UiStateHelper.enableView(mETAlertName);
                            if (mETAlertName.requestFocus()) {
                                ((InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                                BasicUtil.showKeyboard(getActivity());
                            }
                        } else {
//                            UiStateHelper.disableView(mETAlertName);
                            mETAlertName.setVisibility(EditText.GONE);
                            BasicUtil.hideKeyboard(getActivity());
                            //mETAlertName.setText(lstOfPredefinedServices.get(position));
                            //BasicUtil.showKeyboard(getActivity());
                        }

                    }

                });
                fragment.setOnDismissListener(new CustomOptionListFragment.OnDismissListener() {
                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onDetach() {
                        if (mEditMode) {
                            ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.edit_alert_entry), true, false, true, true);
                        } else {
                            ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.create_alert_entry), true, false, true, false);
                        }
                    }
                });
                int mContainerId = R.id.mainRL;
                fragment.show(getFragmentManager(), mContainerId);
                break;
            case R.id.deleteAlertBtn:
                showConfirmation();
                break;
            case R.id.addCalenderBtn:
                if (mRepeatOptionEditText.getText().length()==0)
                Toast.makeText(getActivity(),getResources().getString(R.string.alert_enter_frequency), Toast.LENGTH_SHORT).show();

                else if (mServiceTypeTextView.getText().length()==0)
                    Toast.makeText(getActivity(), getResources().getString(R.string.alert_enter_service_type), Toast.LENGTH_SHORT).show();

                else
                addCalendarRemainder();

                break;

            default:
                break;
        }


    }

    private long getRemainderTime() {
        if (mEditMode) {
            return mReminderData.getReminderDate();
        } else {
            return dateSelectedCalender.getTimeInMillis();
        }

    }

    private String getRemainderName() {

        if ((mServiceTypeTextView.getText().toString().equalsIgnoreCase("Custom"))) {
            return mETAlertName.getText().toString();
        } else
            return mServiceTypeTextView.getText().toString();


    }

    private boolean  isCalendarEventExists(String serviceName, long reminderTime) {
        boolean retFlag = false;
//        long reminderTime = getRemainderTime();
        Log.d(TAG, "$$$ serviceName " + serviceName + " $$$ remainder time " + reminderTime);
        ArrayList<RemainderCalendarEvent> previousReminders = CheckReminderOnDeviceUtil.checkCalendarEventByDate(getActivity(), reminderTime);
        if (previousReminders != null) {
            Log.d(TAG, "$$$ remainder events  " + previousReminders);
            for (RemainderCalendarEvent event : previousReminders) {
                if (serviceName.equals(event.getTitle())) {
                    return true;
                }
            }
        }

        return retFlag;
    }


    private void addCalendarRemainder() {

        long remainderTime=0;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = formatter.parse(mTextDate.getText().toString());

            remainderTime = date.getTime();
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(e));
        }



        //Long remainderTime = mTextDate.getText().toString();
       Log.d(TAG, "$$$ remainder time " + remainderTime);
       if(!isCalendarEventExists(getRemainderName() , remainderTime)) {

           new MaintenanceRemainderNotificationService().alertMaintenanceRemainder(getActivity().getApplicationContext(),
                   remainderTime, Integer.parseInt(mRepeatOptionEditText.getText().toString().trim()), getRemainderName());
       }
        else
           Toast.makeText(getActivity(), getResources().getString(R.string.reminder_already_exists), Toast.LENGTH_SHORT).show();



    }

    private void showProgressDialogFragment(String aMessage) {

        if (mProgressDialogFragment == null)
            mProgressDialogFragment = ProgressDialogFragment.createInstance(aMessage, getHomeFragment());

        int mContainerIdForProgressDialog = R.id.rootViewForMaintenanceAlert;
        mProgressDialogFragment.show(getFragmentManager(), mContainerIdForProgressDialog);
    }

    private void showClickedView(int clickedBtnID) {

        if (clickedBtnID == R.id.dateBTN) {

            dateRL.setVisibility(View.VISIBLE);
            mileageRL.setVisibility(View.INVISIBLE);
            dayMonthRL.setVisibility(View.INVISIBLE);
            repeatRL.setVisibility(View.VISIBLE);
            addToCalendarBTN.setVisibility(View.VISIBLE);

            setSelectedEffect(R.drawable.bg_button, R.drawable.bg_button_selected, R.drawable.bg_button);

        } else if (clickedBtnID == R.id.mileageIntervalBTN) {
            onMileageSelected();
        } else {
            dateRL.setVisibility(View.INVISIBLE);
            mileageRL.setVisibility(View.INVISIBLE);
            dayMonthRL.setVisibility(View.VISIBLE);

            setSelectedEffect(R.drawable.bg_button, R.drawable.bg_button, R.drawable.bg_button_selected);
        }


    }

    private void onMileageSelected() {

        dateRL.setVisibility(View.INVISIBLE);
        mileageRL.setVisibility(View.VISIBLE);
        dayMonthRL.setVisibility(View.INVISIBLE);
        repeatRL.setVisibility(View.GONE);
        emailRL.setVisibility(View.VISIBLE);
        textRL.setVisibility(View.VISIBLE);
        addToCalendarBTN.setVisibility(View.GONE);


        setSelectedEffect(R.drawable.bg_button_selected, R.drawable.bg_button, R.drawable.bg_button);
    }

    private void setSelectedEffect(int dateBtnColor, int mileageBtnColor, int dayMonthColor) {

        dateBTN.setBackgroundResource(dateBtnColor);
        mileageIntervalBTN.setBackgroundResource(mileageBtnColor);
        dayBTN.setBackgroundResource(dayMonthColor);
    }


    private void removeFragment() {

        if (getFragmentManager() == null || getActivity() == null)
            return;
        BasicUtil.hideKeyboard(getActivity());
        getHomeFragment().popFragmentStack();
        //getFragmentManager().popBackStack();
        DiagnosticController.instance().unregister(this);
        //MaintenanceReminderFragment.imgBtnAddLog.setEnabled(true);
    }


    @Override
    public void onTopBarItemClick(View aView) {
        if (aView.getId() == HomeActivity.LEFT_ARROW_ID) {
            removeFragment();
            BasicUtil.hideKeyboard(getActivity());

        } else if (aView.getId() == HomeActivity.DONE_BUTTON_ID) {
            BasicUtil.hideKeyboard(getActivity());
            save();
        }

    }

    private void showConfirmation() {

        new AlertDialog.Builder(getActivity())
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


}