package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;



import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

public class AccountAuthorizationResultActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_authorization_result);

        Button continueBtn = (Button) findViewById(R.id.continueBTN);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_access_app),
                        getResources().getString(R.string.label_sync_continue));
                Intent intent = new Intent(AccountAuthorizationResultActivity.this, AccountSetupUNPWDActivity.class);
                startActivity(intent);
            }
        });

        ImageView infoIV = (ImageView)findViewById(R.id.infoIV);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To decide the text to be displayed
                ToBeDecidedDialog dialog = new ToBeDecidedDialog(AccountAuthorizationResultActivity.this, getResources().getString(R.string.authorization_sent_infoText));
                dialog.setTitle("Authorization Code");
                dialog.show();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_authorization_result, menu);
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
