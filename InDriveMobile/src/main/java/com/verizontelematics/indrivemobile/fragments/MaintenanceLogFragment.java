package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.CreateMaintenanceLogActivity;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.activity.MaintenanceLogDetailActivity;
import com.verizontelematics.indrivemobile.adapters.cursoradapters.MaintenanceLogCursorAdapter;
import com.verizontelematics.indrivemobile.adapters.cursoradapters.MaintenanceReminderCursorAdapter;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceLogTable;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceReminderTable;
import com.verizontelematics.indrivemobile.models.MaintenanceLogsModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.MaintenanceLogData;
import com.verizontelematics.indrivemobile.userprofile.UserFactory;
import com.verizontelematics.indrivemobile.userprofile.UserRoleConstants;
import com.verizontelematics.indrivemobile.userprofile.utils.UserUtils;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;
import com.verizontelematics.indrivemobile.utils.ui.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;



/**
 * Created by z681639 on 8/28/2014.
 */

public class MaintenanceLogFragment extends BaseSubUIFragment implements UIInterface, Observer, HomeActivity.CustomTopBarItemsClickListener,UserRoleConstants {

    private ListView maintenanceListView;
    private String TAG = MaintenanceInformationFragment.class.getCanonicalName();
    private int mContainerId = R.id.container_id_diagnostics;
    public static MaintenanceLogCursorAdapter mCursorAdapter;
    private static MaintenanceReminderCursorAdapter mAlertsCursorAdapter;
    private ImageView mLogInfoIV;
    public static ImageView imgBtnAddRemainder;
    private static ImageView imgBtnAddLog;
    private TextView empty_Log_TV;
    private int alerts_Count;
    private  String lMessage;
    private SwipeRefreshLayout mLogsRefreshLayout;

    public static final String PRE_EXISTS_SERVICE_TYPE ="PRE_EXISTS_SERVICE_TYPE";



    ArrayList<Integer> lstDisableServiceTypes;
    private SwipeRefreshLayout mAlertsRefreshLayout;


    public MaintenanceLogFragment(){
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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2]);

        //Getting the list item counts to show the visibility accordingly


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
        /*if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootView = inflater.inflate(R.layout.fragment_maintenancelog_alert_land, container, false);
        } else {*/
            rootView = inflater.inflate(R.layout.fragment_maintenance_logs, container, false);
        //}
        init(rootView);
        setListenersForSaveCancel();
        // Hack to not propagate touch events to fragment.
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_maintenance_log));
        return rootView;

    }

    private void init(View rootView) {

        TextView empty_Alert_TV = (TextView) rootView.findViewById(R.id.emptyAlertTV);
        empty_Log_TV=(TextView)rootView.findViewById(R.id.emptyLogTV);

        lMessage = getResources().getString(R.string.error_occurred);


        if (getArguments() != null) {
            mContainerId = getArguments().getInt("container", R.id.container_id_diagnostics);
        }



        // Maintenance Alert Logs list
        maintenanceListView = (ListView)rootView.findViewById(R.id.maintenanceLogLV);
        mLogInfoIV = (ImageView)rootView.findViewById(R.id.btn_info_maintenance_log);
        ImageView mAlertInfoIV = (ImageView) rootView.findViewById(R.id.btn_info_maintenance_alert);
        populateMaintenanceLogs();
        ListView mMaintenanceAlertList = (ListView) rootView.findViewById(R.id.maintenance_alerts_list);
        setListClickListener();
        setClickListeners();




        imgBtnAddLog = (ImageView)rootView.findViewById(R.id.btn_create_log);



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

                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                        "DiagnosticsAddLog");

                // add geofence fragment.
                /*android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
                CreateMaintenanceLogFragment fragment = new CreateMaintenanceLogFragment();
                fragment.setHomeFragment(getHomeFragment());
                ft.addToBackStack("CreateMaintenanceLog");
                ft.replace(mContainerId,fragment,"CreateMaintenanceLog");
                ft.commit();
                getHomeFragment().pushFragmentStack("CreateMaintenanceLog");*/

                if(UserUtils.isUserInactive(getActivity(),UserRoleConstants.inactiveMessage)){
                    return;
                }

                launchActivity(CreateMaintenanceLogActivity.class,maintenanceLogsAdd);
