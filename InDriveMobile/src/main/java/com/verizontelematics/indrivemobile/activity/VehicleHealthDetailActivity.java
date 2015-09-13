package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.models.data.VehiclePartData;

public class VehicleHealthDetailActivity extends Activity {

    private String mPartData;
    private String mPartStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_health_detail);
        parseArguments();
        setupUI();
    }

    private void parseArguments() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            if ((arguments.containsKey("title")) && (arguments.getString("title") != null)) {
                mPartData = arguments.getString("title");
            }
            if ((arguments.containsKey("status")) && (arguments.getString("status") != null)) {
                mPartStatus = arguments.getString("status");
            }
        }
    }

    private void setupUI() {
        setupHeaderView();
        TextView mBodyPart = (TextView) this.findViewById(R.id.diagnosticsSummaryTV);
        mBodyPart.setText(mPartData);
        TextView mPartStatusTV = (TextView) this.findViewById(R.id.diagnosticsStatus);
        if (mPartStatus.equals(VehiclePartData.STATUS_ERROR)) {
            mPartStatusTV.setTextColor(getResources().getColor(R.color.failure_red));
            mPartStatusTV.setText(mPartStatus);
        } else if (mPartStatus.equals(VehiclePartData.STATUS_WARNING)) {
            mPartStatusTV.setTextColor(getResources().getColor(R.color.sc_yellowLightFill));
            mPartStatusTV.setText(VehiclePartData.TEXT_WARNING);
        } else {
            mPartStatusTV.setTextColor(getResources().getColor(R.color.success_green));
            mPartStatusTV.setText(VehiclePartData.TEXT_OK);
        }
        ImageView mInfoIV = (ImageView) this.findViewById(R.id.infoIcon);
        mInfoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(VehicleHealthDetailActivity.this,getResources().getString(R.string.dtc_info));
                dialog.setTitle("DTC Code");
                dialog.show();
            }
        });
    }

    private void setupHeaderView() {
        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
        headerRL.setBackgroundResource(R.drawable.diagnostic_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        headerTitleTV.setText(this.getResources().getString(R.string.diagnostic_details));
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerBtn.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vehicle_health_detail, menu);
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
