package com.verizontelematics.indrivemobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.EmergencyServicesActivity;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.activity.RoadsideAssistActivity;
import com.verizontelematics.indrivemobile.activity.StolenVehicleActivity;
import com.verizontelematics.indrivemobile.adapters.HomeMenuAdapter;
import com.verizontelematics.indrivemobile.userprofile.UserFactory;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

/**
 * Created by Priyanga on 8/20/2014.
 */
public class EmergencyFragment extends BaseUIScreenFragment {

    private static final String TAG = EmergencyFragment.class.getSimpleName();
    private ListView emergencyLV;
    /* private final String mFragmentLabels[] = new String[]{
            "Emergency Services"
            , "Stolen Vehicle Location Assistance", "Roadside Assistance"
    };*/

    public EmergencyFragment() {
        super();
        BaseUIScreenFragment mHomeFragment = this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        emergencyLV = (ListView) rootView.findViewById(R.id.emergencyLV);
        setHeaderFooterDivider();
        mActivity.setTitle(getResources().getStringArray(R.array.emergency_option_list_array)[0]);
        mActivity.setTitleColor(getResources().getColor(R.color.white));
        mOptionMenuItems = mActivity.getResources().getStringArray(R.array.emergency_option_list_array);
        BaseAdapter mMenuOptionListAdapter = new HomeMenuAdapter(mActivity, mOptionMenuItems);
        emergencyLV.setAdapter(mMenuOptionListAdapter);
        setListClickListener();
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_emergency));
        return rootView;
    }

    private void setHeaderFooterDivider() {

        ImageView headerDividerView = new ImageView(getActivity());
        headerDividerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1));
        headerDividerView.setImageResource(R.color.gray);
        ImageView footerDividerView = new ImageView(getActivity());
        footerDividerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1));
        footerDividerView.setImageResource(R.color.gray);
        emergencyLV.addHeaderView(headerDividerView);
        emergencyLV.addFooterView(footerDividerView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void setupTitle() {
        // setup title for this UIScreen
        String[] moduleNames = ((BaseUI) mActivity).getModuleNames();
        if (moduleNames.length > 1)
            mTitle = moduleNames[1];

    }

    @Override
    public void setBackgroundColor() {

        mColorCode = getActivity().getResources().getColor(R.color.emergency_code);
    }

    private void setListClickListener() {
        emergencyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                BaseSubUIFragment fragment = null;
                switch (position) {

                    case 1:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_emergency),
                                "EmergencyServices");
                        launchActivity(EmergencyServicesActivity.class,"EmergencyServices");
                        break;

                    case 2:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_emergency),
                                "EmergencyStolenVehicle");
                        launchActivity(StolenVehicleActivity.class,"Stolen_Vehicle_Location_Assistance");
                        break;

                    case 3:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_emergency),
                                "EmergencyRoadSide");
                        launchActivity(RoadsideAssistActivity.class,"RoadsideAssistance");
                        break;


                    default:
                        break;
                }
            }
        });
    }

    private void launchActivity(Class activity,String subModule){
//        activity = UserFactory.getInstance(getActivity()).getActivityView(activity,subModule);
        Log.v("Activity is ","activity"+activity);
        Intent intent = new Intent(getActivity(),activity);
        startActivity(intent);
    }


   /* private void launchActivity(Class<? extends Activity> activityTypeClass) {
        Intent intent = new Intent(getActivity(), activityTypeClass);
        startActivity(intent);
    }*/

    @Override
    void setupOptionItemNames() {
        // setup option item names
        mOptionMenuItems = mActivity.getResources().getStringArray(R.array.slide_menu_array);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onPageSelectionLost() {

    }

    @Override
    public void onPageSelected() {

        String fragmentName = getCurrentSubFragmentTag();
        if (fragmentName.equals("")) {
            ((HomeActivity) getActivity()).setCustomActionBarView("",
                    false, false, false, false);
            ((HomeActivity) getActivity()).setTitle("Emergency");
            ((HomeActivity) getActivity()).showActionToggleButton();

        }
    }


}
