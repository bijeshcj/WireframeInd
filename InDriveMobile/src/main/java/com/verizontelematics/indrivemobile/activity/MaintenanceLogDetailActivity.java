package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.models.data.MaintenanceLogData;
import com.verizontelematics.indrivemobile.userprofile.UserRoleConstants;
import com.verizontelematics.indrivemobile.userprofile.utils.UserUtils;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;
import com.verizontelematics.indrivemobile.utils.ui.CurrencyFormat;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;

public class MaintenanceLogDetailActivity extends Activity {
    private static final String TAG = MaintenanceLogDetailActivity.class.getSimpleName();
    private View mHeaderView;
    private String mTitle;
    private MaintenanceLogData mMaintenanceLogData;
    private static final int FINISH_DETAIL_ACTIVITY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_log_detail);
        parseArguments();
        setupUI();
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),"Edit Maintenacne Log");
    }

    private void parseArguments() {

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            if ((arguments.containsKey("selected_data")) && (arguments.getParcelable("selected_data") != null)) {
                mMaintenanceLogData = arguments.getParcelable("selected_data");
            }
        }
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
        headerRL.setBackgroundResource(R.drawable.diagnostic_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.maintenance_log_detailed));
        headerBtn.setText(this.getResources().getString(R.string.edit));
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_diagnostics),
                        "DiagnosticsEditLog");
                if(UserUtils.isUserInactive(MaintenanceLogDetailActivity.this, UserRoleConstants.inactiveMessage)){
                    return;
                }
                onEditLog();
            }
        });
    }

    private void setupUI() {

        if (mMaintenanceLogData == null)
            return;

        setupHeaderView();

        ImageView infoIV = (ImageView)findViewById(R.id.btn_info);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToBeDecidedDialog dialog = new ToBeDecidedDialog(MaintenanceLogDetailActivity.this,getResources().getString(R.string.info_logs));
                dialog.setTitle(getResources().getString(R.string.about_logs));
                dialog.show();

            }
        });

        TextView mDetailedView = (TextView) this.findViewById(R.id.txt_log_detailed);
        TextView mTitleView = (TextView) this.findViewById(R.id.txt_title);
        TextView mTitleCustom = (TextView) this.findViewById(R.id.txt_title_custom);
        String customText = getResources().getString(R.string.custom);
        if (mMaintenanceLogData.getServiceType().equalsIgnoreCase(customText)) {
            mTitleView.setText(customText);
            mTitleCustom.setVisibility(View.VISIBLE);
            mTitleCustom.setText(mMaintenanceLogData.getServiceName());
        } else {
            mTitleCustom.setVisibility(View.GONE);
            mTitleView.setText(mMaintenanceLogData.getServiceName());
        }
        
        String locationText = mMaintenanceLogData.getServiceCenter() == null ? "" : mMaintenanceLogData.getServiceCenter();
        String descText = mMaintenanceLogData.getDescription() == null ? "" : mMaintenanceLogData.getDescription();
        mDetailedView.setText(Html.fromHtml("<b>" + getResources().getString(R.string.label_location) + " : </b>" + locationText + "<br><br>"
                        + "<b>" + getResources().getString(R.string.description) + " : </b>" + descText + "<br><br>"
                        + "<b>" + getResources().getString(R.string.cost) + " : </b>" + CurrencyFormat.applyCurrencyFormat1(mMaintenanceLogData.getCost()) + "<br><br>"
                        + "<b>" + getResources().getString(R.string.date) + " : </b>" + DateDataFormat.convertMillisAsDateString(this, mMaintenanceLogData.getServiceDate(), true) + "<br><br>"
                        + "<b>" + getResources().getString(R.string.mileage) + " : </b>" + mMaintenanceLogData.getMileage() + "<br><br>"
        ));


    }

    private void onEditLog() {

        Intent createLogIntent = new Intent(this, CreateMaintenanceLogActivity.class);
        createLogIntent.putExtra("edit_mode", true);
        createLogIntent.putExtra("selected_data", mMaintenanceLogData);
        startActivityForResult(createLogIntent, FINISH_DETAIL_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == FINISH_DETAIL_ACTIVITY) && (resultCode == RESULT_OK)) {

            // parse input data
            if (data == null || data.getAction() == null) {
                return;
            }
            if (data.getAction().equals("updated")) {
                if (data.getExtras() == null)
                    return;
                mMaintenanceLogData = data.getExtras().getParcelable("log_data");
                setupUI();
            } else if (data.getAction().equals("deleted")) {
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maintenance_log_detail, menu);
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
}
