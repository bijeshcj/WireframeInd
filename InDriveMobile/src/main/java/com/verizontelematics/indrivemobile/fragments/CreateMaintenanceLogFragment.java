package com.verizontelematics.indrivemobile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceLogTable;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.MaintenanceLogData;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.BasicUtil;
import com.verizontelematics.indrivemobile.utils.ui.CurrencyFormat;
import com.verizontelematics.indrivemobile.utils.ui.CustomOptionListFragment;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;
import com.verizontelematics.indrivemobile.utils.ui.InputFilterMinMax;
import com.verizontelematics.indrivemobile.utils.ui.UiStateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.verizontelematics.indrivemobile.models.LogModel;
//import com.verizontelematics.indrivemobile.models.LogsModel;

/**
 * Created by Priyanga on 8/28/2014.
 */
public class CreateMaintenanceLogFragment extends BaseSubUIFragment implements View.OnClickListener, HomeActivity.CustomTopBarItemsClickListener, UIInterface {

    private int mContainerId = R.id.container_id_diagnostics;

    private static final String TAG = CreateMaintenanceLogFragment.class.getCanonicalName();
    //Spinner serviceReceivedSpinner;
    private TextView dateET;
    private EditText mEdtAlertName;
    private EditText mEdtDesc,mEdtCost,mEdtMileage;
    private Button mCreateLogBtn;
    private Button mCancelBtn;

    private boolean mEditMode =  false;
    private List<String> lstOfPredefinedServices;
    private List<String> mServiceTypes;
    private static final String DEFAULT_COST ="00.00";
    private static final int NOT_FOUND = -1;
    Calendar dateSelectedCalender = Calendar.getInstance();
    private TextView mServiceTypeTextView;

    private static final int LENGTH_THRESHOLD_AFTER_DOT = 3;

    private static final int DIGITS_THRESHOLD_BEFORE_DOT = 2;
    private static final int DIGIT_MOVE_MULTIPLIER_SCALE = 10;
    private static final int DIGIT_MINIMUM_COST_LENGTH = 5;
    private String mServiceType;
    private MaintenanceLogData mMaintenanceLogData = null;

    private int titleResourceId = 0;
    private ProgressDialogFragment mProgressDialogFragment = null;
    private EditText locationET;

    public CreateMaintenanceLogFragment(){

    }

    private void initTitleResourceId(){
        if (getArguments() != null) {
            mEditMode = getArguments().getBoolean("edit_mode", false);
        }
        titleResourceId = mEditMode ? R.string.accept_changes : R.string.create_log_entry;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // to make sure all day event.
        dateSelectedCalender.set(Calendar.HOUR_OF_DAY, 0);
        dateSelectedCalender.set(Calendar.MINUTE, 0);
        dateSelectedCalender.set(Calendar.SECOND, 0);

        ((HomeActivity)getActivity()).hideActionToggleButton();

      //  ((HomeActivity)getActivity()).hideActionToggleButton();




         View rootView = inflater.inflate(R.layout.fragment_create_logentry,container,false);
        //rootView.findViewById(R.id.service_type_arrow_btn);

        dateSelectedCalender=Calendar.getInstance();

        setHasOptionsMenu(true);

        //serviceReceivedSpinner =(Spinner)rootView.findViewById(R.id.serviceReceivedSpinner);

        locationET = (EditText)rootView.findViewById(R.id.locationET);
        mServiceTypeTextView =  (TextView) rootView.findViewById(R.id.serviceType);
        LinearLayout serviceContainer=(LinearLayout)rootView.findViewById(R.id.service_type_container);
        mServiceTypeTextView.setOnClickListener(this);
        rootView.findViewById(R.id.service_type_container).setOnClickListener(this);
        ImageButton service_Arrow=(ImageButton)rootView.findViewById(R.id.service_type_arrow_btn);
                service_Arrow.setOnClickListener(this);
        Button mDeleteLogBtn = (Button) rootView.findViewById(R.id.deleteLogBtn);
        mDeleteLogBtn.setVisibility(View.INVISIBLE);
        mCreateLogBtn = (Button)rootView.findViewById(R.id.createLogBTN);
        mEdtAlertName = (EditText)rootView.findViewById(R.id.alertNameET);
        mEdtDesc = (EditText)rootView.findViewById(R.id.alertDescET);

        mEdtDesc.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() ==R.id.alertDescET) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        mEdtCost = (EditText)rootView.findViewById(R.id.costET);
//        mEdtCost.setText(DEFAULT_COST);
        mEdtMileage = (EditText)rootView.findViewById(R.id.mileageET);
        mEdtMileage.setFilters(new InputFilter[]{new InputFilterMinMax(AppConstants.MIN_MILEAGE_SIZE, AppConstants.MAX_MILEAGE_SIZE, getActivity(), AppConstants.USER_SELECTION.MILEAGE)});
        dateET=(TextView)rootView.findViewById(R.id.dateET);
        dateET.setText(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));

        mCancelBtn = (Button)rootView.findViewById(R.id.cancelBTN);

        populateSpinner();


        // set edit/create label depends on mode of selection
        mCreateLogBtn.setText(getResources().getString(titleResourceId));

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        mDeleteLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmation();
            }
        });

        setClickListeners();
