package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.UserRegistrationData;

public class NewDeviceAuthorizationActivity extends Activity implements UIInterface {
    private EditText authorizationCodeET;
    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device_authorization);

        authorizationCodeET = (EditText) findViewById(R.id.authCodeET);


        Button submitBtn = (Button) findViewById(R.id.submitBTN);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authorizationCodeET.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.device_registration), getResources().getString(R.string.authorization_code_required));
                    return;
                }
                UserRegistrationData userRegistrationData = AuthenticateController.instance().getUserRegistrationData();
                userRegistrationData.setToken(authorizationCodeET.getText().toString());
                userRegistrationData.setTokenType(AuthenticateController.AUTH_DEVICE);
                AuthenticateController.instance().validateRegistration(NewDeviceAuthorizationActivity.this, userRegistrationData);
            }
        });
        AuthenticateController.instance().register(this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_device_authorization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AuthenticateController.instance().unregister(this);
    }


    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()) {
            if (mProgressDialog == null) {
                mProgressDialog = new CustomProgressDialog(NewDeviceAuthorizationActivity.this, "");
            }
            mProgressDialog.show();
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            String message = getMessage(opr.getInformation());
            if(message != null)
             Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private String getMessage(String operationInformation){
        if(operationInformation.equalsIgnoreCase("1414"))
            return getResources().getString(R.string.invalid_auth_code);
        else
            return null;//"Validate registration error : "+operationInformation;
    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            Intent intent = new Intent(NewDeviceAuthorizationActivity.this, AccountSetupResultActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
