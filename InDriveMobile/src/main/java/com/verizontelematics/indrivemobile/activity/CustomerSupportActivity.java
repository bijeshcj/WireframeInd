package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

public class CustomerSupportActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        setupUI();
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_cus));
    }

    private void setupUI() {
        setupHeaderView();
        TextView supportCenterPhoneNumberTV = (TextView) findViewById(R.id.supportCenterDetailTV);
        supportCenterPhoneNumberTV.setText(Html.fromHtml(" <a href='tel:" + getString(R.string.support_center_number) + "'>"
                                + getString(R.string.support_center_number) + "</a> " ));
        supportCenterPhoneNumberTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView accountCenterPhoneNumberTV = (TextView) findViewById(R.id.accountCenterDetailTV);
        accountCenterPhoneNumberTV.setText(Html.fromHtml(" <a href='tel:" + getString(R.string.account_center_number) + "'>"
                + getString(R.string.account_center_number) + "</a> " ));
        accountCenterPhoneNumberTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView stolenVehicleNumberTV = (TextView) findViewById(R.id.stolenVehicleDetailTV);
        stolenVehicleNumberTV.setText(Html.fromHtml(" <a href='tel:" + getString(R.string.stolen_vehicle_number) + "'>"
                + getString(R.string.stolen_vehicle_number) + "</a> " ));
        stolenVehicleNumberTV.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
        headerRL.setBackgroundColor(this.getResources().getColor(R.color.gray));
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.customer_support));
        headerBtn.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_support, menu);
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
