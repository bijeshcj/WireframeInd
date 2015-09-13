package com.verizontelematics.indrivemobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.customViews.CustomDatePicker;

import java.util.Calendar;

public class LocationHistoryPickerActivity extends FragmentActivity {
    private CustomDatePicker datePickerSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history_picker);
        setupHeaderView();
        setupUI();
    }

    private void setupUI() {

        //Max Day is current date and min date is 13 days past
        datePickerSelectedDate =(CustomDatePicker)this.findViewById(R.id.datePickerHistoryDateValue);
        Calendar calendar = Calendar.getInstance();
        datePickerSelectedDate.setMaxDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, -13);
        datePickerSelectedDate.setMinDate(calendar.getTimeInMillis());

    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerLayout);
        headerRL.setBackgroundResource(R.drawable.location_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerBtn.setText(this.getResources().getString(R.string.done));
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectedDayIntent = new Intent();
                Calendar startDateCal = Calendar.getInstance();
                startDateCal.set(datePickerSelectedDate.getYear(),datePickerSelectedDate.getMonth(),datePickerSelectedDate.getDayOfMonth(),0,0,0);
                long selectedDate = startDateCal.getTimeInMillis();
                selectedDayIntent.putExtra("SelectedDay", selectedDate);
                setResult(RESULT_OK, selectedDayIntent);
                finish();
            }
        });
        headerTitleTV.setText(this.getResources().getString(R.string.location_history));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_history_picker, menu);
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
