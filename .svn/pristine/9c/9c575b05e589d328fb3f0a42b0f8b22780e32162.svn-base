package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.IndriveHomeActivity;
import com.verizontelematics.indrivemobile.adapters.IndriveHomeAdapter;
import com.verizontelematics.indrivemobile.models.IndriveHomeAdapterModel;

import java.util.ArrayList;

/**
 * Created by bijesh on 11/18/2014.
 */
public class IndriveHomeFragment extends Fragment implements AdapterView.OnItemClickListener{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView mOptionsListView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static IndriveHomeFragment newInstance(int sectionNumber) {
        IndriveHomeFragment fragment = new IndriveHomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public IndriveHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView){
        mOptionsListView = (ListView)rootView.findViewById(R.id.homeListView);
        ArrayList<IndriveHomeAdapterModel> listModels = populateOptionsList();
        IndriveHomeAdapter lstAdapter = new IndriveHomeAdapter(getActivity().getApplicationContext(),listModels);
        mOptionsListView.setAdapter(lstAdapter);

        ListView mAlertsListView = (ListView) rootView.findViewById(R.id.home_alerts_list);



        setListeners();
    }

    private ArrayList<IndriveHomeAdapterModel> populateOptionsList(){
        IndriveHomeAdapterModel alertOptions = new IndriveHomeAdapterModel("ALERT SETTINGS","Manage your automated alerts");
        IndriveHomeAdapterModel locationOptions = new IndriveHomeAdapterModel("LOCATION","Manage boundaries and locations");
        IndriveHomeAdapterModel drivingDataOptions = new IndriveHomeAdapterModel("DRIVING DATA","View mileage,speed and fuel costs");
        IndriveHomeAdapterModel diagnosticsOptions = new IndriveHomeAdapterModel("DIAGNOSTICS","Maximize your car's potential");
        IndriveHomeAdapterModel emergencyOptions = new IndriveHomeAdapterModel("EMERGENCY","");

        ArrayList<IndriveHomeAdapterModel> lstModels = new ArrayList<IndriveHomeAdapterModel>();
        lstModels.add(alertOptions);
        lstModels.add(locationOptions);
        lstModels.add(drivingDataOptions);
        lstModels.add(diagnosticsOptions);
        lstModels.add(emergencyOptions);

        return lstModels;

    }

    private void setListeners(){
        mOptionsListView.setOnItemClickListener(this);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Context mContext = activity;
        ((IndriveHomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            Toast.makeText(getActivity(),"Alert settings ",Toast.LENGTH_SHORT).show();
            replaceFragment(position);
        }
    }

    private void replaceFragment(int position){
        if(position == 0){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment indriveBaseOptionsFragment = new IndriveBaseOptionsFragment();
            transaction.addToBackStack("IndriveBaseOptionsFragment");
            transaction.replace(R.id.container, indriveBaseOptionsFragment, "IndriveBaseOptionsFragment");
            transaction.commit();
        }
    }
}


