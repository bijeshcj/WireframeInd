package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;


public class TermsConditionsActivity extends Activity {
    private CheckBox privacyPolicyCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        privacyPolicyCB = (CheckBox) findViewById(R.id.policyCB);


        Button submitBtn = (Button) findViewById(R.id.submitBTN);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!privacyPolicyCB.isChecked()) {
                    showAlertDialog(getResources().getString(R.string.privacy_policy), getResources().getString(R.string.privacy_policy_validation));
                    return;
                }
                Intent dashBoardActivity = new Intent(TermsConditionsActivity.this, MenuHomeActivity.class);
                startActivity(dashBoardActivity);
                finish();

            }
        });

       /* Button agreeBtn = (Button) this.findViewById(R.id.agreeBTN);
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent dashBoardActivity = new Intent(TermsConditionsActivity.this, MenuHomeActivity.class);
                startActivity(dashBoardActivity);
                finish();
            }
        });

        Button cancelBtn = (Button) this.findViewById(R.id.cancelBTN);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent loginIntent = new Intent(TermsConditionsActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });*/

        TextView policyTV = (TextView) findViewById(R.id.privacyPolicyTV);
        TextView customerAgreementTV = (TextView) findViewById(R.id.customerAgreementTV);
        policyTV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        customerAgreementTV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.terms_conditions, menu);
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

}
