package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.adapters.AlertHistoryAdapter;
import com.verizontelematics.indrivemobile.adapters.AlertSettingsAdapter;
import com.verizontelematics.indrivemobile.models.AlertHistory;
import com.verizontelematics.indrivemobile.models.AlertSettings;

import java.util.ArrayList;

/**
 * Created by bijesh on 8/25/2014.
 */
public class AlertsGeoFenceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert_geofence,container,false);
        ListView alertHistoryListView = (ListView) rootView.findViewById(R.id.geoHistoryListView);
        ListView geoAlertSettingsListView = (ListView) rootView.findViewById(R.id.geoAlertSettingsListView);
        AlertHistoryAdapter adapter = new AlertHistoryAdapter(getActivity(), R.layout.alert_history_lv_item,populateData());
        alertHistoryListView.setAdapter(adapter);
        alertHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });
        AlertSettingsAdapter settingsAdapter = new AlertSettingsAdapter(getActivity(), R.layout.alert_settings_detail_lv_item, populateDataSettings());
        geoAlertSettingsListView.setAdapter(settingsAdapter);
        geoAlertSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });
        rootView.findViewById(R.id.imgBtn_add).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // add geo fence fragment.
                android.support.v4.app.FragmentTransaction ft = getParentFragment().getFragmentManager().beginTransaction();
                GeoFenceFragment fragment = new GeoFenceFragment();
                ft.addToBackStack("geoFence");
                ft.replace(R.id.container_id, fragment,"geoFence");
                ft.commit();
            }
        });

//        showCoach(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.alerts_array)[1]);
    }

//    private void showCoach(View rootView){
//        new ShowcaseView.Builder(getActivity())
//                .setTarget(new ViewTarget(rootView.findViewById(R.id.imgBtn_add)))
//                .setContentTitle("Add GeoFence")
//                .setContentText("Click on the button to add Geofence").setStyle(R.style.CustomShowcaseThemeUS)
//                .hideOnTouchOutside()
//                .hasManualPosition(true)
//                .xPosition(50)
//                .yPosition(940)
//                .build();
//    }

    private ArrayList<AlertHistory> populateData() {

        ArrayList<AlertHistory> alertArrayList = new ArrayList<AlertHistory>();

        AlertHistory alertHistoryObj = new AlertHistory("Geofence #4 entered","Today","");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Bobby's House entered","Yesterday","");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Geofence #2 entered","8/4","");

        alertArrayList.add(alertHistoryObj);

        return alertArrayList;

    }
    private ArrayList<AlertSettings> populateDataSettings() {

        ArrayList<AlertSettings> alertArrayList = new ArrayList<AlertSettings>();

        AlertSettings obj= new AlertSettings("Geofence", " 1",true);
        alertArrayList.add(obj);
        obj= new AlertSettings("Geofence", " 2",true);
        alertArrayList.add(obj);

        return alertArrayList;

    }

}
