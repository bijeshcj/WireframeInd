package com.verizontelematics.indrivemobile.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.adapters.cursoradapters.MaintenanceLogCursorAdapter;
import com.verizontelematics.indrivemobile.adapters.cursoradapters.MaintenanceReminderCursorAdapter;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceReminderTable;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceLogTable;
import com.verizontelematics.indrivemobile.models.MaintenanceLogsModel;
import com.verizontelematics.indrivemobile.models.MaintenanceRemindersModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.MaintenanceLogData;
import com.verizontelematics.indrivemobile.models.data.MaintenanceReminderData;
import com.verizontelematics.indrivemobile.utils.ui.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;



/**
 * Created by Priyanga on 8/28/2014.
 */

public class MaintenanceInformationFragment extends Fragment implements UIInterface, Observer {

    private ListView maintenanceListView;
    private ListView mMaintenanceAlertList;
    private String TAG = MaintenanceInformationFragment.class.getCanonicalName();
    private int mContainerId = R.id.container_id_diagnostics;
    private static MaintenanceLogCursorAdapter mCursorAdapter;
    private static MaintenanceReminderCursorAdapter mAlertsCursorAdapter;
    private ImageView mLogInfoIV;
    private ImageView mAlertInfoIV;
    public static ImageView imgBtnAddRemainder,imgBtnAddLog;
    private TextView empty_Alert_TV,empty_Log_TV;

    private SwipeRefreshLayout mLogsRefreshLayout;

    public static final String PRE_EXISTS_SERVICE_TYPE ="PRE_EXISTS_SERVICE_TYPE";



    ArrayList<Integer> lstDisableServiceTypes;
    private SwipeRefreshLayout mAlertsRefreshLayout;


