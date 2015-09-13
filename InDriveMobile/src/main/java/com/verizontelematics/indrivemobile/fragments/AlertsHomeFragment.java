package com.verizontelematics.indrivemobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

import java.util.Random;

/**
 * Created by Priyanga on 8/25/2014.
 */
public class AlertsHomeFragment extends AlertsFragment implements View.OnClickListener {

    //private static Context mContext;
    //void title;

    //private views;




    public AlertsHomeFragment() {
        //mContext=ctx;
//        Empty constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert_menu, container, false);
        RelativeLayout geoFenceIV = (RelativeLayout) rootView.findViewById(R.id.geofenceIV);
        RelativeLayout speedAlertsIV = (RelativeLayout) rootView.findViewById(R.id.speedAlertsIV);
        RelativeLayout diagnosticsAlertsIV = (RelativeLayout) rootView.findViewById(R.id.diagnosticsAlertsIV);
        RelativeLayout maintenanceAlertsIV = (RelativeLayout) rootView.findViewById(R.id.maintenanceAlertsIV);
        RelativeLayout valetAlertsIV = (RelativeLayout) rootView.findViewById(R.id.valetAlertsIV);

        final Random gen = new Random();
        // Badge counters for the respective alerts
        int counter_1 = gen.nextInt(5);
        TextView badge_geofence= (TextView) rootView.findViewById(R.id.textViewGeofence);
        badge_geofence.setText(Integer.toString(counter_1));
        if(counter_1 ==0)
            badge_geofence.setVisibility(TextView.INVISIBLE);

        int counter_2 = gen.nextInt(4);
        TextView badge_speed= (TextView) rootView.findViewById(R.id.textViewSpeed);
        badge_speed.setText(Integer.toString(counter_2));
        if(counter_2 ==0)
            badge_speed.setVisibility(TextView.INVISIBLE);

        int counter_3 = gen.nextInt(10);
        TextView badge_diagnostics= (TextView) rootView.findViewById(R.id.textViewDiagnostics);
        badge_diagnostics.setText(Integer.toString(counter_3));
        if(counter_3 ==0)
            badge_diagnostics.setVisibility(TextView.INVISIBLE);

        int counter_4 = gen.nextInt(6);
        TextView badge_maintenance= (TextView) rootView.findViewById(R.id.textViewMaintenance);
        badge_maintenance.setText(Integer.toString(counter_4));
        if(counter_4 ==0)
            badge_maintenance.setVisibility(TextView.INVISIBLE);

        int counter_5 = gen.nextInt(8);
        TextView badge_valet= (TextView) rootView.findViewById(R.id.textViewValet);
        badge_valet.setText(Integer.toString(counter_5));
        if(counter_5 ==0)
            badge_valet.setVisibility(TextView.INVISIBLE);



        geoFenceIV.setOnClickListener(this);
        speedAlertsIV.setOnClickListener(this);
        diagnosticsAlertsIV.setOnClickListener(this);
        maintenanceAlertsIV.setOnClickListener(this);
        valetAlertsIV.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.alerts_array)[0]);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //Create the instance of fragment transaction
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        removeViews();
        switch (id) {
            case (R.id.geofenceIV): {

                Fragment alertsGeoFragment = new AlertsGeoFenceFragment();

               transaction.addToBackStack("GeoFenceAlerts");
                transaction.replace(R.id.nested_fragment, alertsGeoFragment, "GeoFenceAlerts");
                transaction.commit();
                break;
            }
            case (R.id.speedAlertsIV): {
                Fragment alertsSpeedFragment = new SpeedAlertFragment();
                transaction.addToBackStack("SpeedAlert");
                transaction.replace(R.id.nested_fragment, alertsSpeedFragment, "SpeedAlert");
                transaction.commit();
                break;
            }
            case (R.id.diagnosticsAlertsIV): {
                Fragment alertsDiagnosticsFragment = new DiagnosticsAlertFragment();
                transaction.addToBackStack("DiagnosticsAlert");
                transaction.replace(R.id.nested_fragment, alertsDiagnosticsFragment, "DiagnosticsAlert");
                transaction.commit();
                break;
            }
            case (R.id.maintenanceAlertsIV): {
                Fragment alertsMaintenanceFragment = new MaintenanceInformationFragment();
                transaction.addToBackStack("MaintenanceAlert");
                transaction.replace(R.id.nested_fragment, alertsMaintenanceFragment, "MaintenanceAlert");
                transaction.commit();
                break;
            }
            case (R.id.valetAlertsIV): {
                Fragment alertValetFragment = new AlertValetFragment();
                transaction.addToBackStack("ValetAlert");
                transaction.replace(R.id.nested_fragment, alertValetFragment, "ValetAlert");
                transaction.commit();
                break;
            }
            default:
                break;
        }
    }

    private void removeViews() {

        //Remove all views from Content Layout
        RelativeLayout contentLayout = (RelativeLayout) getActivity().findViewById(R.id.nested_fragment);
        if (contentLayout == null)
            return;
        contentLayout.removeAllViews();
    }
}
