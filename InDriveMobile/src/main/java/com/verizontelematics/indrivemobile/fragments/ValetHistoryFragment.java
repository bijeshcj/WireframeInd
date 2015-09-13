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
public class ValetHistoryFragment extends BaseSubUIFragment {

    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_valet_history, container, false);
        ListView valetHistoryListView = (ListView) rootView.findViewById(R.id.valetHistoryListView);
        AlertHistoryAdapter adapter = new AlertHistoryAdapter(getActivity(), R.layout.boundary_history_lv_item, populateData());
        valetHistoryListView.setAdapter(adapter);
        valetHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });

        rootView.findViewById(R.id.valetHistoryHeaderRL).setOnTouchListener(new View.OnTouchListener() {
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

        ImageView infoIV = (ImageView)rootView.findViewById(R.id.btn_info_valet_alert);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(getActivity(), getActivity().getResources().getString(R.string.info_alerts_history));
                dialog.setTitle(getResources().getString(R.string.about_valet_alerts));
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

        AlertHistory alertHistoryObj = new AlertHistory("Peach tree street", "8/14/14   11:42 a.m", "");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("AshFord Drive", "9/25/14   8:09 p.m ", "");

        alertArrayList.add(alertHistoryObj);

        return alertArrayList;

    }



}