    public MaintenanceInformationFragment(){
//        Empty constructor
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            getActivity().setTitle(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2]);
            //getActivity().setTitle("Edit Log Entry");

        }
        else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2]);

        //Getting the list item counts to show the visibility accordingly
        int alerts_Count = mMaintenanceAlertList.getCount();
        if (alerts_Count == 0) {
            empty_Alert_TV.setVisibility(TextView.VISIBLE);
        } else {
            empty_Alert_TV.setVisibility(TextView.INVISIBLE);
        }
        /*if (alerts_Count==3) {
            imgBtnAddRemainder.setVisibility(View.GONE);
        }*/

        int logs_Count = maintenanceListView.getCount();
        if (logs_Count == 0)
            empty_Log_TV.setVisibility(TextView.VISIBLE);
        else
            empty_Log_TV.setVisibility(TextView.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootView = inflater.inflate(R.layout.fragment_maintenancelog_alert_land, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_maintenancelog_alert, container, false);
        }
        init(rootView);
        return rootView;

    }

    private void init(View rootView) {

        empty_Alert_TV= (TextView)rootView.findViewById(R.id.emptyAlertTV);
        empty_Log_TV=(TextView)rootView.findViewById(R.id.emptyLogTV);


        if (getArguments() != null) {
            mContainerId = getArguments().getInt("container", R.id.container_id_diagnostics);
        }



        // Maintenance Alert Logs list
        maintenanceListView = (ListView)rootView.findViewById(R.id.maintenanceLogLV);
        mLogInfoIV = (ImageView)rootView.findViewById(R.id.btn_info_maintenance_log);
        mAlertInfoIV = (ImageView)rootView.findViewById(R.id.btn_info_maintenance_alert);
        populateMaintenanceLogs();
        mMaintenanceAlertList = (ListView) rootView.findViewById(R.id.maintenance_alerts_list);
        setListClickListener();
        setClickListeners();

        mMaintenanceAlertList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.d(TAG, "onItemClick " + position);

                Cursor cursor =(Cursor) mAlertsCursorAdapter.getItem(position);
                if (cursor == null)
                    return;

                StorageTransaction transaction = new StorageTransaction(getActivity().getApplicationContext());
                MaintenanceReminderData reminderData = transaction.getMaintenanceReminder(cursor);
                if (reminderData == null)
                    return;
                // Edit Alert
                android.support.v4.app.FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);

                CreateMaintenanceReminderFragment fragment = new CreateMaintenanceReminderFragment();


                // pass arguments with selected index and edit mode
                Bundle editAlertArguments = new Bundle();
                // edit_mode is true
                editAlertArguments.putBoolean("edit_mode", true);
                // selected index
                //int lIndex = (Integer) view.getTag();
                editAlertArguments.putParcelable("selected_data", reminderData);

                fragment.setArguments(editAlertArguments);
                // launch fragment.
                ft.addToBackStack("EditMaintenanceAlert");
                ft.replace(mContainerId, fragment, "EditMaintenanceAlert");
                ft.commit();
            }
        });


        imgBtnAddLog = (ImageView)rootView.findViewById(R.id.btn_create_log);

        // add alert Button click listener
        imgBtnAddRemainder = (ImageView)rootView.findViewById(R.id.btn_create_alert);
        imgBtnAddRemainder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG,"$$$ onClicked remainder button...");
                if(!imgBtnAddRemainder.isEnabled()){
                    Log.d(TAG,"$$$ remainder button not enabled ");
                    return;
                }
                imgBtnAddLog.setEnabled(false);
                Log.d(TAG,"$$$ log button disabled");
                // add geofence fragment.
                android.support.v4.app.FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);

                CreateMaintenanceReminderFragment fragment = new CreateMaintenanceReminderFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PRE_EXISTS_SERVICE_TYPE, lstDisableServiceTypes);
                fragment.setArguments(bundle);

                ft.addToBackStack("CreateMaintenanceAlert");
                ft.replace(mContainerId, fragment, "CreateMaintenanceAlert");
                ft.commit();
            }
        });

        // add alert log button click listener
        imgBtnAddLog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mLogsRefreshLayout.isRefreshing())
                    return;

                if(!imgBtnAddLog.isEnabled()){
                    Log.d(TAG,"$$$ log button not enabled ");
                    return;
                }

                Log.d(TAG,"$$$ onClick Log screen");
                imgBtnAddRemainder.setEnabled(false);
                Log.d(TAG,"remainder button disabled");

                // add geofence fragment.
                android.support.v4.app.FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
                CreateMaintenanceLogFragment fragment = new CreateMaintenanceLogFragment();
                ft.addToBackStack("CreateMaintenanceLog");
                ft.replace(mContainerId, fragment,"CreateMaintenanceLog");
                ft.commit();
            }
        });

        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2]);

        mAlertsRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.maintenance_alerts_list_swipe_refresh);
        mAlertsRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh() maintenance reminders ");
                // once job done setRefresh to false.
                mAlertsRefreshLayout.setRefreshing(true);
                // perform controller refresh operation
                DiagnosticController.instance().getMaintenanceLogs(getActivity());

            }
        });
        //mAlertsRefreshLayout.setColorSchemeResources(R.color.red, R.color.sc_blueDark, R.color.goldenrod_yellow, R.color.sc_financialGreen);

        mLogsRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.maintenance_alert_log_list_swipe_refresh);
        mLogsRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh() maintenance logs  ");
                // once job done setRefresh to false.
                mLogsRefreshLayout.setRefreshing(true);
                // perform controller refresh operation
                DiagnosticController.instance().getMaintenanceLogs(getActivity());

            }
        });
        //mLogsRefreshLayout.setColorSchemeResources(R.color.red, R.color.sc_blueDark, R.color.goldenrod_yellow, R.color.sc_financialGreen);
        // by default in initial state views will be in refreshing state.
        if (mAlertsRefreshLayout != null)
            mAlertsRefreshLayout.setRefreshing(true);
        if (mLogsRefreshLayout != null)
            mLogsRefreshLayout.setRefreshing(true);

        setupCallbacks();


        // work around to smooth the animation.
        // But code need to be removed
        // Actual fix : perform on animation end.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                DiagnosticController.instance().getMaintenanceLogs(getActivity());

            }
        }, 1000);
        // work around end.

    }


    private void setListClickListener(){
        maintenanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int lIndex = (Integer) view.getTag();
                showEditMode(lIndex);
            }
        });
        maintenanceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showConfirmation(position, "log");
                return true;
            }
        });
        mMaintenanceAlertList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showConfirmation(position,"reminder");
                return true;
            }
        });


    }

    private void showConfirmation(final int position, final String type){
        if(type.equals("log")) {

            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.maintenance_log))
                    .setMessage(getResources().getString(R.string.alert_delete))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            removeLogEntry(position);

                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
        else{
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.maintenance_reminders)) //maintenance_reminders
                    .setMessage(getResources().getString(R.string.alert_delete))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            deleteReminderEntry(position);
                            addButtonVisibility();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    private void addButtonVisibility(){
        StorageTransaction transaction = new StorageTransaction(getActivity().getApplicationContext());
        boolean showAddButton = makeMaintenanceAddVisibility(transaction);
        if(!showAddButton){
            imgBtnAddRemainder.setVisibility(View.INVISIBLE);
        }else{
            imgBtnAddRemainder.setVisibility(View.VISIBLE);
        }
//        lstDisableServiceTypes = new ArrayList<Integer>();
    }

    private void setClickListeners(){
        mLogInfoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerms("LOGS");
            }
        });
        mAlertInfoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerms("REMINDERS");
            }
        });
    }

    private void showTerms(String title){
        DBUtils.pullDbFromLocalStorageToSDCard();
        ToBeDecidedDialog dialog = new ToBeDecidedDialog(getActivity());
        if (title.equals("LOGS"))
        dialog.setTitle(getResources().getString(R.string.about_logs));
        else if (title.equals("REMINDERS"))
            dialog.setTitle(getResources().getString(R.string.about_reminders));
        dialog.show();
    }

    // this should not be position it should be id.
    private void removeLogEntry(int position){

        Cursor cursor = (Cursor) mCursorAdapter.getItem(position);

        if (cursor == null) {
            return;
        }
        ArrayList<String> serviceIds = new ArrayList<String>();
        serviceIds.add(cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MAINTENANCE_ID_ON_SERVER)));
        DiagnosticController.instance().deleteMaintenanceLogs(getActivity(), serviceIds);

    }

    private void deleteReminderEntry(int position){
        Log.d(TAG,"$$$ deleteReminderEntry "+position);
        /*StorageTransaction transaction = new StorageTransaction(getActivity().getApplicationContext());
        transaction.removeRowForIdAndColumn(position,MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER, MaintenanceReminderTable.COLUMN_MAINTENANCE_Id);
        mAlertsCursorAdapter.changeCursor(new StorageTransaction(getActivity()).getAllData(MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER, new boolean[]{false,true}, MaintenanceReminderTable.COLUMN_REMINDER_DATE, MaintenanceReminderTable.COLUMN_REMINDER_NAME));
*/
//      CHECK WITH THE SERVICES
        Cursor cursor = (Cursor) mAlertsCursorAdapter.getItem(position);

        if (cursor == null) {
            return;
        }
        Log.d(TAG, " delete reminder id "+cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_ID)));
        ArrayList<String> serviceIds = new ArrayList<String>();
        serviceIds.add(cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_ID)));
        DiagnosticController.instance().deleteMaintenanceReminders(getActivity(), serviceIds);
    }


    private void showEditMode(int aIndex) {
        // not allowed to launch when status is in pending.
        if (mLogsRefreshLayout.isRefreshing())
            return;
        android.support.v4.app.FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        CreateMaintenanceLogFragment fragment = new CreateMaintenanceLogFragment();

        // pass arguments with selected index and edit mode
        Bundle editAlertArguments = new Bundle();
        // edit_mode is true
        editAlertArguments.putBoolean("edit_mode", true);
        // selected index
        editAlertArguments.putInt("selected_index", aIndex);
        //editAlertArguments.putString("title", "edit");
        fragment.setArguments(editAlertArguments);
        // launch fragment.
        ft.addToBackStack("EditMaintenanceLog");
        ft.replace(mContainerId, fragment,"EditMaintenanceLog");
        ft.commit();
    }

    private void  populateMaintenanceLogs(){


        new Handler().post(new Runnable() {
            @Override
            public void run() {


                StorageTransaction transaction = new StorageTransaction(getActivity().getApplicationContext());
                mCursorAdapter = new MaintenanceLogCursorAdapter(getActivity().getApplicationContext(),transaction.getAllData( MaintenanceLogTable.TABLE_NAME_MAINTENANCE,new boolean[]{false,true},MaintenanceLogTable.COLUMN_DATE,MaintenanceLogTable.COLUMN_ALERT_NAME),false);
                maintenanceListView.setAdapter(mCursorAdapter);
                mCursorAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        if( mCursorAdapter.getCount() == 0){
                            empty_Log_TV.setVisibility(TextView.VISIBLE);
                        }else{
                            empty_Log_TV.setVisibility(TextView.INVISIBLE);
                        }
                    }
                });

                mAlertsCursorAdapter = new MaintenanceReminderCursorAdapter(getActivity().getApplicationContext(),transaction.getAllData(MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER,new boolean[]{false,true},MaintenanceReminderTable.COLUMN_REMINDER_DATE,MaintenanceReminderTable.COLUMN_SERVICE_NAME),false);
                mMaintenanceAlertList.setAdapter(mAlertsCursorAdapter);
                boolean showAddButton = makeMaintenanceAddVisibility(transaction);
                if(!showAddButton){
                    imgBtnAddRemainder.setVisibility(View.INVISIBLE);
                }
                mAlertsCursorAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                       if( mAlertsCursorAdapter.getCount() == 0){
                           empty_Alert_TV.setVisibility(TextView.VISIBLE);
                       }else{
                           empty_Alert_TV.setVisibility(TextView.INVISIBLE);
                       }
                       if (mAlertsCursorAdapter.getCount() == 3){
                            imgBtnAddRemainder.setVisibility(View.GONE);
                        }
                        else{
                            imgBtnAddRemainder.setVisibility(View.VISIBLE);
                        }

                    }
                });

                // give a start call.
                mCursorAdapter.notifyDataSetChanged();
                mAlertsCursorAdapter.notifyDataSetChanged();

            }
        });
    }

    private boolean makeMaintenanceAddVisibility(StorageTransaction transaction){
        lstDisableServiceTypes = new ArrayList<Integer>();

        ArrayList<MaintenanceReminderData> lstAlerts = transaction.getAllMaintenanceReminders();
        boolean retFlag = true;
        String[] predefinedServiceTypes = getActivity().getResources().getStringArray(R.array.maintenance_service_types);
        int alreadyExist = 0;
        if(lstAlerts != null) {
            for (int i = 0; i < predefinedServiceTypes.length; i++) {
                String predefinedServiceType = predefinedServiceTypes[i];

                for (MaintenanceReminderData maintenanceReminderData : lstAlerts) {
                    String serviceType = maintenanceReminderData.getServiceType();

                    if (serviceType.trim().equalsIgnoreCase(predefinedServiceType)) {

                        lstDisableServiceTypes.add(i);
                        alreadyExist++;
                        break;
                    }
                }
            }
            if (alreadyExist == 3) {
                retFlag = false;
            }
        }

        return retFlag;
    }


    @Override
    public void onProgress(Operation opr) {
        //mLogsRefreshLayout.setRefreshing(true);

    }

    @Override


    public void onError(Operation opr) {
        if (opr.getId() ==  Operation.OperationCode.GET_MAINTENANCE_REMINDERS.ordinal()
            || opr.getId() == Operation.OperationCode.GET_MAINTENANCE_LOGS.ordinal()
            || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()
            || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()) {
            if (mAlertsRefreshLayout != null && mAlertsRefreshLayout.isRefreshing())
                mAlertsRefreshLayout.setRefreshing(false);
            if (mLogsRefreshLayout != null && mLogsRefreshLayout.isRefreshing())
                mLogsRefreshLayout.setRefreshing(false);
            String lMessage = getResources().getString(R.string.error_occurred);//"Error occurred  ";
            if (opr != null && opr.getInformation() != null) {
                lMessage += opr.getInformation();
            }
            if (getActivity() != null) {
                Toast.makeText(getActivity(),  lMessage, Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateMaintenanceLogs();
                    }
                });
            }
        }
    }

    @Override
    public void onSuccess(Operation opr) {
        if (mAlertsRefreshLayout != null && mAlertsRefreshLayout.isRefreshing())
            mAlertsRefreshLayout.setRefreshing(false);
        if (mLogsRefreshLayout != null && mLogsRefreshLayout.isRefreshing())
            mLogsRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanup();
    }

    @Override
    public void update(final Observable observable, final Object o) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(MaintenanceLogsModel.class.isInstance(observable)){
                    // we have got new logs.
                    StorageTransaction transaction = new StorageTransaction(getActivity());
                    transaction.deleteAllDataForTable(MaintenanceLogTable.TABLE_NAME_MAINTENANCE);
                    if (o != null) {
                        List<MaintenanceLogData> maintenanceLogList = (List<MaintenanceLogData>) o;
                        transaction.insertBulkMaintenanceLog(getActivity(), maintenanceLogList);
                    }

                }
                if(MaintenanceRemindersModel.class.isInstance(observable)){
                    // we have got new logs.
                    StorageTransaction transaction = new StorageTransaction(getActivity());
                    transaction.deleteAllDataForTable(MaintenanceReminderTable.TABLE_NAME_MAINTENANCE_REMINDER);
                    if (o != null) {
                        List<MaintenanceReminderData> reminders = (List<MaintenanceReminderData>) o;
                        transaction.insertBulkMaintenanceReminders(reminders);
                    }
                }
                populateMaintenanceLogs();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getActivity() == null)
            return;

        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null) {
            return;
        }

        View newView = null;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newView = inflater.inflate(R.layout.fragment_maintenancelog_alert_land, null);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            newView = inflater.inflate(R.layout.fragment_maintenancelog_alert, null);
        }

        if (newView == null)
            return;

        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        // Temp fix
        // It supposed to be in onPause or onDestroyView

        rootView.addView(newView);
        cleanup();
        // setup
        init(rootView);
    }

    private void setupCallbacks() {
        DiagnosticController.instance().register(this);
        DiagnosticController.instance().getMaintenanceLogsModel().addObserver(this);
        DiagnosticController.instance().getMaintenanceRemindersModel().addObserver(this);
    }

    private void cleanup() {
        DiagnosticController.instance().unregister(this);
        DiagnosticController.instance().getMaintenanceLogsModel().deleteObserver(this);
        DiagnosticController.instance().getMaintenanceRemindersModel().deleteObserver(this);
    }

}