//                Intent createLogIntent = new Intent(getActivity(), CreateMaintenanceLogActivity.class);
//                startActivity(createLogIntent);

            }
        });

        TextView txtVwMLog = (TextView)rootView.findViewById(R.id.maintenanceLogTV);
        txtVwMLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLogsRefreshLayout.isRefreshing())
                    return;

                if(!imgBtnAddLog.isEnabled()){
                    Log.d(TAG,"$$$ log button not enabled ");
                    return;
                }
                launchActivity(CreateMaintenanceLogActivity.class,maintenanceLogsAdd);
            }
        });

        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[1]);



        mLogsRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.maintenance_alert_log_list_swipe_refresh);
        mLogsRefreshLayout.setColorSchemeResources(R.color.diagnostics_code, R.color.sub_header_color, R.color.diagnostics_code, R.color.sub_header_color);
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

//        if (mLogsRefreshLayout != null)
//            mLogsRefreshLayout.setRefreshing(true);

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

    private void launchActivity(Class activity,String subModule){
//        activity = UserFactory.getInstance(getActivity()).getActivityView(activity,subModule);
//        if(UserUtils.isUserInactive(getActivity(), inactiveMessage)){
//            return;
//        }
        Intent intent = UserFactory.getInstance(getActivity()).getActivityViewIntent(getActivity(),
                activity,subModule);//new Intent(getActivity(),activity);
        startActivity(intent);
    }

    private void launchActivity(Class activity,String subModule,MaintenanceLogData logData){
//        activity = UserFactory.getInstance(getActivity()).getActivityView(activity,subModule);
//        if(UserUtils.isUserInactive(getActivity(), inactiveMessage)){
//            return;
//        }
        Intent intent = UserFactory.getInstance(getActivity()).getActivityViewIntent(getActivity(),
                activity,subModule);//new Intent(getActivity(),activity);
        intent.putExtra("selected_data",logData);
        startActivity(intent);
    }




    private void setListClickListener(){
        maintenanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mCursorAdapter == null)
                    return;

                if(UserUtils.isUserInactive(getActivity(),UserRoleConstants.inactiveMessage)){
                    return;
                }

                // get data from selected index
                Cursor cursor =(Cursor) mCursorAdapter.getItem(position);
                if (cursor == null)
                    return;
                StorageTransaction transaction = new StorageTransaction(getActivity().getApplicationContext());
                MaintenanceLogData logData = transaction.getMaintenanceLog(cursor);
                if (logData == null)
                    return;

                // prepare parcel with selected log
                /*Bundle logDetailedArguments = new Bundle();
                logDetailedArguments.putParcelable("selected_data", logData);

                // launch detailed view of log
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
                MaintenanceLogDetailedFragment fragment = new MaintenanceLogDetailedFragment();
                fragment.setHomeFragment(getHomeFragment());
                // pass arguments with selected index and edit mode
                fragment.setArguments(logDetailedArguments);
                // launch fragment.
                ft.addToBackStack("MaintenanceLogDetailed");
                ft.replace(mContainerId, fragment, "MaintenanceLogDetailed");
                ft.commit();
                getHomeFragment().pushFragmentStack("MaintenanceLogDetailed");*/

                launchActivity(MaintenanceLogDetailActivity.class,"maintenanceLogsViewEditButton",logData);
