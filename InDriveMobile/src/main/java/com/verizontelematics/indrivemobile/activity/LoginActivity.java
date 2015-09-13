    package com.verizontelematics.indrivemobile.activity;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.WindowManager;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.CompoundButton;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.analytics.GoogleAnalytics;
    import com.google.android.gms.analytics.HitBuilders;
    import com.google.android.gms.analytics.Tracker;
    import com.verizontelematics.indrivemobile.IndriveApplication;
    import com.verizontelematics.indrivemobile.R;
    import com.verizontelematics.indrivemobile.animations.DemoAnimationActivity;
    import com.verizontelematics.indrivemobile.controllers.AppController;
    import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
    import com.verizontelematics.indrivemobile.controllers.ControllerUtils;
    import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
    import com.verizontelematics.indrivemobile.controllers.UIInterface;
    import com.verizontelematics.indrivemobile.cryptography.Algorithams;
    import com.verizontelematics.indrivemobile.cryptography.CryptoManager;
    import com.verizontelematics.indrivemobile.customViews.dialogs.CustomProgressDialog;
    import com.verizontelematics.indrivemobile.database.IndriveDBHelper;
    import com.verizontelematics.indrivemobile.models.Operation;
    import com.verizontelematics.indrivemobile.models.data.UserRegistrationData;
    import com.verizontelematics.indrivemobile.utils.BasicUtil;
    import com.verizontelematics.indrivemobile.utils.FileUtils;
    import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;
    import com.verizontelematics.indrivemobile.utils.config.Config;
    import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;
    import com.verizontelematics.indrivemobile.utils.phone.CryptoWrapper;


    public class LoginActivity extends Activity implements UIInterface{
    private static final int GET_ACCOUNT_ID = 0;
    public static final String ACCOUNT_ID = "AccountId";
    private static final String TAG = "LoginActivity";
    private EditText userNameET;
    private EditText passwordET;
    private CheckBox rememberMe;
    private CheckBox mCbStayLoggedMe;
    private CustomProgressDialog mProgressDialog;
    private UserRegistrationData mUserRegistrationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(), getResources().getString(R.string.screen_login));
        userNameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
