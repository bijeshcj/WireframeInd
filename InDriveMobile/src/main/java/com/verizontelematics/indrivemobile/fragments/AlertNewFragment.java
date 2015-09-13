package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by bijesh on 11/18/2014.
 */
public class AlertNewFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_alert_new, container, false);
        TextView mAlertSettings = (TextView) rootView.findViewById(R.id.txtSettings);
        mAlertSettings.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
         replaceFragment();
    }

    private void removeViews(){
        //Remove all views from Content Layout
        LinearLayout contentLayout = (LinearLayout)getActivity().findViewById(R.id.container_alert_new);
        if (contentLayout == null)
            return;
        contentLayout.removeAllViews();
    }

    private void replaceFragment(){
            removeViews();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment alertSettings = new AlertSettingsFragment();
            transaction.addToBackStack("AlertSettingsFragment");
            transaction.replace(R.id.container_alert_new, alertSettings, "AlertSettingsFragment");
            transaction.commit();
    }
}
