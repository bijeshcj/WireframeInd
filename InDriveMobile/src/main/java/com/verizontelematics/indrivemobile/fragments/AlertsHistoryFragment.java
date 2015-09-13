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

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.BoundaryHistoryActivity;
import com.verizontelematics.indrivemobile.activity.DiagnosticsHistoryActivity;
import com.verizontelematics.indrivemobile.activity.SpeedHistoryActivity;
import com.verizontelematics.indrivemobile.activity.ValetHistoryActivity;
import com.verizontelematics.indrivemobile.adapters.AlertDetailHistoryAdapter;
import com.verizontelematics.indrivemobile.controllers.AlertsController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.models.AlertHistory;
import com.verizontelematics.indrivemobile.models.AlertsDataModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.Alert;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by bijesh on 1/7/2015.
 */
public class AlertsHistoryFragment extends Fragment implements Observer, UIInterface {
    private static final String TAG = AlertsHistoryFragment.class.getSimpleName();
    private ListView alertHistoryListView;
    private AlertDetailHistoryAdapter alertHistoryAdapter;
   /* private String mFragmentLabels[] = {
            "DiagnosticsAlertHistory",
            "ValetAlertHistory",
            "SpeedAlertHistory","BoundaryAlertHistory"

    };*/
    private SwipeRefreshLayout mRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alert_history, container, false);
        alertHistoryListView = (ListView) rootView.findViewById(R.id.alertHistoryLV);
        alertHistoryAdapter = new AlertDetailHistoryAdapter(getActivity(),
                R.layout.alert_history_detail_lv_item, populateData());
        alertHistoryListView.setAdapter(alertHistoryAdapter);
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

                AlertsController.instance().getAlertsHistoryData(getActivity());
            }
        }, 700);
    }

    private void setupListeners() {

        AlertsController.instance().getAlertsDataModel().addObserver(this);
        AlertsController.instance().register(this);

        alertHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                BaseSubUIFragment fragment = null;
                switch (position) {

                    case 0:
                        launchActivity(DiagnosticsHistoryActivity.class);
//                        fragment = new DiagnosticsHistoryFragment();
                        break;

                    case 1:
                        launchActivity(ValetHistoryActivity.class);
//                        fragment = new ValetHistoryFragment();
                        break;

                    case 2:
                        launchActivity(SpeedHistoryActivity.class);
//                        fragment = new SpeedHistoryFragment();
                        break;

                    case 3:
                        launchActivity(BoundaryHistoryActivity.class);
//                        fragment = new BoundaryHistoryFragment();
                        break;


                    default:
                        break;
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AlertsController.instance().getAlertsHistoryData(getActivity());
            }
        });
    }


    private void launchActivity(Class<? extends Activity> activityTypeClass){
        Intent intent = new Intent(getActivity(),activityTypeClass);
        startActivity(intent);
    }

    private ArrayList<AlertHistory> populateData() {

        ArrayList<AlertHistory> alertArrayList = new ArrayList<AlertHistory>();


        AlertHistory alertHistoryObj = new AlertHistory("Diagnostic Alert", "", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Valet Alert", "", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Speed Alert", "", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Location Alert", "", "");

        alertArrayList.add(alertHistoryObj);

        return alertArrayList;

    }

    @Override
    public void update(final Observable observable, final Object data) {

        Log.d(TAG, "update received on UI ");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                  update UI
                if (AlertsDataModel.class.isInstance(observable)) {
                    Alert alert = (Alert) data;
                    // populate UI
                    populateUI(alert);
                    // Store in database
                }
            }
        });

    }

    private void populateUI(Alert alert) {
        // update adapter
        alertHistoryAdapter.clear();

        //Populate UI
    }

    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_ALERTS_HISTORY.ordinal()) {
            if (mRefreshLayout != null && !mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(true);
            }
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_ALERTS_HISTORY.ordinal()) {
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.GET_ALERTS_HISTORY.ordinal()) {
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
