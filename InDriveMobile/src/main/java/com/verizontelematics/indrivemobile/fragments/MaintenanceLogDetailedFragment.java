package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.models.data.MaintenanceLogData;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;

/**
 * Created by z688522 on 12/2/14.
 */
public class MaintenanceLogDetailedFragment extends BaseSubUIFragment implements HomeActivity.CustomTopBarItemsClickListener{

    private final String TAG = "MaintenanceLogDetailedFragment";

    private View mHeaderView;
    private String mTitle;
    private TextView mDetailedView;

    private MaintenanceLogData mMaintenanceLogData;
    private TextView mTitleView,mTitleCustom;

    public MaintenanceLogDetailedFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maintenance_log_detailed, container, false);
        mDetailedView = (TextView) rootView.findViewById(R.id.txt_log_detailed);
        mTitleView = (TextView) rootView.findViewById(R.id.txt_title);
        mTitleCustom=(TextView) rootView.findViewById(R.id.txt_title_custom);
        // parse arguments
        parseArguments();
        // setup
        setup(rootView);
        return rootView;
    }

    private void parseArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mMaintenanceLogData = args.getParcelable("selected_data");
        }
    }

    private void setup(View rootView) {
        // populate detailed view
        populateUI();
    }

    private void populateUI() {
        //((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.maintenance_log_detailed), true,false,true,true);
        if (mMaintenanceLogData == null)
            return;
        // String ServiceName;
        // Title

        String customText = getResources().getString(R.string.custom);
        if (mMaintenanceLogData.getServiceType().equalsIgnoreCase(customText)) {
            mTitleView.setText(customText);
            mTitleCustom.setVisibility(View.VISIBLE);
            mTitleCustom.setText(mMaintenanceLogData.getServiceName());
        } else {
            mTitleCustom.setVisibility(View.GONE);
            mTitleView.setText(mMaintenanceLogData.getServiceName());
        }

       // mTitleView.setText(Html.fromHtml("<b>" + mMaintenanceLogData.getServiceName() + "</b><br>"));

        String serviceCenter = mMaintenanceLogData.getServiceCenter();
        serviceCenter = (serviceCenter == null || serviceCenter.equals("null")) ? "" : serviceCenter;

        String cost = mMaintenanceLogData.getCost();
        cost = (cost == null || cost.equals("0.0") ? "" : cost);

        String description = mMaintenanceLogData.getDescription();
        description = (description == null || description.equals("null") ? "" : description);

        String mileage = mMaintenanceLogData.getMileage();
        mileage = (mileage == null || mileage.equals("0") ? "" : mileage);

        mDetailedView.setText(Html.fromHtml("<b>" + getResources().getString(R.string.label_location) + " : </b>" + serviceCenter + "<br><br>"
                        + "<b>" + getResources().getString(R.string.description) + " : </b>" + description + "<br><br>"
                        + "<b>" + getResources().getString(R.string.cost) + " : </b>" + cost + "<br><br>"
                        + "<b>" + getResources().getString(R.string.date) + " : </b>" + DateDataFormat.convertMillisAsDateString(getActivity(),mMaintenanceLogData.getServiceDate(), true) + "<br><br>"
                        + "<b>" + getResources().getString(R.string.mileage) +" : </b>" + mileage+"<br><br>"
        ));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity)activity).showTabBar(false);
        ((HomeActivity)activity).setCustomActionBarView(getActivity().getResources().getString(R.string.maintenance_log_detailed), true,false,true,true);
        ((HomeActivity)getActivity()).addToDoneButtonClickHandlers(this);
    }

    @Override
    public void onSubFragmentResume() {
        super.onSubFragmentResume();
        if (getActivity() == null)
            return;
        ((HomeActivity)getActivity()).showTabBar(false);
        ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.maintenance_log_detailed), true,false,true,true);
        ((HomeActivity)getActivity()).addToDoneButtonClickHandlers(this);
    }

    @Override
    public void onSubFragmentPause() {
        super.onSubFragmentPause();
        ((HomeActivity)getActivity()).removeFromDoneButtonHandlers(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity)getActivity()).showTabBar(true);
        ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[2],true,false,false,true);
        ((HomeActivity)getActivity()).removeFromDoneButtonHandlers(this);
    }

    @Override
    public void onTopBarItemClick(View aView) {

        if(aView.getId() == HomeActivity.DONE_BUTTON_ID) {
            onEditLog();
        }
    }

    private void onEditLog() {
        // prepare parcel with selected log
        Bundle logDetailedArguments = new Bundle();
        logDetailedArguments.putBoolean("edit_mode", true);
        logDetailedArguments.putParcelable("selected_data", mMaintenanceLogData);

        // launch detailed view of log
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        CreateMaintenanceLogFragment fragment = new CreateMaintenanceLogFragment();
        fragment.setHomeFragment(getHomeFragment());
        // pass arguments with selected index and edit mode
        fragment.setArguments(logDetailedArguments);

        // launch fragment.
        ft.addToBackStack("EditMaintenanceLog");
        int mContainerId = R.id.container_id_diagnostics;
        ft.replace(mContainerId, fragment, "EditMaintenanceLog");
        ft.commit();
        getHomeFragment().pushFragmentStack("EditMaintenanceLog");

    }
}
