package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.userprofile.UserRoleConstants;
import com.verizontelematics.indrivemobile.userprofile.utils.UpSellData;
import com.verizontelematics.indrivemobile.userprofile.utils.UserUtils;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

/**
 * Created by bijesh on 2/3/2015.
 */
public class UpSellPageActivity extends Activity implements UserRoleConstants{

    private static final String TAG = UpSellPageActivity.class.getCanonicalName();
    private LinearLayout mLinearLayout;
    private TextView mTxtHeader;
    private TextView mTxtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsellpage);
        initComponents();
        Intent intent = getIntent();
        if(intent != null)
          process(intent.getStringExtra(BUNDLE_UPSELL_PAGE_MODULE_KEY));


    }

    private void initComponents(){
        mLinearLayout = (LinearLayout) findViewById(R.id.upsellMainLayout);
        mTxtHeader = (TextView) findViewById(R.id.upsellHeader);
        mTxtMessage = (TextView) findViewById(R.id.upsellMessage);
    }

    private void setupHeaderView(int resId,String header) {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
        headerRL.setBackgroundResource(resId);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(header);
        headerBtn.setVisibility(View.GONE);


    }


    private void process(String module){
            Log.d(TAG, "Upsell Page module " + module);
            UpSellData data = UserUtils.getUpSellData(module, UpSellPageActivity.this);
            if(data != null){
                mLinearLayout.setBackgroundResource(data.getImageId());
                mTxtHeader.setText(data.getHeader());
                mTxtMessage.setText(data.getMessage());
                triggerScreenAsEvent(data.getHeader());
            }
    }

    private void triggerScreenAsEvent(String header){
        int resId = R.drawable.alerts_header_background;
        String headerTitle = this.getResources().getString(R.string.alert_settings_small);
        if(header.equals(getResources().getString(R.string.vehicle_location_services_header))){
            new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_location_upgrade));
            resId = R.drawable.location_header_background;
            headerTitle = this.getResources().getString(R.string.about_location_alerts);
        }else if(header.equals(getResources().getString(R.string.about_diagnostics))){
            new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_diag_upgrade));
        }else if(header.equals(getResources().getString(R.string.about_boundary_alerts))){
            new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_location_aler_upgr));
        }else if(header.equals(getResources().getString(R.string.about_speed_alert))){
            new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_speed_al_upg));
        }else if(header.equals(getResources().getString(R.string.about_diagnostics_alerts))){
            new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_enha_diag));
        }

        setupHeaderView(resId,headerTitle);

    }


}
