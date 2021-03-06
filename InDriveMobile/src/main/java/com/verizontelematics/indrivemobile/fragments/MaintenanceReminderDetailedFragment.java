package com.verizontelematics.indrivemobile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.models.data.MaintenanceReminderData;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;
import com.verizontelematics.indrivemobile.fragments.CreateMaintenanceReminderFragment.USER_SELECTION;

import java.text.SimpleDateFormat;

/**
 * Created by Priyanga on 12/3/2014.
 */
public class MaintenanceReminderDetailedFragment extends BaseSubUIFragment implements HomeActivity.CustomTopBarItemsClickListener {

    private static final String TAG = MaintenanceReminderDetailedFragment.class.getCanonicalName();
    private MaintenanceReminderData mMaintenanceReminderData;


    public MaintenanceReminderDetailedFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maintenance_reminder_detailed, container, false);

        // parse arguments
        parseArguments();
        // setup
        setup(rootView);

        return rootView;
    }

    private void parseArguments() {

        Bundle args = getArguments();
        if (args != null) {
            mMaintenanceReminderData = args.getParcelable("selected_data");
        }
    }

    private void setup(View rootView) {
        // populate detailed view

        if (mMaintenanceReminderData == null)
            return;

        TextView serviceTypeTV = (TextView) rootView.findViewById(R.id.serviceTypeText);
        TextView customServiceTypeTV = (TextView) rootView.findViewById(R.id.customServiceTypeText);

        String customText = getResources().getString(R.string.custom);
        if (mMaintenanceReminderData.getServiceType().equalsIgnoreCase(customText)) {
            serviceTypeTV.setText(customText);
            customServiceTypeTV.setVisibility(View.VISIBLE);
            customServiceTypeTV.setText(mMaintenanceReminderData.getReminderName());
        } else {
            customServiceTypeTV.setVisibility(View.GONE);
            serviceTypeTV.setText(mMaintenanceReminderData.getServiceName());
        }

        TextView serviceDescTV = (TextView) rootView.findViewById(R.id.serviceDescText);
        String serviceDesc = mMaintenanceReminderData.getReminderDescription() == null ? "" : mMaintenanceReminderData.getReminderDescription();
        serviceDescTV.setText(Html.fromHtml("<b>" + getResources().getString(R.string.description) +  " : </b>" +serviceDesc));

        LinearLayout dateLL = (LinearLayout) rootView.findViewById(R.id.dateDetailLL);
        TextView mileageTV = (TextView) rootView.findViewById(R.id.mileageText);
        TextView dateTV = (TextView) rootView.findViewById(R.id.dateText);
        TextView frequencyTV = (TextView) rootView.findViewById(R.id.frequencyText);


        // Select Date or Mileage option
        USER_SELECTION mUserSelection = (mMaintenanceReminderData.getReminderDate() <= 0) ? USER_SELECTION.MILEAGE : USER_SELECTION.DATE;
        if (mUserSelection.equals(USER_SELECTION.MILEAGE)) {
            dateLL.setVisibility(View.GONE);
            mileageTV.setVisibility(View.VISIBLE);
            mileageTV.setText(Html.fromHtml("<b>" + getResources().getString(R.string.mileage_interval) + " : </b>"  + String.valueOf(mMaintenanceReminderData.getReminderConfigMiles())));

        } else {
            // Alert Date
            dateLL.setVisibility(View.VISIBLE);
            mileageTV.setVisibility(View.GONE);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat mFormatter = new SimpleDateFormat("MM/dd/yyyy");
            dateTV.setText(Html.fromHtml("<b>" + getResources().getString(R.string.date) + " : </b>"  +DateDataFormat.convertMillisAsDateString(getActivity(), mMaintenanceReminderData.getReminderDate(), mFormatter, false)));
            frequencyTV.setText(Html.fromHtml("<b>" + getResources().getString(R.string.repeat) + " : </b>"  +  mMaintenanceReminderData.getReminderConfigMonths()));

        }

        TextView emailTV = (TextView) rootView.findViewById(R.id.emailText);
        TextView messageTV = (TextView) rootView.findViewById(R.id.messageText);

        if (mMaintenanceReminderData.isNotificationFlagEmail())
            emailTV.setVisibility(View.VISIBLE);
        else
            emailTV.setVisibility(View.GONE);

        if (mMaintenanceReminderData.isNotificationFlagSms())
            messageTV.setVisibility(View.VISIBLE);
        else
            messageTV.setVisibility(View.GONE);

    }

    @Override
    public void onTopBarItemClick(View aView) {

        if (aView.getId() == HomeActivity.DONE_BUTTON_ID) {
            onEditReminder();
        }

    }

    private void onEditReminder() {
        // prepare parcel with selected reminder
        Bundle reminderDetailedArguments = new Bundle();
        reminderDetailedArguments.putBoolean("edit_mode", true);
        reminderDetailedArguments.putParcelable("selected_data", mMaintenanceReminderData);

        // launch edit view of reminder
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        CreateMaintenanceReminderFragment fragment = new CreateMaintenanceReminderFragment();
        fragment.setHomeFragment(getHomeFragment());
        // pass arguments with selected index and edit mode
        fragment.setArguments(reminderDetailedArguments);

        // launch fragment.
        ft.addToBackStack("EditMaintenanceReminder");
        int mContainerId = R.id.container_id_diagnostics;
        ft.replace(mContainerId, fragment, "EditMaintenanceReminder");
        ft.commit();
        getHomeFragment().pushFragmentStack("EditMaintenanceReminder");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).showTabBar(false);
        ((HomeActivity) activity).setCustomActionBarView(getActivity().getResources().getString(R.string.maintenance_reminder), true, false, true,true);
        ((HomeActivity) getActivity()).addToDoneButtonClickHandlers(this);
    }

    @Override
    public void onSubFragmentResume() {
        super.onSubFragmentResume();
        if (getActivity() == null)
            return;
        ((HomeActivity) getActivity()).showTabBar(false);
        ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.maintenance_reminder), true, false, true,true);
        ((HomeActivity) getActivity()).addToDoneButtonClickHandlers(this);
    }

    @Override
    public void onSubFragmentPause() {
        super.onSubFragmentPause();
        ((HomeActivity) getActivity()).removeFromDoneButtonHandlers(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity) getActivity()).showTabBar(true);
        ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[1], true, false, false,true);
        ((HomeActivity) getActivity()).removeFromDoneButtonHandlers(this);
    }

}
