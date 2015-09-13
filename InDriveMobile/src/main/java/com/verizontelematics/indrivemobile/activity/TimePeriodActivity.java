package com.verizontelematics.indrivemobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.constants.UIConstants;
import com.verizontelematics.indrivemobile.customViews.CustomDatePicker;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by bijesh on 12/5/2014.
 */
public class TimePeriodActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = TimePeriodActivity.class.getSimpleName();
    private TextView mActionBarTitle;
    private Button headerBtn;
    private String[] selections;
    private LayoutInflater mLayoutInflater;
    private static final int CUSTOM = 4;
    private CustomDatePicker datePicketStartDate;
    private CustomDatePicker datePickerEndDate;
    private Long startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_period);
        setupHeaderView();
        setupUI();
    }

    private void setupUI() {
        selections = getResources().getStringArray(R.array.time_period_options);
        mLayoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListView mListView = (ListView) findViewById(R.id.lstTypes);
        TimePeriodOptionsAdapter adapter = new TimePeriodOptionsAdapter(this, R.layout.adapter_time_period, Arrays.asList(selections));
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.topView_timePeriod);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.driving_module_code));
        headerRL.setBackgroundResource(R.drawable.drivingdata_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        headerTitleTV.setText("Driving Data");
        headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerBtn.setText(this.getResources().getString(R.string.done));
        headerBtn.setVisibility(View.INVISIBLE);
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_app_navigation),
                        "DrivingDataTimePeriodDone");
                Calendar startDateCal = Calendar.getInstance();
                startDateCal.set(datePicketStartDate.getYear(),datePicketStartDate.getMonth(),datePicketStartDate.getDayOfMonth(),0,0,0);
                startDate = startDateCal.getTimeInMillis();

                Calendar endDateCal = Calendar.getInstance();
                endDateCal.set(datePickerEndDate.getYear(),datePickerEndDate.getMonth(),datePickerEndDate.getDayOfMonth(),0,0,0);
                endDate = endDateCal.getTimeInMillis();

                if (startDate > endDate) {
                    Toast.makeText(TimePeriodActivity.this, TimePeriodActivity.this.getResources().getString(R.string.validation_driving_data_date), Toast.LENGTH_SHORT).show();
                    return;
                }

                finishTimePeriodActivity(CUSTOM);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == CUSTOM) {
            showCustomDateOptions(view);
        } else {
            finishTimePeriodActivity(position);
        }
    }

    void finishTimePeriodActivity(int position) {
        Intent intent = getIntent();
        //setting the bundle values if selected option is custom
        if (position == CUSTOM) {
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
        }
        intent.putExtra(UIConstants.DRIVING_DATA_TIME_PERIOD, selections[position]);
        setResult(RESULT_OK, intent);
        finish();
    }


    //Adapter class for the layout
    private class TimePeriodOptionsAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<String> lstTimePeriods;

        public TimePeriodOptionsAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.lstTimePeriods = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == CUSTOM) {

                View rowView = mLayoutInflater.inflate(R.layout.driving_data_time_period, parent, false);
                TextView textView = (TextView) rowView.findViewById(R.id.txtViewTImePeriod);
                textView.setText(lstTimePeriods.get(position));
                LinearLayout customTimeSelection = (LinearLayout) rowView.findViewById(R.id.customTimePeriodSelection);
                customTimeSelection.setVisibility(View.INVISIBLE);
                return rowView;
            } else {
                View rowView = mLayoutInflater.inflate(R.layout.adapter_time_period, parent, false);
                TextView textView = (TextView) rowView.findViewById(R.id.timePeriodOptions);
                textView.setText(lstTimePeriods.get(position));
                return rowView;
            }
        }
    }


    private void showCustomDateOptions(View v) {

        LinearLayout customTimeSelection = (LinearLayout) v.findViewById(R.id.customTimePeriodSelection);
        customTimeSelection.setVisibility(View.VISIBLE);
        if (customTimeSelection.getVisibility() == View.VISIBLE)
            headerBtn.setVisibility(View.VISIBLE);

        datePicketStartDate = (CustomDatePicker) v.findViewById(R.id.datePickerStartDateValue);
        datePickerEndDate = (CustomDatePicker) v.findViewById(R.id.datePickerEndDateValue);

        Calendar calendar = Calendar.getInstance();
        datePicketStartDate.setMaxDate(calendar.getTimeInMillis());
        datePickerEndDate.setMaxDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, -365);
        datePicketStartDate.setMinDate(calendar.getTimeInMillis());
        datePickerEndDate.setMinDate(calendar.getTimeInMillis());
    }


}
