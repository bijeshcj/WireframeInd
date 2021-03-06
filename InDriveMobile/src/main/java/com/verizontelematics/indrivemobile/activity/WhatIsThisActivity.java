package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.constants.UIConstants;

public class WhatIsThisActivity extends Activity {
    private ImageView helpIV;
    private TextView helpTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.activity_what_is_this);
        setupUI();
        parseArguments();


    }

    private void setupUI() {

        helpIV = (ImageView) findViewById(R.id.helpIV);
        helpTV = (TextView) findViewById(R.id.descriptionTV);
    }

    private void parseArguments() {


        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            if (arguments.containsKey(UIConstants.HELP_SCREEN)) {
                String screenName = arguments.getString(UIConstants.HELP_SCREEN);
                if (screenName.equalsIgnoreCase(UIConstants.VIN_NUMBER_HELP)) {

                    helpTV.setText(getResources().getString(R.string.vin_help));
                    helpIV.setImageResource(R.drawable.vin_popup);

                } else {

                    helpTV.setText(getResources().getString(R.string.serial_number_help));
                    helpIV.setImageResource(R.drawable.serial_number_pop_up);
                }
            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_what_is_this, menu);
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
