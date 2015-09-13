package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.adapters.HomeMenuAdapter;
import com.verizontelematics.indrivemobile.userprofile.UserFactory;


public class DrivingDataHome extends BaseUIScreenFragment {

    private static final String TAG = DrivingDataHome.class.getSimpleName();
    private ListView drivingDataLV;
    private final String mFragmentLabels[] = {
            "DrivingData"
    };
    private BaseUIScreenFragment mHomeFragment;


    public DrivingDataHome() {
        super();
//        Empty constructor
        mHomeFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_driving_data, container, false);
        drivingDataLV = (ListView) rootView.findViewById(R.id.drivingDataLV);
        setHeaderFooterDivider();
        mActivity.setTitle(getResources().getStringArray(R.array.diagnosis_data_option_list_array)[0]);
        mActivity.setTitleColor(getResources().getColor(R.color.white));
        drivingDataLV = (ListView) rootView.findViewById(R.id.drivingDataLV);
        mOptionMenuItems = mActivity.getResources().getStringArray(R.array.driving_data_option_list_array);
        BaseAdapter mMenuOptionListAdapter = new HomeMenuAdapter(mActivity, mOptionMenuItems);
        drivingDataLV.setAdapter(mMenuOptionListAdapter);
        setListClickListener();
        return rootView;

    }

    private void setHeaderFooterDivider() {

        ImageView headerDividerView = new ImageView(getActivity());
        headerDividerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1));
        headerDividerView.setImageResource(R.color.gray);
        ImageView footerDividerView = new ImageView(getActivity());
        footerDividerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1));
        footerDividerView.setImageResource(R.color.gray);
        drivingDataLV.addHeaderView(headerDividerView);
        drivingDataLV.addFooterView(footerDividerView);
    }

    @Override
    public void setBackgroundColor() {

        mColorCode = getActivity().getResources().getColor(R.color.driving_module_code);
    }

    @Override
    public void setupTitle() {
        // setup title for this UIScreen
        String[] moduleNames = ((BaseUI) mActivity).getModuleNames();
        if (moduleNames.length > 2)
            mTitle = moduleNames[2];
    }

    @Override
    void setupOptionItemNames() {
        // setup option item names
        mOptionMenuItems = mActivity.getResources().getStringArray(R.array.slide_menu_array);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Close the drawer
        ((HomeActivity) getActivity()).getDrawerLayout().closeDrawers();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPageSelectionLost() {

    }

    @Override
    public void onPageSelected() {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String fragmentName = getCurrentSubFragmentTag();
        if (fragmentName.equals("")) {
            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.driving_data_option_list_array)[0],
                    false, false, false,false);
            ((HomeActivity) getActivity()).setTitle("Driving Data");
            ((HomeActivity) getActivity()).showActionToggleButton();
        } else if (fragmentName.equals("DrivingData")) {
            ((HomeActivity) getActivity()).setTitle("Driving Data");
            ((HomeActivity) getActivity()).hideActionToggleButton();
            ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.driving_data_option_list_array)[0],
                    true, false, false, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void setListClickListener() {
        drivingDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                BaseSubUIFragment fragment = null;
                switch (position) {
                    case 1:
                        fragment = new DrivingDataFragment();
                        break;
                    default:
                        break;
                }
                replaceFragmentContainer(fragment,transaction,position,"Driving Data","drivingData");
            }
        });
    }


    private <T extends BaseSubUIFragment> void replaceFragmentContainer(BaseSubUIFragment fragment,FragmentTransaction transaction,int position,String title,String subModule){
        fragment = UserFactory.getInstance(getActivity()).getBaseSubUIFragment(fragment,subModule);
        if (fragment != null) {
            ((HomeActivity)getActivity()).hideCustomActionBar("Driving Data");
            fragment.setHomeFragment(mHomeFragment);
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
            transaction.addToBackStack(mFragmentLabels[position-1]);
            transaction.add(R.id.container_id_dd, fragment, mFragmentLabels[position-1]);
            transaction.commit();
//                    ((HomeActivity) getActivity()).hideActionToggleButton();
//                    ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.driving_data_option_list_array)[0],
//                            true, false, false, false);
            mTitle = getActivity().getResources().getStringArray(R.array.driving_data_option_list_array)[position-1];
            pushFragmentStack(mFragmentLabels[position-1]);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) getActivity()).setCustomActionBarView("", false, false, false, false);
        getActivity().setTitle("Driving Data");
    }
}
