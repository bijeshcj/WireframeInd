package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.RecallData;
import com.verizontelematics.indrivemobile.models.data.VehicleInfoData;
import com.verizontelematics.indrivemobile.utils.BasicUtil;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;

import java.util.Calendar;

/**
 * Created by z688522 on 10/8/14.
 */
public class RecallInfoDetailFragment extends BaseSubUIFragment implements View.OnClickListener, HomeActivity.CustomTopBarItemsClickListener, UIInterface {
    private static final String TAG = RecallInfoDetailFragment.class.getSimpleName();
    private TextView mTxtName;
    private TextView mTxtDate;
    private TextView mTxtID;
    private TextView mTxtComponent;
    private TextView mTxtVehicleImpacted;
    private TextView mTxtManufacturer;
    private TextView mTxtManufacturingDates;
    private TextView mTxtUnits;
    private TextView mTxtSummary;
    private TextView mTxtDesc;
    private TextView mTxtAction;

    private ImageView onStateStatus, offStateStatus;
    private RecallData mRecallData;
    private ProgressDialogFragment mProgressDialogFragment = null;


    public RecallInfoDetailFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstance) {

        // parse arguments
        if (getArguments() != null) {
            mRecallData = getArguments().getParcelable("selected_data");
        }

        View view = layoutInflater.inflate(R.layout.fragment_recall_detailed, container, false);
        // setup ui elements
        setupUI(view);
        setHasOptionsMenu(true); 
        setListenersForSaveCancel();

        mTxtComponent=(TextView)view.findViewById(R.id.componentTV);
        mTxtID=(TextView)view.findViewById(R.id.idNoTV);
        mTxtVehicleImpacted=(TextView)view.findViewById(R.id.vehicleImpactTV);
        mTxtManufacturer=(TextView)view.findViewById(R.id.manufacturerTV);
        mTxtManufacturingDates=(TextView)view.findViewById(R.id.manufacturerDatesTV);
        mTxtUnits=(TextView)view.findViewById(R.id.unitsAffectedTV);
        mTxtSummary=(TextView)view.findViewById(R.id.summaryTV);
        mTxtDesc=(TextView)view.findViewById(R.id.defectDescTV);
        mTxtAction=(TextView)view.findViewById(R.id.correctiveActionTV);

        updateUI();

        DiagnosticController.instance().register(this);
        return view;
    }

    private void updateUI() {
        populateUIElements(mRecallData);
    }

    private void populateUIElements(RecallData mRecallData) {
        // get the data to populate
        // alert name
        if (mRecallData == null)
            return;

        mTxtName.setText(mRecallData.getComponentName());
        // date
        mTxtDate.setText(DateDataFormat.convertMillisAsDateString(getActivity(), mRecallData.getDateIssued(), true));
        // component name
        String str = mRecallData.getComponentName();
        str = (str == null || str.equalsIgnoreCase("null")) ? "" : str;
        mTxtComponent.setText(Html.fromHtml("<b>" + getResources().getString(R.string.component)+":\t" + "</b> "+str)); //"Component:\t"

        // Recall id
        str = mRecallData.getRecallID();
        str = (str == null || str.equalsIgnoreCase("null")) ? "" : str;
        mTxtID.setText(Html.fromHtml("<b>" + getResources().getString(R.string.id_number)+":\t" + "</b> "+str));

        // Vehicle Impacted
        String vehicleImpacted = "";
        if (DiagnosticController.instance().getVehicleInfoModel() != null) {
            if (VehicleInfoData.class.isInstance(DiagnosticController.instance().getVehicleInfoModel().getData())) {
                VehicleInfoData vehicleInfoData = (VehicleInfoData)DiagnosticController.instance().getVehicleInfoModel().getData();
                vehicleImpacted += vehicleInfoData.get("Model")
                                  +" " + vehicleInfoData.get("Make")
                                  +" " + vehicleInfoData.get("Year");
            }
        }
        mTxtVehicleImpacted.setText(Html.fromHtml("<b>" +getResources().getString(R.string.vehicle_impacted)+":\t" + "</b> "+vehicleImpacted));

        // Manufacturer
        str = mRecallData.getManufacturer();
        str = (str == null || str.equalsIgnoreCase("null")) ? "" : str;
        mTxtManufacturer.setText(Html.fromHtml("<b>" + getResources().getString(R.string.manufacturer)+":\t" + "</b> "+str));

        mTxtManufacturingDates.setText(Html.fromHtml("<b>" + getResources().getString(R.string.manufacturing_dates)+":\t" + "</b> "
                +DateDataFormat.convertMillisAsDateString(getActivity(), mRecallData.getManufacturingBeginDate(), true)+" - "
                +DateDataFormat.convertMillisAsDateString(getActivity(), mRecallData.getManufacturingEndDate(), true)));

        // Potential Number of units Affected
        str = mRecallData.getPotentialAffectedUnits();
        str = (str == null || str.equalsIgnoreCase("null")) ? "" : str;
        mTxtUnits.setText(Html.fromHtml("<b>" + getResources().getString(R.string.potential_number_of_units_affected)+":\t" + "</b> "+str));

        // Summary
        str = mRecallData.getSummary();
        str = (str == null || str.equalsIgnoreCase("null")) ? "" : str;
        mTxtSummary.setText(Html.fromHtml("<b>" + getResources().getString(R.string.summary)+":\t" + "</b> "+str));

        // Defected Description
        str = mRecallData.getDefectDescription();
        str = (str == null || str.equalsIgnoreCase("null")) ? "" : str;
        mTxtDesc.setText(Html.fromHtml("<b>" + getResources().getString(R.string.defect_description)+":\t" + "</b> "+str));

        // CorrectiveAction
        str = mRecallData.getCorrectiveAction();
        str = (str == null || str.equalsIgnoreCase("null")) ? "" : str;
        mTxtAction.setText(Html.fromHtml("<b>" + getResources().getString(R.string.corrective_action)+":\t" + "</b> "+str));


        //Set the switch to OFF-Current; ON-Completed Recalls
        boolean completed = (mRecallData.getDateCompleted() > 0);

        if (completed) {
            onStateStatus.setVisibility(View.VISIBLE);
            offStateStatus.setVisibility(View.INVISIBLE);
        } else {
            onStateStatus.setVisibility(View.INVISIBLE);
            offStateStatus.setVisibility(View.VISIBLE);
        }
    }

    private void setupUI(View view) {
        mTxtName = (TextView) view.findViewById(R.id.txt_recall_title);
        mTxtDate = (TextView) view.findViewById(R.id.txt_recall_date);



        RelativeLayout switchLayout = (RelativeLayout) view.findViewById(R.id.completedSwitch);
        onStateStatus = (ImageView) view.findViewById(R.id.onStateCompleted);
        offStateStatus = (ImageView) view.findViewById(R.id.offStateCompleted);
        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offStateStatus.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateStatus.setVisibility(View.VISIBLE);
                    offStateStatus.setVisibility(View.INVISIBLE);
                } else if (offStateStatus.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onStateStatus.setVisibility(View.INVISIBLE);
                    offStateStatus.setVisibility(View.VISIBLE);
                } else if (onStateStatus.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onStateStatus.setVisibility(View.INVISIBLE);
                    offStateStatus.setVisibility(View.VISIBLE);
                } else {
                    //turning the state as OFF
                    onStateStatus.setVisibility(View.VISIBLE);
                    offStateStatus.setVisibility(View.INVISIBLE);
                }


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_close:
//                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
//                ft.remove(this);
//                ft.commit();
//                break;
            default:
                break;
        }
    }

    @Override
    public void onTopBarItemClick(View aView) {

        if (aView.getId() == HomeActivity.LEFT_ARROW_ID) {
            removeFragment();
            BasicUtil.hideKeyboard(getActivity());

        } else if (aView.getId() == HomeActivity.DONE_BUTTON_ID) {
            updateRecall();
        }
    }

    private void updateRecall() {
        if(mRecallData != null) {
            boolean completed = (mRecallData.getDateCompleted() > 0);
            // check if status changed
            if (completed == (onStateStatus.getVisibility() == View.VISIBLE)) {

                removeFragment();
                return;
            }

            // update recall by
            if (onStateStatus.getVisibility() == View.VISIBLE) {
                mRecallData.setDateCompleted(Calendar.getInstance().getTimeInMillis());
            } else {
                mRecallData.setDateCompleted(0);
            }
            DiagnosticController.instance().setRecallCompleted(getActivity(), mRecallData);
        }
    }

    private void removeFragment() {

        DiagnosticController.instance().unregister(this);
        if (getFragmentManager() == null || getActivity() == null)
            return;
        BasicUtil.hideKeyboard(getActivity());
        getHomeFragment().popFragmentStack();
        //getFragmentManager().popBackStack();


    }

    private void setListenersForSaveCancel() {
        ((HomeActivity) getActivity()).addToDoneButtonClickHandlers(this);
        ((HomeActivity) getActivity()).addToLeftArrowClickHandlers(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).showTabBar(false);
        ((HomeActivity) activity).setCustomActionBarView(getActivity().getResources().getString(R.string.recall_info_details), true, false, true,false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProgressDialogFragment = null;
        ((HomeActivity) getActivity()).showTabBar(true);
        ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.diagnosis_data_option_list_array)[3], true, false, false,false);
        DiagnosticController.instance().unregister(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressDialogFragment = null;
        ((HomeActivity) getActivity()).removeFromDoneButtonHandlers(this);
        ((HomeActivity) getActivity()).removeFromLeftArrowHandlers(this);
      //  ((HomeActivity) getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.recall_info_details), false, false, true);
        DiagnosticController.instance().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mProgressDialogFragment = null;
        DiagnosticController.instance().unregister(this);
    }



    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.SET_RECALL_COMPLETED.ordinal()) {
            Log.d(TAG, "Progress "+opr.getInformation());
            // Show progress notification
            if (mProgressDialogFragment == null)
                mProgressDialogFragment = ProgressDialogFragment.createInstance("", getHomeFragment());

            mProgressDialogFragment.setHomeFragment(getHomeFragment());
            int mContainerIdForProgressDialog = R.id.fragment_recall_detailed;
            mProgressDialogFragment.show(getFragmentManager(), mContainerIdForProgressDialog);
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.SET_RECALL_COMPLETED.ordinal()) {
            Log.d(TAG, "Error "+opr.getInformation());
            // stop progress notification
            if(mProgressDialogFragment != null) {
                mProgressDialogFragment.dismiss();
                mProgressDialogFragment = null;
            }
            removeFragment();
            if (getActivity() == null)
                return;
            Toast lToast = Toast.makeText(getActivity(), getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT);
            lToast.show();

        }
    }

    @Override
    public void onSuccess(Operation opr) {

        if (opr.getId() == Operation.OperationCode.SET_RECALL_COMPLETED.ordinal()) {
            // Stop progress notification
            Log.d(TAG, "Success " + opr.getInformation());
            if (mProgressDialogFragment != null) {
                mProgressDialogFragment.dismiss();
                mProgressDialogFragment = null;
            }
            removeFragment();
        }
    }
}
