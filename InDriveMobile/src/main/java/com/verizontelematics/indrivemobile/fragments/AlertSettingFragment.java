package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.AlertBoundaryActivity;
import com.verizontelematics.indrivemobile.activity.AlertValetActivity;
import com.verizontelematics.indrivemobile.activity.AlertsDiagnosticsActivity;
import com.verizontelematics.indrivemobile.activity.SpeedAlertActivity;
import com.verizontelematics.indrivemobile.adapters.AlertSettingsAdapter;
import com.verizontelematics.indrivemobile.controllers.AlertsController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.DiagnosticAlertTable;
import com.verizontelematics.indrivemobile.database.tables.LocationAlertTable;
import com.verizontelematics.indrivemobile.database.tables.SpeedAlertTable;
import com.verizontelematics.indrivemobile.database.tables.ValetAlertTable;
import com.verizontelematics.indrivemobile.models.AlertSettings;
import com.verizontelematics.indrivemobile.models.AlertsDataModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.Alert;
import com.verizontelematics.indrivemobile.models.data.DiagnosticAlert;
import com.verizontelematics.indrivemobile.models.data.LocationAlert;
import com.verizontelematics.indrivemobile.models.data.SpeedAlert;
import com.verizontelematics.indrivemobile.models.data.ValetAlert;

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
 * Created by bijesh on 1/7/2015.
 */
public class AlertSettingFragment extends Fragment implements Observer, UIInterface,UserRoleConstants {

    private static final String TAG = AlertSettingFragment.class.getCanonicalName();

    private ListView alertSettingsLV;
    private AlertSettingsAdapter alertSettingsAdapter;
    private SwipeRefreshLayout mRefreshLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert_settings, container, false);
        alertSettingsLV = (ListView) rootView.findViewById(R.id.settingsLV);
        alertSettingsAdapter = new AlertSettingsAdapter(getActivity(), R.layout.alert_settings_detail_lv_item, populateDataSettings(null));
        alertSettingsLV.setAdapter(alertSettingsAdapter);
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout_id);
        mRefreshLayout.setColorSchemeResources(R.color.sub_header_color, R.color.alert_module_swipe_refresh, R.color.sub_header_color, R.color.alert_module_swipe_refresh);
        setupListeners();
        updateData();
        return rootView;
    }

    private void updateData() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(true);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AlertsController.instance().getAlertsData(getActivity());
            }
        }, 700);
    }

    private void setupListeners() {

        AlertsController.instance().getAlertsDataModel().addObserver(this);
        AlertsController.instance().register(this);

        alertSettingsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                BaseSubUIFragment fragment = null;
                switch (position) {

                    case 0:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_alerts),
                                "AlertSettingsDiagnosticAlerts");
                        launchActivity(AlertsDiagnosticsActivity.class,"diagnosticsAlert");
//                        fragment = new SpeedAlertFragment();
                        break;
                    case 1:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_alerts),
                                "AlertSettingsSpeedAlerts");
                        launchActivity(SpeedAlertActivity.class,"speedAlert");

//                        fragment = new AlertValetFragment();
                        break;
                    case 2:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_alerts),
                                "AlertSettingsValetAlerts");
                        launchActivity(AlertValetActivity.class,"valetAlert");
//                        fragment = new DiagnosticsAlertFragment();
                        break;

                    case 3:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_alerts),
                                "AlertSettingsLocationAlerts");
                        launchActivity(AlertBoundaryActivity.class,"locationAlert");
