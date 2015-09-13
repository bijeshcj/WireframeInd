package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.models.VehiclePartModel;
import com.verizontelematics.indrivemobile.models.data.VehiclePartData;

import java.util.ArrayList;

/**
 * Created by Priyanga on 12/31/2014.
 */
public class DiagnosticsHistoryFragment extends BaseSubUIFragment {

    private SwipeRefreshLayout mRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diagnostics_history, container, false);
        ListView diagnosticsHistoryListView = (ListView) rootView.findViewById(R.id.diagnosticsHistoryListView);
        /*VehicleHealthAdapter adapter = new VehicleHealthAdapter(getActivity(), R.layout.vehicle_part_list_item, populateData());
        diagnosticsHistoryListView.setAdapter(adapter);
        diagnosticsHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });*/

        rootView.findViewById(R.id.diagnosticsHistoryHeaderRL).setOnTouchListener(new View.OnTouchListener() {
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

        ImageView infoIV = (ImageView)rootView.findViewById(R.id.btn_info_diagnostics_alert);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(getActivity(), getActivity().getResources().getString(R.string.info_alerts_history));
                dialog.setTitle(getResources().getString(R.string.about_diagnostics_alerts));
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


    private ArrayList<VehiclePartModel> populateData() {

        ArrayList<VehiclePartModel> alertArrayList = new ArrayList<VehiclePartModel>();

        VehiclePartModel pm = new VehiclePartModel();
        VehiclePartData pd = new VehiclePartData();
        pd.set(VehiclePartData.PART_TITLE, "Battery");
        pd.set(VehiclePartData.STATUS, VehiclePartData.STATUS_OK);
        pm.setData(pd);
        alertArrayList.add(pm);

        VehiclePartModel pm1 = new VehiclePartModel();
        VehiclePartData pd1 = new VehiclePartData();
        pd1.set(VehiclePartData.PART_TITLE, "Alternator");
        pd1.set(VehiclePartData.STATUS, VehiclePartData.STATUS_WARNING);
        pd1.set(VehiclePartData.DTC_CODE, "DTC : 987-898");
        pm1.setData(pd1);
        alertArrayList.add(pm1);

        VehiclePartModel pm2 = new VehiclePartModel();
        VehiclePartData pd2 = new VehiclePartData();
        pd2.set(VehiclePartData.PART_TITLE, "Electrical");
        pd2.set(VehiclePartData.STATUS, VehiclePartData.STATUS_ERROR);
        pd2.set(VehiclePartData.DTC_CODE, "DTC : 009-231");
        pm2.setData(pd2);
        alertArrayList.add(pm2);

        return alertArrayList;

    }



}
