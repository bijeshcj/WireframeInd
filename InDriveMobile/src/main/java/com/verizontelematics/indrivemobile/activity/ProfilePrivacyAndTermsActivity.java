package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by bijesh on 2/1/2015.
 */
public class ProfilePrivacyAndTermsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_privacy_terms);
        setupHeaderView();
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


    }
}