//                        fragment = new BoundaryAlertFragment();
                        break;


                    default:
                        break;
                }

            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AlertsController.instance().getAlertsData(getActivity());
            }
        });
    }

    private void launchActivity(Class<? extends Activity> activityTypeClass) {
        Intent intent = new Intent(getActivity(), activityTypeClass);
        startActivity(intent);
    }


    private void launchActivity(Class activity,String subModule){
//        activity = UserFactory.getInstance(getActivity()).getActivityView(activity,subModule);
//        if(UserUtils.isUserInactive(getActivity(), inactiveMessage)){
//            return;
//        }
        Log.d(TAG,"$$$ launchActivity "+subModule);
        Intent intent = UserFactory.getInstance(getActivity()).getActivityViewIntent(getActivity(),
                activity,subModule);//new Intent(getActivity(),activity);
        startActivity(intent);
    }


    private ArrayList<AlertSettings> populateDataSettings(Alert alert) {

        ArrayList<AlertSettings> alertArrayList = new ArrayList<AlertSettings>();
        List<SpeedAlert> lstSpeedAlerts = null;

        AlertSettings obj = new AlertSettings("Diagnostic Alert", "", false);
        List<DiagnosticAlert> listDiagnosticAlerts = null;
        if (alert != null)
            listDiagnosticAlerts = alert.getDiagnosticAlert();
        if (listDiagnosticAlerts != null && !listDiagnosticAlerts.isEmpty()) {
            DiagnosticAlert diagnosticAlert = listDiagnosticAlerts.get(0);
            obj.setAlertDetail("Diagnostic Alert");
            obj.setAlertToggle(diagnosticAlert.getStatus().equalsIgnoreCase("Active"));
        }
        alertArrayList.add(obj);

        if (alert != null)
            lstSpeedAlerts = alert.getSpeedAlert();
        obj = new AlertSettings("Speed Alert", "", false);
        if (lstSpeedAlerts != null && !lstSpeedAlerts.isEmpty()) {
            SpeedAlert speedAlert = lstSpeedAlerts.get(0);
            obj.setAlertDetail("Speed Alert");
            obj.setAlertToggle(speedAlert.getStatus().equalsIgnoreCase("Active"));
        }
        alertArrayList.add(obj);

        obj = new AlertSettings("Valet Alert", "", false);
        // get the status of valet alert and update the status
        List<ValetAlert> lstValetAlerts = null;
        if (alert != null)
            lstValetAlerts = alert.getValetAlerts();
        if (lstValetAlerts != null && !lstValetAlerts.isEmpty()) {
            ValetAlert valetAlert = lstValetAlerts.get(0);
            obj.setAlertDetail("Valet Alert");
            obj.setAlertToggle(valetAlert.getStatus().equalsIgnoreCase("Active"));
        }
        alertArrayList.add(obj);


        obj = new AlertSettings("Location Alerts", "", false);
        List<LocationAlert> lstBoundaryAlerts = null;
        if (alert != null)
            lstBoundaryAlerts = alert.getLocationAlerts();
        if (lstBoundaryAlerts != null && !lstBoundaryAlerts.isEmpty()) {
            boolean status = false;
            // Put the logic for getting compunding all boundary alerts status into one.
            for (LocationAlert locationAlert : lstBoundaryAlerts) {
                status = locationAlert.getStatus().equalsIgnoreCase("Active");
                if (status)
                    break;
            }
            // update the status
            obj.setAlertToggle(status);
        }
        alertArrayList.add(obj);
        return alertArrayList;
    }


    @Override
    public void update(final Observable observable, final Object data) {
        Log.d(TAG, "update received on UI ");
        if (AlertsDataModel.class.isInstance(observable)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                  update UI
                    Alert alert = (Alert) data;
                    // populate UI
                    populateUI(alert);
                        // Store in database
                    persistOnSQLite(alert);
                }

            });
        }
    }

    private void persistOnSQLite(Alert alert){
        /// Crash found
        if (alert == null)
            return;

        StorageTransaction transaction = new StorageTransaction(getActivity());
// clearing the previous data of DiagnosticsAlert table and inserting the Diagnostics Alerts
        transaction.deleteAllDataForTable(DiagnosticAlertTable.TABLE_NAME_DIAGNOSTIC_ALERT);
        transaction.insertDiagnosticsAlert(alert.getDiagnosticAlert());

        // clearing the previous data of SpeedAlert table and inserting the Speed Alerts
        transaction.deleteAllDataForTable(SpeedAlertTable.TABLE_NAME_SPEED_ALERT);
        transaction.insertSpeedAlert(alert.getSpeedAlert());

        // clearing the previous data of ValetAlert table and inserting the Valet Alerts
        transaction.deleteAllDataForTable(ValetAlertTable.TABLE_NAME_VALET_ALERT);
        transaction.insertValetAlert(alert.getValetAlerts());

//        clearing the previous data of Location Alert table and inserting the Location alerts
        transaction.deleteAllDataForTable(LocationAlertTable.TABLE_NAME_LOCATION_ALERT);
        transaction.insertLocationAlerts(alert.getLocationAlerts());

        DBUtils.pullDbFromLocalStorageToSDCard();
    }

    private void setup(Activity ctx) {
        // hide the tab bar.
//        ((HomeActivity)ctx).hideSlidingMenus();
        // setup title
//        ((HomeActivity)ctx).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.alerts_option_list_array)[0],
//                true, false, false,false);
        // Hide navigation toggle button.
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setup(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cleanup();
    }

    private void populateUI(Alert alert) {
        // update adapter
        alertSettingsAdapter.clear();

        List<AlertSettings> lstAlertSettings = populateDataSettings(alert);
        if (lstAlertSettings != null && !lstAlertSettings.isEmpty()) {
            for (AlertSettings alertSettings : lstAlertSettings)
                alertSettingsAdapter.add(alertSettings);
        }
    }

    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_ALERTS.ordinal()) {
            if (mRefreshLayout != null && !mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(true);
            }
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_ALERTS.ordinal()) {
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            Toast lToast = Toast.makeText(getActivity(), getResources().getString(R.string.alert_settings_error), Toast.LENGTH_SHORT);
            lToast.show();
        }

    }



    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_ALERTS.ordinal()) {
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanup();
    }

    private void cleanup() {
        // de-register listeners.
        AlertsController.instance().unregister(this);
        AlertsController.instance().getAlertsDataModel().deleteObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanup();
    }

}