//        Button scanBtn = (Button) findViewById(R.id.scanBTN);
        Button loginBtn = (Button) this.findViewById(R.id.loginBTN);
        rememberMe = (CheckBox) findViewById(R.id.rememberCB);
        mCbStayLoggedMe = (CheckBox) findViewById(R.id.stayLoggedInCB);
        /*scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "SCAN_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(LoginActivity.this, "No Application found to scan", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = null;
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_access_app),
                        getResources().getString(R.string.label_login));
                if (rememberMe.isChecked()) {

                    if(userNameET.getText().toString().equals(AppController.instance().getMobileUserId())) {

                        AppController.instance().setMobileUserId(AppController.instance().getMobileUserId());
                    }else{

                        if(userNameET.getText().toString().contains("...")){

                            AppController.instance().setMobileUserId(AppController.instance().getMobileUserId());
                        }else {

                            AppController.instance().setMobileUserId(userNameET.getText().toString());
                        }
                    }
                    username = AppController.instance().getMobileUserId();
                }else {

                    username = userNameET.getText().toString();
                }
                String password = passwordET.getText().toString();
                if (username.isEmpty() && password.isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.login), getResources().getString(R.string.un_pwd_required));
                    return;
                }
                if (username.isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.login), getResources().getString(R.string.username_required));
                    return;
                }
                if (password.isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.login), getResources().getString(R.string.password_required));
                    return;
                }
//                if (username.length() < 6) {
//                    showAlertDialog(getResources().getString(R.string.login), getResources().getString(R.string.username_validation));
//                    return;
//                }
//
//                if (password.length() < 6) {
//                    showAlertDialog(getResources().getString(R.string.login), getResources().getString(R.string.password_validation));
//                    return;
//                }

//                String cert = InDrivePreference.getInstance().getStringData("cert", "");
//                Log.d(TAG, "cert " + cert);

//                CryptoManager cryptoManager = new CryptoManager(Algorithams.AES);
//                cryptoManager.process(cert);

                mUserRegistrationData = AuthenticateController.instance().getUserRegistrationData();

                mUserRegistrationData.setMobileUserID(username);
                mUserRegistrationData.setPassword(password);
                // stubbed code for timebeing as layer 7 cert mechanisam not integrated.
//
                if ((mUserRegistrationData != null) && !AppController.instance().checkAlreadyRegistered(IndriveApplication.getInstance(),mUserRegistrationData.getMobileUserID())) {

                    Intent intent = new Intent(LoginActivity.this, NewDeviceLoginActivity.class);

                    AppController.instance().setMobileUserId(username);
                    AppController.instance().setPassword(password);
                    startActivity(intent);
                    finish();
                    return;
                }

//                reload here if different user

                if(!userNameET.getText().toString().trim().equals(AppController.instance().getMobileUserId())){
                    AppController.instance().setMobileUserId(userNameET.getText().toString().trim());
                    IndriveApplication.getInstance().reloadCertificate("");
                }

                AuthenticateController.instance().authenticate(LoginActivity.this, username, password);

            }
        });


        TextView forgotUsernameTv = (TextView) this.findViewById(R.id.forgotUsernameTV);
        TextView forgotPasswordTv = (TextView) this.findViewById(R.id.forgotPasswordTV);
//        TextView registerAccountTv = (TextView) this.findViewById(R.id.registerAccountTV);

        forgotUsernameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotUsernameIntent = new Intent(LoginActivity.this, ForgotUsernameActivity.class);
                startActivity(forgotUsernameIntent);

            }
        });

        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);

            }
        });

        Button accountSetupBtn = (Button) findViewById(R.id.accountSetupBtn);
        accountSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_access_app),
                        getResources().getString(R.string.label_mobile_app_setup));
                Intent intent = new Intent(LoginActivity.this, AccountSetupActivity.class);
                startActivity(intent);
            }
        });

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    if(userNameET.getText().toString().trim().length() > 0){
                        userNameET.setText("");
                    }
                }
            }
        });

       /* registerAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerAccountIntent = new Intent(LoginActivity.this, RegisterAccountActivity.class);
                startActivity(registerAccountIntent);

            }
        });

        ImageView goToAccountSelection = (ImageView) findViewById(R.id.go_to_accounts);
        goToAccountSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the account selector activity.
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, AccountSelectorActivity.class);
                startActivityForResult(intent, GET_ACCOUNT_ID);
            }
        });*/
        Config.getInstance(getApplicationContext());
        initDB();
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



    private void cleanup() {
        AuthenticateController.instance().unregister(this);
    }

    private void setup() {
        AuthenticateController.instance().register(this);
        populateUI();

    }

    private void populateUI() {
        // update remember me
        rememberMe.setChecked(AppController.instance().getRememberMe());
        // Fill Account Id
        if (rememberMe.isChecked()) {
            userNameET.setText("" + BasicUtil.maskText(AppController.instance().getMobileUserId()));
            // focus to password
            if(passwordET.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
        // update stay logged
        mCbStayLoggedMe.setChecked(AppController.instance().getStayLoggedSetting());

    }

    public void onDemoClicked(View view) {
        startActivity(new Intent(this, DemoAnimationActivity.class));
    }
//commenting the below code with respect to warning, as the object is not getting used anywhere
    //getting interpreted as a dead code

    private void initDB() {
        IndriveDBHelper dhHelper = new IndriveDBHelper(this);
    }

//    private void initRolesAndResponsibility() {
//        UserFactory.getInstance(this);
//        UserType userType = UserFactory.getUserType();
//
//    }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (GET_ACCOUNT_ID): {
                if (resultCode == Activity.RESULT_OK) {
                    String accountId = data.getStringExtra(ACCOUNT_ID);
                    userNameET.setText(accountId);
                }
                if (resultCode == Activity.RESULT_OK) {
                    // Handle successful scan
                    String contents = data.getStringExtra("SCAN_RESULT");
                    String formatName = data.getStringExtra("SCAN_RESULT_FORMAT");
                    byte[] rawBytes = data.getByteArrayExtra("SCAN_RESULT_BYTES");
                    int intentOrientation = data.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                    Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null : intentOrientation;
                    String errorCorrectionLevel = data.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
                    //Toast.makeText(getApplicationContext(), formatName, Toast.LENGTH_SHORT);
                }
                break;
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }




        @Override
        protected void onStart() {
            super.onStart();
            GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStart(this);
        }

        @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStop(this);
    }

    @Override
    public void onProgress(Operation opr) {
        if (opr.getId() == Operation.OperationCode.AUTHENTICATE.ordinal()) {
            if (mProgressDialog == null) {
                mProgressDialog = new CustomProgressDialog(this, "");
            }
            mProgressDialog.show();
        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr.getId() == Operation.OperationCode.AUTHENTICATE.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
//            Toast.makeText(this, "Invalid Credentials ", Toast.LENGTH_SHORT).show();
//            stub as authenitcate service is not working.
//            login();
//            stub ened.
        }
    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr.getId() == Operation.OperationCode.AUTHENTICATE.ordinal()) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            login();
        }
    }

    private void login() {
        Intent termsIntent = new Intent(LoginActivity.this, TermsConditionsActivity.class);
                Intent dashBoardIntent = new Intent(LoginActivity.this, MenuHomeActivity.class);
                if (!rememberMe.isChecked()) {
                    // delete this account.
                } else {
                    // Add this account .. check that the account is not added again.
                }
                AppController.instance().setRememberMe(rememberMe.isChecked());
                AppController.instance().setStayLoggedSetting(mCbStayLoggedMe.isChecked());
                if (InDrivePreference.getInstance().getBooleanData("login", false)) {

                    try {

                        if(rememberMe.isChecked()){

                            AppController.instance().setMobileUserId(AppController.instance().getMobileUserId());
//                            AppController.instance().setMobileUserId(userNameET.getText().toString());fdg
                        }else {

                            AppController.instance().setMobileUserId(userNameET.getText().toString());
                        }
                        ControllerUtils.refreshControllerCache(DiagnosticController.instance());

                    } catch (NumberFormatException nex) {
                        ControllerUtils.refreshControllerCache(DiagnosticController.instance());
                        // stubbed.
                        AppController.instance().setMobileUserId("300952309");
                    }
                    startActivity(dashBoardIntent);
//                    initRolesAndResponsibility();
                } else {

                    if(rememberMe.isChecked()){
                        if(userNameET.getText().toString().equals(AppController.instance().getMobileUserId())) {

                            AppController.instance().setMobileUserId(AppController.instance().getMobileUserId());
                        }else{

                            if(userNameET.getText().toString().contains("...")){

                                AppController.instance().setMobileUserId(AppController.instance().getMobileUserId());
                            }else {

                                AppController.instance().setMobileUserId(userNameET.getText().toString());
                            }
                        }
                    }else {

                        AppController.instance().setMobileUserId(userNameET.getText().toString());
                    }
                    ControllerUtils.refreshControllerCache(DiagnosticController.instance());
                    startActivity(dashBoardIntent);
                    InDrivePreference.getInstance().setBooleanData("login", true);
                }
        finish();
    }
}
