package com.verizontelematics.indrivemobile.activity;

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
public class SpeedHistoryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_history);
        setupHeaderView();
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
        headerRL.setBackgroundResource(R.drawable.alerts_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.alert_history_small));
        headerBtn.setVisibility(View.GONE);


    }

}
