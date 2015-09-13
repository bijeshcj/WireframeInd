package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.AlertsController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.models.AlertsDataModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.Alert;
import com.verizontelematics.indrivemobile.models.data.DiagnosticAlert;
import com.verizontelematics.indrivemobile.userprofile.UserRoleConstants;
import com.verizontelematics.indrivemobile.userprofile.utils.UserUtils;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Priyanga on 8/25/2014.
 */
public class DiagnosticsAlertFragment extends BaseSubUIFragment implements UIInterface, Observer,UserRoleConstants {


    private static final String TAG = "DiagnosticsAlertFragment";
    private ImageView onState;
    private ImageView offState;
    private DiagnosticAlert mDiagnosticAlert;
    private boolean mSwitchOn = false;
    private CustomProgressDialog mCustomProgressDialog = null;
    //    private ProgressDialogFragment mProgressDialog = null;

    public DiagnosticsAlertFragment(){
//        Empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diagnostics_alert,container,false);
        ImageView infoView = (ImageView) rootView.findViewById(R.id.btn_info_speed_alert);

        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_help),
                        "DiagnosticsAlertsHelp");
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(getActivity(), getActivity().getResources().getString(R.string.info_diagnostics_alerts));
                dialog.setTitle(getResources().getString(R.string.about_diagnostics_alerts));
                dialog.show();
            }
        });

        RelativeLayout switchLayout = (RelativeLayout) rootView.findViewById(R.id.layoutSwitch);
        onState= (ImageView)rootView.findViewById(R.id.onState);
        offState=(ImageView)rootView.findViewById(R.id.offState);
        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_alerts),
                        "DiagnosticsAlertsOnOff");
                if(UserUtils.isUserInactive(getActivity(),inactiveMessage)){
                    return;
                }
                if (offState.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onState.setVisibility(View.VISIBLE);
                    offState.setVisibility(View.INVISIBLE);
                    mSwitchOn = true;
                } else if (offState.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onState.setVisibility(View.INVISIBLE);
                    offState.setVisibility(View.VISIBLE);
                    mSwitchOn = false;
                }
                onSwitchStateChange();
            }
        });
        populateUI();
        AlertsController.instance().register(this);
        AlertsController.instance().getAlertsDataModel().addObserver(this);
        rootView.findViewById(R.id.diagnosticsAlertHeaderRL).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return rootView;
    }

    private void populateUI() {
        AlertsDataModel mAlertsDataModel = (AlertsDataModel) AlertsController.instance().getAlertsDataModel();
        Alert alert = (Alert) mAlertsDataModel.getData();
        List<DiagnosticAlert> lstDiagnosticAlert = null;
        if (alert != null)
            lstDiagnosticAlert = alert.getDiagnosticAlert();
        if (lstDiagnosticAlert != null && !lstDiagnosticAlert.isEmpty()) {
            mDiagnosticAlert = lstDiagnosticAlert.get(0);
        }
        populateUI(mDiagnosticAlert);
    }
    private void populateUI(DiagnosticAlert mDiagnosticAlert) {
        setActive(mDiagnosticAlert != null && mDiagnosticAlert.getStatus().equalsIgnoreCase("Active"));
    }

    private void setup() {
        // setup title.
    }

    private void onSwitchStateChange() {
        if (mDiagnosticAlert != null) {
            mDiagnosticAlert.setStatus(mSwitchOn ? "Active" : "InActive");

            ArrayList<DiagnosticAlert> diagnosticAlerts = new ArrayList<DiagnosticAlert>();
            diagnosticAlerts.add(mDiagnosticAlert);
            AlertsController.instance().updateDiagnosticAlerts(getActivity(), diagnosticAlerts);
        }
        else {
            Toast.makeText(getActivity(), "Cannot Update : Network error ", Toast.LENGTH_SHORT).show();
            revertUI();
        }
    }

    public void setActive(boolean status) {
        if(status)
        {
            //turning the state as ON
            onState.setVisibility(View.VISIBLE);
            offState.setVisibility(View.INVISIBLE);
            mSwitchOn = true;
        }
        else
        {
            //turning the state as OFF
            onState.setVisibility(View.INVISIBLE);
            offState.setVisibility(View.VISIBLE);
            mSwitchOn = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getStringArray(R.array.alerts_array)[4]);
    }
    
    @Override
    public void onSubFragmentResume() {
        super.onSubFragmentResume();
    }

    @Override
    public void onSubFragmentPause() {
        super.onSubFragmentPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanup();
    }

    private void cleanup() {
        // un register with controller.
        AlertsController.instance().unregister(this);
        AlertsController.instance().getAlertsDataModel().deleteObserver(this);
    }

    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.UPDATE_DIAGNOSTIC_ALERTS.ordinal()) {

            showProgressDialog("");
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.UPDATE_DIAGNOSTIC_ALERTS.ordinal()) {
            dismissProgressDialog();
            Toast lToast = Toast.makeText(getActivity(), getResources().getString(R.string.error_occurred), Toast.LENGTH_SHORT);
            lToast.show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    revertUI();
                }
            });

        }
    }

    private void revertUI() {
        mSwitchOn = !mSwitchOn;
        if (mDiagnosticAlert != null) {
            mDiagnosticAlert.setStatus(mSwitchOn ? "Active" : "InActive");
        }
        populateUI(mDiagnosticAlert);
    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.UPDATE_DIAGNOSTIC_ALERTS.ordinal()) {
            dismissProgressDialog();
        }
    }

    private void showProgressDialog(String aMessage) {


        if (mCustomProgressDialog == null) {
            mCustomProgressDialog = new CustomProgressDialog(getActivity(), aMessage);
            mCustomProgressDialog.show();
        }

    }

    public void dismissProgressDialog() {
        if (mCustomProgressDialog != null) {
            mCustomProgressDialog.dismiss();
            mCustomProgressDialog = null;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (AlertsDataModel.class.isInstance(observable)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populateUI();
                }
            });
        }
    }
}
