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
import android.widget.ImageView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.AppController;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.UserRegistrationData;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;
import com.verizontelematics.indrivemobile.utils.ui.ValidatorUtil;

public class AccountSetupUNPWDActivity extends Activity implements UIInterface{

    private static final String TAG = AccountSetupUNPWDActivity.class.getCanonicalName();
    private EditText authorizationCodeET;
    private EditText usernameET;
    private EditText passwordET;
    private EditText confirmPasswordET;
    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup_unpwd);

        authorizationCodeET = (EditText) findViewById(R.id.authorizationCodeET);
        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.pwdET);
        confirmPasswordET = (EditText) findViewById(R.id.confirmPwdET);


        ImageView infoIV = (ImageView)findViewById(R.id.infoIV);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_access_app),
                        getResources().getString(R.string.label_submit_name_pass_help));
                //To decide the text to be displayed
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(AccountSetupUNPWDActivity.this, getResources().getString(R.string.standard_login_process_info));
                dialog.setTitle(getResources().getString(R.string.standard_login_process));
                dialog.show();
            }
        });

        Button submitBtn = (Button) findViewById(R.id.submitBTN);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_access_app),
                        getResources().getString(R.string.label_submit_name_pass));
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPasswordET.getText().toString();

                if (authorizationCodeET.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.account_setup), getResources().getString(R.string.authorization_code_required));
                    return;
                }
                if (username.isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.account_setup), getResources().getString(R.string.username_required));
                    return;
                }
                if (password.isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.account_setup), getResources().getString(R.string.password_required));
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.account_setup), getResources().getString(R.string.confirmation_password_required));
                    return;
                }

                if (username.length() < 6) {
                    showAlertDialog(getResources().getString(R.string.prompt_username), getResources().getString(R.string.username_validation));
                    return;
                }
//
                if (password.length() < 10) {
                    showAlertDialog(getResources().getString(R.string.prompt_password), getResources().getString(R.string.password_validation));
                    return;
                }

                if(ValidatorUtil.hasSpecialCharsInPassword(password)){
                    showAlertDialog(getResources().getString(R.string.prompt_password), getResources().getString(R.string.password_validation));
                    return;
                }

                if(!confirmPassword.equals(password))
                {
                    showAlertDialog(getResources().getString(R.string.prompt_password), getResources().getString(R.string.pwd_confirm_validation));
                    return;
                }

                //AuthenticateController.instance().getCerId(AccountSetupUNPWDActivity.this);
                UserRegistrationData mUserRegistrationData = AuthenticateController.instance().getUserRegistrationData();
                mUserRegistrationData.setMobileUserID(username);
                mUserRegistrationData.setPassword(password);
                mUserRegistrationData.setToken(authorizationCodeET.getText().toString());
                mUserRegistrationData.setTokenType(AuthenticateController.AUTH_DEVICE_AND_USER);

                AppController.instance().setMobileUserId(username);

                AuthenticateController.instance().validateRegistration(AccountSetupUNPWDActivity.this, mUserRegistrationData);



            }
        });



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
    public void onResume() {
        super.onResume();
        setup();
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanup();
    }


    private void setup() {
        AuthenticateController.instance().register(this);
    }

    private void cleanup() {
        AuthenticateController.instance().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_setup_unpwd, menu);
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
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()) {
            if (mProgressDialog == null) {
                mProgressDialog = new CustomProgressDialog(this, "");
            }
            mProgressDialog.show();
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
//            Toast.makeText(this,getMessage(opr.getInformation()) , Toast.LENGTH_SHORT).show();
            promptUser(getMessage(opr.getInformation()));
        }
    }

    private String getMessage(String operationInformation){
        if(operationInformation.equalsIgnoreCase("1414"))
            return getResources().getString(R.string.invalid_auth_code);
        else if(operationInformation.equalsIgnoreCase("1412")){
            return getResources().getString(R.string.user_already_exists);
        }
        else
            return "Validate registration error : "+operationInformation;
    }

    private void promptUser(String message){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onSuccess(Operation opr) {

        if (opr.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            Intent intent = new Intent(AccountSetupUNPWDActivity.this, AccountSetupResultActivity.class);
            startActivity(intent);
        }

    }
}
