package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.adapters.AlertDetailHistoryAdapter;
import com.verizontelematics.indrivemobile.models.AlertHistory;

import java.util.ArrayList;

/**
 * Created by Priyanga on 8/25/2014.
 */
@Deprecated
public class AlertHistoryFragment extends BaseSubUIFragment {
    private String mFragmentLabels[] = {
            "DiagnosticsAlertHistory",
            "ValetAlertHistory",
            "SpeedAlertHistory","BoundaryAlertHistory"

    };
    private SwipeRefreshLayout mRefreshLayout;

    public AlertHistoryFragment() {
//        Empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert_history, container, false);
        ListView alertDetailListView = (ListView) rootView.findViewById(R.id.alertHistoryLV);
        AlertDetailHistoryAdapter adapter = new AlertDetailHistoryAdapter(getActivity(),
                R.layout.alert_history_detail_lv_item, populateData());
        alertDetailListView.setAdapter(adapter);
        alertDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                BaseSubUIFragment fragment = null;
                switch (position) {

                    case 0:
                        fragment = new DiagnosticsHistoryFragment();
                        break;

                    case 1:
                        fragment = new ValetHistoryFragment();
                        break;

                    case 2:
                        fragment = new SpeedHistoryFragment();
                        break;

                    case 3:
                        fragment = new BoundaryHistoryFragment();
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

        mRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh_layout_id);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private ArrayList<AlertHistory> populateData() {

        ArrayList<AlertHistory> alertArrayList = new ArrayList<AlertHistory>();


        AlertHistory alertHistoryObj = new AlertHistory("Diagnostic Alerts", "", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Valet Alerts", "", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Speed Alerts", "", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Location Alerts", "", "");

        alertArrayList.add(alertHistoryObj);

        return alertArrayList;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.alerts_array)[7]);
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
        ((HomeActivity)ctx).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.alerts_option_list_array)[1],
                true, false, false,false);
        // Hide navigation toggle button.
    }

    private void cleanup() {
        // reset title
        // show tab bar
        ((HomeActivity)getActivity()).showSlidingMenus();
        ((HomeActivity)getActivity()).showActionToggleButton();
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