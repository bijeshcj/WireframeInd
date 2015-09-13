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
import android.widget.RadioButton;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.UserAccountModel;
import com.verizontelematics.indrivemobile.models.data.UserAccountData;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

import java.util.Observable;
import java.util.Observer;

public class AccountSetupSelectionActivity extends Activity implements UIInterface, Observer {
    private RadioButton textRB;
    private RadioButton emailRB;
    private EditText textET;
    private EditText emailET;
    private CustomProgressDialog mProgressDialog;
    private UserAccountData mUserAccountData;
//    private ImageView infoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup_selection);

        textET =(EditText)findViewById(R.id.textET);
        emailET =(EditText)findViewById(R.id.emailET);

        Button submitBtn = (Button)findViewById(R.id.submitBTN);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_access_app),
                        getResources().getString(R.string.label_receive_auth_code));
                if (textRB.isChecked() && (textET.getText().toString().isEmpty())) {
                    showAlertDialog("Selection Method", getResources().getString(R.string.text_required));
                    return;
                }
                if (emailRB.isChecked() && (emailET.getText().toString().isEmpty())) {
                    showAlertDialog("Selection Method", getResources().getString(R.string.email_required));
                    return;
                }
                if (emailRB.isChecked()) {
                    mUserAccountData.setPhone(null);
                }
                else {
                    mUserAccountData.setEmail(null);
                }
                // update mUserAccountData depends on user selection
                AuthenticateController.instance().sendAuthorizationToken(AccountSetupSelectionActivity.this, mUserAccountData, AuthenticateController.AUTH_DEVICE_AND_USER);
            }
        });
        textRB =(RadioButton)findViewById(R.id.textRB);
        emailRB =(RadioButton)findViewById(R.id.emailRB);

        textET.setEnabled(false);
        emailET.setEnabled(false);

        /*textET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    textRB.setChecked(true);
                    emailRB.setChecked(false);
                }

            }
        });

        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    emailRB.setChecked(true);
                    textRB.setChecked(false);
                }

            }
        });

        textRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    textET.requestFocus();
                }

            }
        });

        emailRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    emailET.requestFocus();
                }

            }
        });*/

        /*infoIV =(ImageView)findViewById(R.id.infoIV);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To decide the text to be displayed
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(AccountSetupSelectionActivity.this, "TBD");
                dialog.setTitle("TBD");
                dialog.show();

            }
        });*/
        setup();
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
        populateUI();
        AuthenticateController.instance().register(this);
        AuthenticateController.instance().getUserAccountModel().addObserver(this);
    }

    private void populateUI() {
        Object data = AuthenticateController.instance().getUserAccountModel().getData();
        if (data == null) {
            return;
        }
        if (UserAccountData.class.isInstance(data)) {
            mUserAccountData = (UserAccountData)data;
        }
        if (mUserAccountData != null) {

            emailET.setText((mUserAccountData.getEmail() != null) ? mUserAccountData.getEmail().getEmailMask() : "");
            textET.setText((mUserAccountData.getPhone() != null) ? mUserAccountData.getPhone().getPhoneNumberMask() : "");

            /*if (mUserAccountData.getPhone() != null) {
                textET.setText(mUserAccountData.getPhone().getPhoneNumberMask());
                textRB.setChecked(true);
            }
            else {
                textRB.setChecked(false);
            }
            if (mUserAccountData.getEmail() != null) {
                emailET.setText(mUserAccountData.getEmail().getEmailMask());
                emailRB.setChecked(true);
            }
            else {
                emailRB.setChecked(false);
            }*/
        }
    }

    private void cleanup() {
        AuthenticateController.instance().register(this);
        AuthenticateController.instance().getUserAccountModel().deleteObserver(this);
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

    public void radioButtonClick(View view) {
        switch (view.getId()) {
            case R.id.emailRB:
                textRB.setChecked(false);
                break;

            case R.id.textRB:
                emailRB.setChecked(false);
                break;

            default:
                break;


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_setup_selection, menu);
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
        if (opr.getId() == Operation.OperationCode.SEND_AUTH_TOKEN.ordinal()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog == null) {
                        mProgressDialog = new CustomProgressDialog(AccountSetupSelectionActivity.this, "");
                    }
                    if(!(AccountSetupSelectionActivity.this).isFinishing())
                      mProgressDialog.show();
                }
            });
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.SEND_AUTH_TOKEN.ordinal()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog != null)
                        mProgressDialog.dismiss();
                    Toast.makeText(AccountSetupSelectionActivity.this, "Error in sending authorization token ...", Toast.LENGTH_SHORT).show();
//            for testing purpose remove these two lines after testing
                    Intent intent = new Intent(AccountSetupSelectionActivity.this, AccountAuthorizationResultActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.SEND_AUTH_TOKEN.ordinal()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog != null)
                        mProgressDialog.dismiss();
                    Toast.makeText(AccountSetupSelectionActivity.this, "Authorization token send successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AccountSetupSelectionActivity.this, AccountAuthorizationResultActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (UserAccountModel.class.isInstance(observable))
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                populateUI();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }




}
