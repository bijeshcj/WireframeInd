package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.AppController;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.UserRegistrationData;
import com.verizontelematics.indrivemobile.utils.ui.ValidatorUtil;

/**
 * Created by bijesh on 2/1/2015.
 */
public class ProfileContentActivity extends Activity implements UIInterface {
    private CustomProgressDialog mProgressDialog;
    private EditText oldPasswordET;
    private EditText newPasswordET;
    private EditText confirmPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_content);
        setupUI();

    }

    private void setupUI() {

        setupHeaderView();
        confirmPasswordET = (EditText) findViewById(R.id.confirmPasswordET);
        newPasswordET = (EditText) findViewById(R.id.newPasswordET);
        oldPasswordET = (EditText) findViewById(R.id.oldPasswordET);

    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
//        headerRL.setBackgroundResource(R.drawable.alerts_header_background);
        headerRL.setBackgroundColor(this.getResources().getColor(R.color.gray));
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.profile_header));
        headerBtn.setVisibility(View.VISIBLE);
        headerBtn.setText("Done");
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oldPasswordET.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.update_password), getResources().getString(R.string.old_password_required));
                    return;
                }

                if (newPasswordET.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.update_password), getResources().getString(R.string.new_password_required));
                    return;
                }

                if (confirmPasswordET.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.update_password), getResources().getString(R.string.confirmation_password_required));
                    return;
                }

                if (ValidatorUtil.hasSpecialCharsInPassword(confirmPasswordET.getText().toString())) {
                    showAlertDialog(getResources().getString(R.string.prompt_password), getResources().getString(R.string.password_validation));
                    return;
                }
                if (!AppController.instance().getPasword().equals(oldPasswordET.getText().toString()))
                {
                    Toast.makeText(ProfileContentActivity.this, getResources().getString(R.string.old_password_incorrect), Toast.LENGTH_LONG).show();
                    return;
                }
                if (confirmPasswordET.getText().toString().equals(newPasswordET.getText().toString())) {
                    UserRegistrationData mUserRegistrationData = AuthenticateController.instance().getUserRegistrationData();
                    mUserRegistrationData.setPassword(confirmPasswordET.getText().toString());
                    AuthenticateController.instance().updatePassword(ProfileContentActivity.this, mUserRegistrationData);
                } else {
                    showAlertDialog(getResources().getString(R.string.change_password), getResources().getString(R.string.pwd_confirm_validation));
                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setup();
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanup();
    }

    private void cleanup() {
        AuthenticateController.instance().unregister(this);
    }


    private void setup() {
        AuthenticateController.instance().register(this);
    }

    @Override
    public void onProgress(Operation opr) {

        if (opr.getId() == Operation.OperationCode.UPDATE_PASSWORD.ordinal()) {
            if (mProgressDialog == null) {
                mProgressDialog = new CustomProgressDialog(this, "");
            }
            mProgressDialog.show();
        }

    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.UPDATE_PASSWORD.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
//            Toast.makeText(this, getResources().getString(R.string.cannot_update_password), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.UPDATE_PASSWORD.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            Toast.makeText(this, getResources().getString(R.string.updating_password_success), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                })
                .show();
    }
}