//                Intent detailIntent = new Intent(getActivity(), MaintenanceLogDetailActivity.class);
//                detailIntent.putExtra("selected_data",logData);
//                startActivity(detailIntent);



            }
        });
        maintenanceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserUtils.isUserInactive(getActivity(),inactiveMessage)){
                    return true;
                }
                showConfirmation(position, "log");
                return true;
            }
        });
    }

    private void showConfirmation(final int position, final String type){
        if(type.equals("log")) {

            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.delete_maintenance_log_header))
                    .setMessage(getResources().getString(R.string.alert_delete))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            removeLogEntry(position);

                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }



    private void setClickListeners(){
        mLogInfoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerms("LOGS");
            }
        });

    }

    private void showTerms(String title){
//        DBUtils.pullDbFromLocalStorageToSDCard();
        ToBeDecidedDialog dialog = new ToBeDecidedDialog(getActivity(),getActivity().getResources().getString(R.string.info_logs));
        if (title.equals("LOGS"))
            dialog.setTitle(getResources().getString(R.string.about_maintenance_log));

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
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        CreateMaintenanceLogFragment fragment = new CreateMaintenanceLogFragment();
        fragment.setHomeFragment(getHomeFragment());
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
        getHomeFragment().pushFragmentStack("EditMaintenanceLog");
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

                // give a start call.
                mCursorAdapter.notifyDataSetChanged();
            }
        });
    }




    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_MAINTENANCE_LOGS.ordinal()
                || opr.getId() ==  Operation.OperationCode.GET_MAINTENANCE_REMINDERS.ordinal()
                || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
                || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()) {
            if (mLogsRefreshLayout != null && !mLogsRefreshLayout.isRefreshing())
                mLogsRefreshLayout.setRefreshing(true);
        }

    }

    @Override


    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_MAINTENANCE_LOGS.ordinal()
            || opr.getId() ==  Operation.OperationCode.GET_MAINTENANCE_REMINDERS.ordinal()
            || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()
            || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
            ) {
//            if (mAlertsRefreshLayout != null && mAlertsRefreshLayout.isRefreshing())
//                mAlertsRefreshLayout.setRefreshing(false);
            if (mLogsRefreshLayout != null && mLogsRefreshLayout.isRefreshing())
                mLogsRefreshLayout.setRefreshing(false);
//            lMessage = getResources().getString(R.string.error_occurred); //"Error occurred  ";
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
        /*if (mAlertsRefreshLayout != null && mAlertsRefreshLayout.isRefreshing())
            mAlertsRefreshLayout.setRefreshing(false);*/
        if (opr.getId() == Operation.OperationCode.GET_MAINTENANCE_LOGS.ordinal()
                || opr.getId() ==  Operation.OperationCode.GET_MAINTENANCE_REMINDERS.ordinal()
                || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS.ordinal()
                || opr.getId() == Operation.OperationCode.DELETE_MAINTENANCE_LOG.ordinal()) {
            if (mLogsRefreshLayout != null && mLogsRefreshLayout.isRefreshing())
                mLogsRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanup();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cleanup();
        ((HomeActivity)getActivity()).hideCustomActionBar("Diagnostics");
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

                populateMaintenanceLogs();
            }
        });
    }

    /*@Override
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
    }*/

    private void setupCallbacks() {
        DiagnosticController.instance().register(this);
        DiagnosticController.instance().getMaintenanceLogsModel().addObserver(this);
    }

    private void cleanup() {
        DiagnosticController.instance().unregister(this);
        DiagnosticController.instance().getMaintenanceLogsModel().deleteObserver(this);
    }
    @Override
    public void onTopBarItemClick(View aView) {
        if (aView.getId() == HomeActivity.LEFT_ARROW_ID) {
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
//            DiagnosticsFragment diagnosticsHome = new DiagnosticsFragment();
//            transaction.replace(R.id.mainLogsLL, diagnosticsHome);
//            transaction.commit();
            getHomeFragment().popFragmentStack();
            ((HomeActivity)getActivity()).removeFromDoneButtonHandlers(this);
            ((HomeActivity)getActivity()).removeFromLeftArrowHandlers(this);
            //((HomeActivity)getActivity()).removeFromCancelHandlers(this);

            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.create_log_entry), false,true,true,false);
        }
    }

    private void setListenersForSaveCancel() {
        ((HomeActivity) getActivity()).addToDoneButtonClickHandlers(this);
        ((HomeActivity) getActivity()).addToLeftArrowClickHandlers(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2],
                true, false, false,false);

    }

}