//      UiStateHelper.disableView(mEdtAlertName);
        //Priyanga : Fix for QC Defect  #29976
        mEdtAlertName.setVisibility(View.GONE);

        rootView.findViewById(R.id.costRL).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.findViewById(R.id.costET).requestFocus();
                BasicUtil.showKeyboard(getActivity());
            }
        });

        rootView.findViewById(R.id.dateRL).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        rootView.findViewById(R.id.mileageRL).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.findViewById(R.id.mileageET).requestFocus();
                BasicUtil.showKeyboard(getActivity());
            }
        });


        if(mEditMode){
            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.edit_log_entry), true,false,true,true);

            mMaintenanceLogData = getArguments().getParcelable("selected_data");
            updateUI(mMaintenanceLogData);
            //loadData(mSelectedIndex);
            //UiStateHelper.disableView(serviceReceivedSpinner);
            UiStateHelper.disableView(mServiceTypeTextView);
            UiStateHelper.disableView(mEdtAlertName);
            mEdtAlertName.setHint(mEdtAlertName.getText());
            mDeleteLogBtn.setVisibility(View.VISIBLE);
            mEdtAlertName.setFocusable(false);
            service_Arrow.setEnabled(false);
            mServiceTypeTextView.setEnabled(false);
            serviceContainer.setEnabled(false);
            if((mServiceTypeTextView.getText()).equals(getResources().getStringArray(R.array.maintenance_alert_type)[2])){
                // enabling the service type edit text for Custom and hiding for default ones in the edit mode
                mEdtAlertName.setVisibility(EditText.VISIBLE);
            }
            else{
                mEdtAlertName.setVisibility(EditText.GONE);
            }
        }



        mEdtCost.addTextChangedListener(new TextWatcher() {
            boolean isEditing;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isEditing) return;
                isEditing = true;
                String formattedString = CurrencyFormat.formatDoubleAsCurrency(editable.toString().trim());
                editable.replace(0, editable.length(), formattedString);

                isEditing = false;
            }
        });

        ((HomeActivity)getActivity()).addToDoneButtonClickHandlers(this);
        ((HomeActivity)getActivity()).addToLeftArrowClickHandlers(this);


        DiagnosticController.instance().register(this);

        return rootView;
    }



    private String formatCost(double cost){

        String lCost = ""+cost;
        int positionOfDot = getLocationOfDot(lCost);
        int lLength = lCost.length();
        if((lLength - positionOfDot) > LENGTH_THRESHOLD_AFTER_DOT){
            int lMaxLength = positionOfDot + LENGTH_THRESHOLD_AFTER_DOT;
            lCost = lCost.substring(0,lMaxLength);
        }

        return lCost;
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        ((HomeActivity)activity).showTabBar(false);
        initTitleResourceId();
        ((HomeActivity)activity).setCustomActionBarView(activity.getResources().getString(titleResourceId), true,false,true,false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProgressDialogFragment = null;
        DiagnosticController.instance().unregister(this);
        if (!mEditMode)
            ((HomeActivity)getActivity()).showTabBar(true);
        ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2],true,false,true,true);
    }


    private String prependWithZero(String aString){
        int locationOfDot = getLocationOfDot(aString);
        // Prepend with 0.
        if(locationOfDot < DIGITS_THRESHOLD_BEFORE_DOT){
            int lDiff = 0;
            String lTemp = "";
            do{
                lTemp = "0"+lTemp;
                lDiff++;
            }while(locationOfDot +lDiff < DIGITS_THRESHOLD_BEFORE_DOT);
            // There are less then 2 digits before .
            aString = lTemp+aString;
        }
        return aString;
    }

    private String appendZero(String aString){
        int locationOfDot = getLocationOfDot(aString);
        // Prepend with 0.
        if(locationOfDot > (aString.length() - LENGTH_THRESHOLD_AFTER_DOT)){
            int lDiff = (aString.length()-LENGTH_THRESHOLD_AFTER_DOT) - locationOfDot;
            StringBuilder lTemp = new StringBuilder();
            do{
                lTemp = lTemp.append("0");
                lDiff--;
            }while(lDiff > 0);
            // There are less then 2 digits before .
            aString = aString+lTemp.toString();
        }
        return aString;
    }



    private int getLocationOfDot(String aText){
        return aText.indexOf('.');
    }

    private void populateSpinner(){
//        DBUtils.pullDbFromLocalStorageToSDCard();
        String[] predefinedServiceTypes = getActivity().getResources().getStringArray(R.array.maintenance_alert_type);
        String[] serviceTypes = getActivity().getResources().getStringArray(R.array.maintenance_service_types);
        lstOfPredefinedServices = Arrays.asList(predefinedServiceTypes);

        mServiceTypes = Arrays.asList(serviceTypes);

    }



    private void loadData(int aIndex){

        StorageTransaction transaction = new StorageTransaction(getActivity());
        mMaintenanceLogData = transaction.getMaintenanceLog(aIndex);
        updateUI(mMaintenanceLogData);

    }

    private void updateUI(MaintenanceLogData maintenance){
        if (maintenance == null)
            return;
//        int index = getAlertTypeIndex(maintenance.getServiceType());
//
//        getServiceType.setSelection(index);
        if (maintenance.getServiceType().equalsIgnoreCase("Custom")) {
            mEdtAlertName.setVisibility(View.VISIBLE);
            mServiceTypeTextView.setText(getResources().getString(R.string.custom));
            mEdtAlertName.setText(maintenance.getServiceName());
        }
        else {
            mEdtAlertName.setVisibility(View.GONE);
            mServiceTypeTextView.setText(maintenance.getServiceName());
            mEdtAlertName.setText(maintenance.getServiceName());
        }

        mEdtDesc.setText(maintenance.getDescription());

        mEdtCost.setText(CurrencyFormat.applyCurrencyFormat(maintenance.getCost()));
        // temp fix but need discussion and fix appropriate.
        mEdtMileage.setText((maintenance.getMileage().isEmpty() || maintenance.getMileage().equalsIgnoreCase("0")) ? "" : ""+maintenance.getMileage());
        dateET.setText(DateDataFormat.convertMillisAsDateString(getActivity(), maintenance.getServiceDate(), false));
        locationET.setText(maintenance.getServiceCenter());
    }



    private int getAlertTypeIndex(String aType) {

//        String[] alertTypes = getResources().getStringArray(R.array.maintenance_alert_type);
        for(int i = 0; i < lstOfPredefinedServices.size(); i++) {
            if (lstOfPredefinedServices.get(i).equalsIgnoreCase(aType))
                return i;
        }
        return (lstOfPredefinedServices.size() - 1);

    }

    private void setClickListeners(){
        mCreateLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEditMode){
                    onCreateLog();
                }else{
                    onEditLog();
                }


             //   MaintenanceLogAlertFragment.mCursorAdapter.changeCursor(new StorageTransaction(getActivity()).getAllData( MaintenanceLogTable.TABLE_NAME_MAINTENANCE));
                //BasicUtil.hideKeyboard(getActivity());
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();

            }
        });
    }

    private void removeFragment() {
        getHomeFragment().popFragmentStack();
        //getFragmentManager().popBackStack();
        BasicUtil.hideKeyboard(getActivity());
        // safe unregister.
        DiagnosticController.instance().unregister(this);
        //MaintenanceLogFragment.imgBtnAddRemainder.setEnabled(true);
    }



    private boolean onCreateLog(){
        String serviceType = mServiceTypeTextView.getText().toString();//serviceReceivedSpinner.getSelectedItem().toString();
        serviceType = mServiceType;
        if(serviceType==null || serviceType.equalsIgnoreCase(getResources().getString(R.string.maintenance_prompt)))
        {
            Toast.makeText(getActivity(),getResources().getString(R.string.alert_select_service_type),Toast.LENGTH_SHORT).show();
            return false;

        }

        String alertType = mEdtAlertName.getText().toString();
        if(serviceType.equalsIgnoreCase("Custom"))
        {
            if(alertType.isEmpty())
            {
                Toast.makeText(getActivity(),getResources().getString(R.string.please_enter_custom_service_name),Toast.LENGTH_SHORT).show();
                if (mEdtAlertName.requestFocus()) {
                    ((InputMethodManager)getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                }
                return false;
            }
        }

        String descType = mEdtDesc.getText().toString();
        String serviceCentre = locationET.getText().toString();
        double cost = Double.valueOf(getCost());
        MaintenanceLogData maintenance;
        if (mEdtMileage.getText().toString().isEmpty()) {
            maintenance = new MaintenanceLogData(serviceType, alertType,
                    descType, cost, "", dateSelectedCalender,serviceCentre);
        }else {
            double mileage = Double.valueOf(mEdtMileage.getText().toString());
            maintenance = new MaintenanceLogData(serviceType, alertType,
                    descType, cost, mileage, dateSelectedCalender,serviceCentre);
        }



        ArrayList<MaintenanceLogData> maintenanceLogData = new ArrayList<MaintenanceLogData>();
        maintenanceLogData.add(maintenance);
        DiagnosticController.instance().addMaintenanceLogs(getActivity(), maintenanceLogData);


        return true;
    }

    private String getCost(){
        String cost = mEdtCost.getText().toString().length() == 0? "0":mEdtCost.getText().toString();
        return CurrencyFormat.removeCurrencyFormat(cost);
    }

    private void showProgressDialogFragment(String aMessage){

        if (mProgressDialogFragment == null)
            mProgressDialogFragment = ProgressDialogFragment.createInstance(aMessage, getHomeFragment());

        int mContainerIdForProgressDialog = R.id.rootViewForMaintenanceLog;
        mProgressDialogFragment.show(getFragmentManager(), mContainerIdForProgressDialog);
    }

    private boolean onEditLog(){
        if (mMaintenanceLogData == null)
            return false;
        long epochTime = dateSelectedCalender.getTimeInMillis();
        String description = mEdtDesc.getText().toString();
        double cost = Double.valueOf(getCost());
        mMaintenanceLogData.setDescription(description);
        mMaintenanceLogData.setCost(""+cost);
        mMaintenanceLogData.setMileage(mEdtMileage.getText().toString());
        mMaintenanceLogData.setServiceDate(epochTime);
        mMaintenanceLogData.setServiceCenter(locationET.getText().toString());
        ArrayList<MaintenanceLogData> maintenanceLogDataList = new ArrayList<MaintenanceLogData>();
        maintenanceLogDataList.add(mMaintenanceLogData);
        DiagnosticController.instance().updateMaintenanceLogs(getActivity(), maintenanceLogDataList);
        return true;
    }

    private boolean onEditLog(int pos){

        if (mMaintenanceLogData == null)
            return false;
        //String serviceType = serviceReceivedSpinner.getSelectedItem().toString();

        //String alertType = mEdtAlertName.getText().toString();
        long epochTime = dateSelectedCalender.getTimeInMillis();
        String description = mEdtDesc.getText().toString();
        double cost = Double.valueOf(getCost());
        //long mileage = new Long(mEdtMileage.getText().toString().length() == 0?"0":);

        mMaintenanceLogData.setDescription(description);
        mMaintenanceLogData.setCost(""+cost);
        mMaintenanceLogData.setMileage(mEdtMileage.getText().toString());
        mMaintenanceLogData.setServiceDate(epochTime);

//        StorageTransaction transaction = new StorageTransaction(getActivity());
//        transaction.updateMaintenanceLog(pos, maintenance);

        ArrayList<MaintenanceLogData> maintenanceLogDataList = new ArrayList<MaintenanceLogData>();
        maintenanceLogDataList.add(mMaintenanceLogData);
        DiagnosticController.instance().updateMaintenanceLogs(getActivity(), maintenanceLogDataList);



        //removeFragment();
        return true;
    }

    private void showDatePicker()
    {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putLong("maxDate", calender.getTimeInMillis());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(dateSetListener);
        date.show(getFragmentManager(), "Date Picker");

    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            dateET.setText((monthOfYear + 1) + "/" + dayOfMonth+ "/"+year);
            dateSelectedCalender = Calendar.getInstance();
            dateSelectedCalender.set(Calendar.YEAR, year);
            dateSelectedCalender.set(Calendar.MONTH, monthOfYear);
            dateSelectedCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        }
    };


        /*if(mEditMode)
            inflater.inflate(R.menu.maintenance_log_edit, menu);
        else
        inflater.inflate(R.menu.maintenance_log_creation, menu);*/


        /*if (mEditMode) {
            inflater.inflate(R.menu.maintenance_log_edit, menu);
        } else {
            inflater.inflate(R.menu.maintenance_log_creation, menu);
        }

    }*/





    public void deleteLog(int mSelectedIndex) {
        /*StorageTransaction transaction = new StorageTransaction(getActivity());
        // remove it from db.. check if this works.
        transaction.removeRowForIdAndColumn(mSelectedIndex, MaintenanceLogTable.TABLE_NAME_MAINTENANCE, MaintenanceLogTable.COLUMN_MAINTENANCE_Id);
        MaintenanceLogAlertFragment.mCursorAdapter.changeCursor(new StorageTransaction(getActivity()).getAllData(MaintenanceLogTable.TABLE_NAME_MAINTENANCE,new boolean[]{false,true}, MaintenanceLogTable.COLUMN_DATE,MaintenanceLogTable.COLUMN_ALERT_NAME));
       // showProgressDialogFragment(" Deleting Maintenance Log ...");
        removeFragment();*/

        if (getActivity() == null) {
            return;
        }
        ArrayList<String> serviceIds = new ArrayList<String>();
        StorageTransaction transaction = new StorageTransaction(getActivity());
        MaintenanceLogData mNewData= transaction.getMaintenanceLog(mSelectedIndex);
        serviceIds.add(mNewData.getServiceID());
        DiagnosticController.instance().deleteMaintenanceLogs(getActivity(), serviceIds);
        //removeFragment();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            //case R.id.edit_log_entry:
            case R.id.create_log_entry:
                boolean status = false;
                if(!mEditMode){
                    status = onCreateLog();
                }else{
                    status = onEditLog(mSelectedIndex);
                }
                if (status) {
                    MaintenanceLogAlertFragment.mCursorAdapter.changeCursor(new StorageTransaction(getActivity()).getAllData(MaintenanceLogTable.TABLE_NAME_MAINTENANCE, true, MaintenanceLogTable.COLUMN_DATE,MaintenanceLogTable.COLUMN_ALERT_NAME));
                    BasicUtil.hideKeyboard(getActivity());
                }



                return true;

            *//*case R.id.editLog:
                onEditLog(mSelectedIndex);


                loadData(mSelectedIndex);

            case R.id.cancel:*//*

            case R.id.cancel: {

                removeFragment();
                BasicUtil.hideKeyboard(getActivity());
                return true;
            }

            *//*case R.id.delete_log:{
                deleteLog();
                return true;
            }*//*
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public  final String DATE_FORMAT = "yyyyMMddHHmmss";
    @SuppressLint("SimpleDateFormat")
    private  final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);


    public  Calendar getCalendarFromFormattedLong(long l){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormat.parse(String.valueOf(l)));
            return c;

        } catch (ParseException e) {
            return null;
        }
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
        ((HomeActivity)getActivity()).removeFromDoneButtonHandlers(this);
        ((HomeActivity)getActivity()).removeFromLeftArrowHandlers(this);
        //((HomeActivity)getActivity()).removeFromCancelHandlers(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.serviceType:
            case R.id.service_type_container:
            case R.id.service_type_arrow_btn:
                BasicUtil.hideKeyboard(getActivity());
                // Edit Alert
                CustomOptionListFragment fragment = CustomOptionListFragment.createInstance(lstOfPredefinedServices,null, getHomeFragment());
                fragment.setOnItemSelectedListener(new CustomOptionListFragment.OnItemSelected() {
                    @Override
                    public void onItemSelected(int position, String value) {
                        if (mServiceTypes != null)
                            mServiceType = mServiceTypes.get(position);
                        mServiceTypeTextView.setText(value);
                        if (position == (lstOfPredefinedServices.size() - 1)) {
                            // 2 is for Custom.
                            //enabling the service type edit text for the user to enter the service name of custom
                            mEdtAlertName.setVisibility(View.VISIBLE);
                            mEdtAlertName.setText("");
                            UiStateHelper.enableView(mEdtAlertName);

                            if (mEdtAlertName.requestFocus()) {
                                ((InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                                BasicUtil.showKeyboard(getActivity());
                            }
                        } else {
                            //UiStateHelper.disableView(mEdtAlertName);
                            mEdtAlertName.setVisibility(EditText.GONE);
                            //hiding the service type edit text for the default service types
                            BasicUtil.hideKeyboard(getActivity());
                            //mETAlertName.setText(lstOfPredefinedServices.get(position));
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
                            ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.edit_log_entry), true, false, true, true);
                        } else {
                            ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.create_log_entry), true, false, true, false);
                        }
                    }
                });
                int mContainerId_SL = R.id.mainRL;
                fragment.show(getFragmentManager(), mContainerId_SL);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTopBarItemClick(View aView) {
        if(aView.getId() == HomeActivity.LEFT_ARROW_ID){
            removeFragment();
            BasicUtil.hideKeyboard(getActivity());
            return;

        }
        if(aView.getId() == HomeActivity.DONE_BUTTON_ID){
            if(!mEditMode){
                onCreateLog();
            }else{
                //onEditLog(mSelectedIndex);
                onEditLog();
            }
            BasicUtil.hideKeyboard(getActivity());
        }


        MaintenanceLogFragment.mCursorAdapter.changeCursor(new StorageTransaction(getActivity()).getAllData(MaintenanceLogTable.TABLE_NAME_MAINTENANCE,new boolean[]{false,true},MaintenanceLogTable.COLUMN_DATE,MaintenanceLogTable.COLUMN_ALERT_NAME));

    }

    private void showConfirmation(){

        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.delete_maintenance_log_header))
                .setMessage(getResources().getString(R.string.alert_delete))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        //deleteLog(mSelectedIndex);
                        deleteLog();

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteLog() {
        if (getActivity() == null)
            return;
        ArrayList<String> serviceIds = new ArrayList<String>();
        serviceIds.add(mMaintenanceLogData.getServiceID());
        DiagnosticController.instance().deleteMaintenanceLogs(getActivity(), serviceIds);
    }


    @Override
    public void onProgress(final Operation opr) {

        if (getActivity() == null || opr == null)
            return;

        if (opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_LOGS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_LOG.ordinal()
                && opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()) {
            return;
        }
        String lAddMaintenanceLogMessage = getResources().getString(R.string.adding_maintenance_log);//"Adding Maintenance Log";
        String lEditMaintenanceLogMessage = getResources().getString(R.string.editing_maintenance_log);//"Editing Maintenance Log";
        String lDeleting = getResources().getString(R.string.deleting_maintenance_log);//"Deleting Maintenance Log";
            if (opr.getId() == Operation.OperationCode.ADD_MAINTENANCE_LOGS.ordinal()) {
                showProgressDialogFragment("");
            } else if (opr.getId() == Operation.OperationCode.UPDATE_MAINTENANCE_LOG.ordinal()) {
                showProgressDialogFragment("");
            } else if (opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()) {
                showProgressDialogFragment("");
            }

    }

    @Override
    public void onError(Operation opr) {

        if (getActivity() == null || opr == null)
            return;

        if (opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_LOGS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_LOG.ordinal()
                && opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()) {
            return;
        }
        if(mProgressDialogFragment != null) {
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
        if (opr.getId() != Operation.OperationCode.ADD_MAINTENANCE_LOGS.ordinal()
                && opr.getId() != Operation.OperationCode.UPDATE_MAINTENANCE_LOG.ordinal()
                && opr.getId() != Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()) {
            return;
        }
        if(mProgressDialogFragment != null) {
            mProgressDialogFragment.dismiss();
            mProgressDialogFragment = null;
        }
        // remove edit log fragment.
        removeFragment();
        // on delete
        // remove log detailed fragment and go back to log list fragment
        if (opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal())
            removeFragment();
    }

    //Commented the below part as part of warnings, its a dead code not getting used anywhere
    /*
    @Deprecated
    private void currencyFormatter(){
        TextWatcher watcher = new TextWatcher() {
            private String lStringBeforeChange;
            private boolean ignoreTextChangeEvent = false;
            private int cursorPositionBeforeEdit=-1;

            private void selSelection(int aPosition){

                if(aPosition > lStringBeforeChange.length()){
                    aPosition = lStringBeforeChange.length();
                }
                mEdtCost.setSelection(aPosition);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(ignoreTextChangeEvent){
                    return;
                }
                // check the string before deletion/addition.
                lStringBeforeChange = s.toString();
                cursorPositionBeforeEdit = mEdtCost.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                //((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.create_log_entry), true,false,true);



            }

            private void updateStringForCost(double aCost, int aCursorPosition){

                // remove the last digits from the cost.
                ignoreTextChangeEvent = true;
                boolean lCursorWasAtEnd = false;
                if(lStringBeforeChange.length() ==(cursorPositionBeforeEdit)){
                    lCursorWasAtEnd = true;
                }
                lStringBeforeChange = formatCost(aCost);
                lStringBeforeChange = appendZero(lStringBeforeChange);
                lStringBeforeChange = prependWithZero(lStringBeforeChange);

                mEdtCost.setText(lStringBeforeChange);
                if (aCursorPosition > lStringBeforeChange.length() || lCursorWasAtEnd) {
                    aCursorPosition = lStringBeforeChange.length();
                }
                mEdtCost.setSelection(aCursorPosition);
            }

            @Override
            public void afterTextChanged(Editable s) {


                if(ignoreTextChangeEvent){
                    ignoreTextChangeEvent = false;
                    return;
                }
                int lCursorPosition = mEdtCost.getSelectionStart();
                int locationOfDotAfterEdit = getLocationOfDot(s.toString());
                if(locationOfDotAfterEdit == NOT_FOUND){
                    ignoreTextChangeEvent = true;

                    mEdtCost.setText(lStringBeforeChange, TextView.BufferType.EDITABLE);
                    // move cursor one position to the left.
                    mEdtCost.setSelection(lCursorPosition);
                    return;
                }
                // find out where the digit addition/removal took place.
                int lDotPositionBeforeEdit = getLocationOfDot(lStringBeforeChange);
                int lLengthBeforeEdit = lStringBeforeChange.length();
                int lLengthAfterEdit = s.length();
                double costBeforeEdit = 0;
                double costAfterEdit = 0;

                try {
                    costBeforeEdit = Double.valueOf(lStringBeforeChange);
                    costAfterEdit = Double.valueOf(s.toString());
                }catch(NumberFormatException e){
                    Log.e(TAG, "Exception is " + Log.getStackTraceString(e));

                }

                if(costAfterEdit == 0){
                    ignoreTextChangeEvent = true;
                    lStringBeforeChange = "00.00";
                    // keep the cursor position at the same index after dot as it was before edit.

                    mEdtCost.setText(lStringBeforeChange);
                    lCursorPosition = cursorPositionBeforeEdit;
                    selSelection(lCursorPosition);
                    return;
                }

                if(lLengthBeforeEdit > lLengthAfterEdit){
                    // we deleted some numbers.
                    // find out where the deletion happened.
                    if(lDotPositionBeforeEdit > locationOfDotAfterEdit){
                        // we are short of the digits before . so we will add digits in updateString.
                        if(locationOfDotAfterEdit < DIGITS_THRESHOLD_BEFORE_DOT){
                            lCursorPosition++;
                        }
                        updateStringForCost(costAfterEdit,lCursorPosition);
                        // we deleted some number before . so do nothing.
                    }else {
                        if (lCursorPosition < lLengthAfterEdit && lDotPositionBeforeEdit == locationOfDotAfterEdit) {
                            // don't do anything user has deleted the number between . and end of text.
                            // remove this number and move . to one position to the left.
                            if(lLengthAfterEdit < DIGIT_MINIMUM_COST_LENGTH){
                                // since the updateStringForCost will prepend/append 0 we need to take care of that.
                                lCursorPosition++;
                            }
                            updateStringForCost(costAfterEdit/DIGIT_MOVE_MULTIPLIER_SCALE,lCursorPosition);
                            return;
                        }
                        // we deleted some number after dot, so divide the number by 10.
                        // we need to divide it by 10 only if the number is the last one.
                        costBeforeEdit = costBeforeEdit / DIGIT_MOVE_MULTIPLIER_SCALE;
                        updateStringForCost(costBeforeEdit, cursorPositionBeforeEdit);
                    }
                }else{
                    // we added numbers.
                    if(lDotPositionBeforeEdit < locationOfDotAfterEdit){
                        // we added numbers before dot .. do nothing.
                        updateStringForCost(costAfterEdit,lCursorPosition);
                        locationOfDotAfterEdit = getLocationOfDot(lStringBeforeChange);
                        if(lDotPositionBeforeEdit - locationOfDotAfterEdit == 0){
                            lCursorPosition--;
                        }
                        selSelection(lCursorPosition);
                    } else if((s.toString().length() - locationOfDotAfterEdit) <= LENGTH_THRESHOLD_AFTER_DOT){
                        // we added numbers after dot, but we are still in the decimal points limit so do nothing.
                        return;
                    }else{
                        int lLengthOfStringBeforeEdit = lStringBeforeChange.length();
                        // we added some number
                        costAfterEdit = costAfterEdit * DIGIT_MOVE_MULTIPLIER_SCALE;
                        updateStringForCost(costAfterEdit,lCursorPosition);
                        // if the length of strings remained unchanged then reduce cursor position.
                        if(lLengthOfStringBeforeEdit - lStringBeforeChange.length() == 0){
                            lCursorPosition--;
                        }
                        selSelection(lCursorPosition);
                    }
                }
            }
        };
    }
    */

}
