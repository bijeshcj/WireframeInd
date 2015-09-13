package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.adapters.HomeMenuAdapter;
import com.verizontelematics.indrivemobile.userprofile.UserFactory;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

/**
 * Created by Priyanga on 8/20/2014.
 */
public class DiagnosticsFragment extends BaseUIScreenFragment {
    private static String TAG = DiagnosticsFragment.class.getCanonicalName();
    private Button enterDiagnostics;
    private ListView diagnosticsLV;
    private View rootView;

    private final String[] mFragmentLabels = {
            "VehicleHealth"
            , "MaintenanceReminder"
            , "MaintenanceLogAlert"
            , "RecallInfo"
    };
    private BaseUIScreenFragment mHomeFragment;


    public DiagnosticsFragment() {
        super();
//        Empty constructor
        mHomeFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_diagnostics, container, false);
        diagnosticsLV = (ListView) rootView.findViewById(R.id.diagnosticsLV);
        setHeaderFooterDivider();
        mActivity.setTitle(getResources().getStringArray(R.array.diagnosis_data_option_list_array)[0]);
        mActivity.setTitleColor(getResources().getColor(R.color.white));
        mOptionMenuItems = mActivity.getResources().getStringArray(R.array.diagnosis_data_option_list_array);
        BaseAdapter mMenuOptionListAdapter = new HomeMenuAdapter(mActivity, mOptionMenuItems);
        diagnosticsLV.setAdapter(mMenuOptionListAdapter);
        setListClickListener();
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_diagnostics));
//        showUpgradePage();
        return rootView;

    }

    private void showUpgradePage()
    {
        TextView labelTV = (TextView)rootView.findViewById(R.id.diagnosticsIntroTV);
        labelTV.setText(getResources().getString(R.string.diagnostics_upgrade_lbl));
        diagnosticsLV.setVisibility(View.GONE);
        TextView upgradeTV = (TextView)rootView.findViewById(R.id.diagnosticsUpgradeTV);
        upgradeTV.setVisibility(View.VISIBLE);
        upgradeTV.setText(getResources().getString(R.string.vehicle_health_upsell_text));
    }

    private void setHeaderFooterDivider() {

        ImageView headerDividerView = new ImageView(getActivity());
        headerDividerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1));
        headerDividerView.setImageResource(R.color.gray);
        ImageView footerDividerView = new ImageView(getActivity());
        footerDividerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1));
        footerDividerView.setImageResource(R.color.gray);
        diagnosticsLV.addHeaderView(headerDividerView);
        diagnosticsLV.addFooterView(footerDividerView);
    }

    @Override
    public void setBackgroundColor() {

        mColorCode = getActivity().getResources().getColor(R.color.diagnostics_code);
    }

    @Override
    public void setupTitle() {
        // setup title for this UIScreen
        String[] moduleNames = ((BaseUI) mActivity).getModuleNames();
        if (moduleNames.length > 3)
            mTitle = moduleNames[3];
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
        //Create the instance of fragment transaction
        /*FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        //Remove all views from Content Layout
        RelativeLayout contentLayout = (RelativeLayout) getActivity().findViewById(R.id.container_id_diag);
        if (contentLayout == null)
            return;
        contentLayout.removeAllViews();
        mIndex = position;
        switch (position) {

            case 0:
                //Diagnostics Home Fragment
               DiagnosticsFragment diagnosticsHome = new DiagnosticsFragment();
                setupTitle();
                transaction.addToBackStack("DiagnosticsHome");
                transaction.replace(R.id.container_id_diag, diagnosticsHome, "DiagnosticsHome");
                transaction.commit();
                getActivity().setTitle("Diagnostics");
                break;


            case 6:
                Log.d(TAG, "Logout");
                ((HomeActivity) getActivity()).logOut();
                break;

            default:
                break;
        }*/
    }

    @Override
    public void onPageSelectionLost() {

        ((HomeActivity) getActivity()).showActionToggleButton();
        ((HomeActivity)getActivity()).setCustomActionBarView("",
                false, false, false,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onPageSelected() {


        FragmentManager fm = getFragmentManager();

        boolean isFragmentVisible = false;

        Fragment tempFragment = fm.findFragmentByTag("OptionListSelection");

        if (tempFragment != null) {
            isFragmentVisible = tempFragment.isVisible();
        }
        if (isFragmentVisible) {
            ((HomeActivity) getActivity()).hideActionToggleButton();
            ((HomeActivity) getActivity()).setTitle(getResources().getString(R.string.serviceselection));
        }

        String fragmentName = getCurrentSubFragmentTag();
        if(fragmentName.equals(""))
        {
            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[0],
                    false, false, false,false);
            ((HomeActivity)getActivity()).setTitle("Diagnostics");
            ((HomeActivity)getActivity()).showActionToggleButton();

        }
        else if(fragmentName.equals("VehicleHealth"))
        {
           // ((HomeActivity)getActivity()).setTitle(" Vehicle Health");
            ((HomeActivity)getActivity()).hideActionToggleButton();
            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[0],
                    true, false, false,false);
        }
        else if(fragmentName.equals("MaintenanceReminder"))
        {
           // ((HomeActivity)getActivity()).setTitle("Maintenance Reminder");
            ((HomeActivity)getActivity()).hideActionToggleButton();
            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[1],
                    true, false, false,false);
        }
        else if(fragmentName.equals("MaintenanceLogAlert"))
        {
            ((HomeActivity)getActivity()).setTitle("Maintenance Log");
            ((HomeActivity)getActivity()).hideActionToggleButton();
            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2],
                    true, false, false,false);

        }
        else if(fragmentName.equals("RecallInfo"))
        {
            ((HomeActivity)getActivity()).setTitle("Recall Info");
            ((HomeActivity)getActivity()).hideActionToggleButton();
            ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[3],
                    true, false, false,false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.v("onResume","onResume of DiagnosticsFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v("onConfigurationChanged","diagnostics Screen");
    }



    private void setListClickListener() {
        diagnosticsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                BaseSubUIFragment fragment = null;
                switch (position) {

                    case 1:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                                "DiagnosticsHealth");
                        fragment = new VehicleHealthFragment();
                        replaceFragmentContainer(fragment,transaction,position,"Vehicle Health","vehicleHealthShowServiceNotEnabled");
                        break;

                    case 2:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                                "DiagnosticsReminder");
                        fragment = new MaintenanceReminderFragment();
//                        String[] sub = {"maintenanceRemaindersView","maintenanceRemaindersViewEditButton",
//                        "maintenanceRemainderAdd","maintenanceRemainderEditDelete"};
                        replaceFragmentContainer(fragment,transaction,position,"Vehicle Health","maintenanceRemaindersView");
                        break;

                    case 3:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                                "DiagnosticsLog");
                        fragment = new MaintenanceLogFragment();
                        replaceFragmentContainer(fragment,transaction,position,"Vehicle Health","maintenanceLogsView");
                        break;

                    case 4:
                        new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                                "DiagnosticsRecall");
                        fragment = new RecallInformationFragment();
                        replaceFragmentContainer(fragment,transaction,position,"Vehicle Health","recallInformationCurrentAndCompleted");
                        break;

                    default:
                        break;
                }

            }
        });
    }


    private <T extends BaseSubUIFragment> void replaceFragmentContainer(BaseSubUIFragment fragment,FragmentTransaction transaction,int position,String title,String subModule){
        fragment = UserFactory.getInstance(getActivity()).getBaseSubUIFragment(fragment,subModule);
        if (fragment != null) {
            ((HomeActivity)getActivity()).hideCustomActionBar("Vehicle Health");
            fragment.setHomeFragment(mHomeFragment);
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
            transaction.addToBackStack(mFragmentLabels[position-1]);
            transaction.add(R.id.container_id_diagnostics, fragment, mFragmentLabels[position-1]);
            transaction.commit();
            mTitle = getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[position-1];
            pushFragmentStack(mFragmentLabels[position-1]);
        }

    }

    private <T extends BaseSubUIFragment> void replaceFragmentContainer(BaseSubUIFragment fragment,FragmentTransaction transaction,int position,String title,String[] subModules){
        fragment = UserFactory.getInstance(getActivity()).getBaseSubUIFragment(fragment,subModules);

        if (fragment != null) {
            ((HomeActivity)getActivity()).hideCustomActionBar("Vehicle Health");
            fragment.setHomeFragment(mHomeFragment);
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
            transaction.addToBackStack(mFragmentLabels[position-1]);
            transaction.add(R.id.container_id_diagnostics, fragment, mFragmentLabels[position-1]);
            transaction.commit();
            mTitle = getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[position-1];
            pushFragmentStack(mFragmentLabels[position-1]);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        Log.v("onAttach", "onAttach of Diagnostics Fragment");
        super.onAttach(activity);
        ((HomeActivity) getActivity()).setCustomActionBarView("", false, false, false,false);
        getActivity().setTitle("Diagnostics");
    }


}
