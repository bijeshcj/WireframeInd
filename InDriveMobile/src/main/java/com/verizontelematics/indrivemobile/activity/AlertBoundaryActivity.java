package com.verizontelematics.indrivemobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by bijesh on 1/7/2015.
 */
public class AlertBoundaryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_boundary);
        setupHeaderView();
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
        headerRL.setBackgroundResource(R.drawable.alerts_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.alert_settings_small));
        headerBtn.setVisibility(View.GONE);
    }

    public View getHeaderView() {
        return this.findViewById(R.id.headerRL);
    }

    public void updateHeader() {
        setupHeaderView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
//        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("LocationAlertSettingDetailedFragment");
//        ft.remove(fragment);
//        ft.commit();
        getSupportFragmentManager().popBackStack();
    }
}
