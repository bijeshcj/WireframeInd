package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.adapters.AlertSettingsAdapter;
import com.verizontelematics.indrivemobile.controllers.AlertsController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.models.AlertSettings;
import com.verizontelematics.indrivemobile.models.AlertsDataModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.Alert;
import com.verizontelematics.indrivemobile.models.data.DiagnosticAlert;
import com.verizontelematics.indrivemobile.models.data.LocationAlert;
import com.verizontelematics.indrivemobile.models.data.SpeedAlert;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by bijesh on 11/18/2014.
 * This class has been deprecated
 */
@Deprecated
public class AlertSettingsFragment extends BaseSubUIFragment implements Observer,UIInterface {
    private static final String TAG = AlertSettingsFragment.class.getSimpleName();
    private ListView alertSettingsLV;
    private AlertSettingsAdapter alertSettingsAdapter;
    private String mFragmentLabels[] = {
            "SpeedAlert"
            , "ValetAlert"
            , "DiagnosticsAlert"
            , "RecallInfo"
    };
    private SwipeRefreshLayout mRefreshLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert_settings, container, false);
        alertSettingsLV = (ListView)rootView.findViewById(R.id.settingsLV);
        alertSettingsAdapter = new AlertSettingsAdapter(getActivity(),R.layout.alert_settings_detail_lv_item, populateDataSettings(null));
        alertSettingsLV.setAdapter(alertSettingsAdapter);
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout_id);
        setupListeners();
        updateData();
        return rootView;
    }

    private void updateData(){
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(true);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AlertsController.instance().getAlertsData(getActivity());
            }
        },700);
    }

    private void setupListeners() {

        AlertsController.instance().getAlertsDataModel().addObserver(this);
        AlertsController.instance().register(this);

        alertSettingsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                BaseSubUIFragment fragment = null;
                switch (position) {

                    case 0:
                        fragment = new SpeedAlertFragment();
                        break;

                    case 1:
                        fragment = new AlertValetFragment();
                        break;

                    case 2:
                        fragment = new DiagnosticsAlertFragment();
                        break;

                    case 3:
                        fragment = new BoundaryAlertFragment();
                        break;


                    default:
                        break;
                }
                if (fragment != null) {
                    fragment.setHomeFragment(getHomeFragment());
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
                    transaction.addToBackStack(mFragmentLabels[position]);
                    transaction.add(R.id.container_id, fragment, mFragmentLabels[position]);
                    transaction.commit();
                    //mTitle = getActivity().getResources().getStringArray(R.array.alerts_option_list_array)[position];
                    getHomeFragment().pushFragmentStack(mFragmentLabels[position]);
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

    private ArrayList<AlertSettings> populateDataSettings(Alert alert) {

        ArrayList<AlertSettings> alertArrayList = new ArrayList<AlertSettings>();
        List<SpeedAlert> lstSpeedAlerts = null;

        if (alert != null)
            lstSpeedAlerts= alert.getSpeedAlert();

        AlertSettings obj= new AlertSettings("Speed Alerts", "",false);
        if (lstSpeedAlerts != null && !lstSpeedAlerts.isEmpty()) {
            SpeedAlert speedAlert = lstSpeedAlerts.get(0);
            obj.setAlertDetail(speedAlert.getAlertName());
            obj.setAlertToggle(speedAlert.getStatus().equalsIgnoreCase("Active"));
        }
        alertArrayList.add(obj);

        obj= new AlertSettings("Valet Alerts", "",false);
        // get the status of valet alert and update the status
        alertArrayList.add(obj);

        obj= new AlertSettings("Diagnostic Alerts", "",false);
        List<DiagnosticAlert> listDiagnosticsAlert = null;
        if (alert != null)
            listDiagnosticsAlert = alert.getDiagnosticAlert();
        if (listDiagnosticsAlert != null && !listDiagnosticsAlert.isEmpty()) {
            DiagnosticAlert diagnosticAlert = listDiagnosticsAlert.get(0);
            obj.setAlertDetail(diagnosticAlert.getAlertName());
            obj.setAlertToggle(diagnosticAlert.getStatus().equalsIgnoreCase("Active"));
        }
        alertArrayList.add(obj);

        obj= new AlertSettings("Boundary Alerts", "",false);
        List<LocationAlert> lstBoundaryAlerts = null;
        if (alert != null)
            lstBoundaryAlerts = alert.getLocationAlerts();
        if (lstBoundaryAlerts != null && !lstBoundaryAlerts.isEmpty()) {
            boolean status = true;
            // Put the logic for getting compunding all boundary alerts status into one.
            for (LocationAlert locationAlert : lstBoundaryAlerts) {
                status = status && locationAlert.getStatus().equalsIgnoreCase("Active");
            }
            // update the status
            obj.setAlertToggle(status);
        }
        alertArrayList.add(obj);
        return alertArrayList;
    }

    @Override
    public void update(final Observable observable,  final Object data) {
        Log.d(TAG, "update received on UI ");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                  update UI
                if (AlertsDataModel.class.isInstance(observable)) {
                    Alert alert = (Alert)data;
                    // populate UI
                    populateUI(alert);
                    // Store in database
                }
            }
        });
    }

    private void populateUI(Alert alert) {
        // update adapter
        alertSettingsAdapter.clear();

        List<AlertSettings> listAlertSettings = populateDataSettings(alert);
        if (listAlertSettings != null && !listAlertSettings.isEmpty()) {
            for (AlertSettings alertSettings : listAlertSettings)
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
            if (getActivity() != null) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_server_alert_settings), Toast.LENGTH_SHORT).show();
            }
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
    public void onAttach(Activity ctx) {
        super.onAttach(ctx);
        setup(ctx);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cleanup();
    }

    private void setup(Activity ctx) {
        // hide the tab bar.
        ((HomeActivity)ctx).hideSlidingMenus();
        // setup title
        ((HomeActivity)ctx).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.alerts_option_list_array)[0],
                true, false, false,false);
        // Hide navigation toggle button.
    }

    private void cleanup() {
        // reset title
        // show tab bar
        ((HomeActivity)getActivity()).showSlidingMenus();
        ((HomeActivity)getActivity()).showActionToggleButton();
        // de-register listeners.
        AlertsController.instance().unregister(this);
        AlertsController.instance().getAlertsDataModel().deleteObserver(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanup();
    }
}
