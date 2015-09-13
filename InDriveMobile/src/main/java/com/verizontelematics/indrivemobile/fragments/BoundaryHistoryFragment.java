package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.adapters.AlertHistoryAdapter;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.models.AlertHistory;

import java.util.ArrayList;

/**
 * Created by Priyanga on 12/31/2014.
 */
public class BoundaryHistoryFragment extends BaseSubUIFragment {

    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_boundary_history, container, false);
        ListView boundaryHistoryListView = (ListView) rootView.findViewById(R.id.boundaryHistoryListView);
        AlertHistoryAdapter adapter = new AlertHistoryAdapter(getActivity(), R.layout.boundary_history_lv_item, populateData());
        boundaryHistoryListView.setAdapter(adapter);
        boundaryHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });

        rootView.findViewById(R.id.boundaryHistoryHeaderRL).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh_layout_id);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });

        ImageView infoIV = (ImageView)rootView.findViewById(R.id.btn_info_boundary_alert);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(getActivity(), getActivity().getResources().getString(R.string.info_alerts_history));
                dialog.setTitle(getResources().getString(R.string.about_boundary_alerts));
                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.alerts_array)[1]);
    }


    private ArrayList<AlertHistory> populateData() {

        ArrayList<AlertHistory> alertArrayList = new ArrayList<AlertHistory>();

        AlertHistory alertHistoryObj = new AlertHistory("Soccer Field : entered", "8/12/14 11:42 a.m", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("School : entered",  "8/17/14 10.56 p.m","");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Bobby's House : entered", "12/17/14 12.01 p.m", "");

        alertArrayList.add(alertHistoryObj);

        return alertArrayList;

    }



}
