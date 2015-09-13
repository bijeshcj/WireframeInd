package com.verizontelematics.indrivemobile.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.utils.ui.BaseSubUIListFragment;

import java.util.Calendar;
import java.util.List;

/**
 * Created by z684985 on 12/4/14.
 */
public class DrivingDataSelectTimePeriodFragment extends BaseSubUIListFragment implements HomeActivity.CustomTopBarItemsClickListener {
    private List<String> mTimePeriods;
    private TextView txtViewStartDate,txtViewEndDate;
    private String labelDateLayoutClicked ;

    public interface OnDismissListener {
        public void onDestroy();
        public void onDetach();
    }

    public interface OnItemSelected {
        public void onItemSelected(int position, String value);
    }
    OnItemSelected mListener;

    public static DrivingDataSelectTimePeriodFragment createInstance(List<String> data,BaseUIScreenFragment homeFragment ) {
        DrivingDataSelectTimePeriodFragment fragmentObject = new DrivingDataSelectTimePeriodFragment();
        fragmentObject.setHomeFragment(homeFragment);
        fragmentObject.mTimePeriods =  data;
        return fragmentObject;
    }

    public DrivingDataSelectTimePeriodFragment()
    {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.title_activity_select_time_period), true,false,true,false);
        ((HomeActivity)getActivity()).hideSlidingMenus();
        CustomOptionListAdapter adapter = new CustomOptionListAdapter(inflater.getContext(),R.layout.driving_data_time_period,mTimePeriods);
        setListAdapter(adapter);

        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(Color.WHITE);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, container.getHeight()));

        ((HomeActivity)getActivity()).addToDoneButtonClickHandlers(this);
        return v;
    }

    public void setOnItemSelectedListener(OnItemSelected listener) {
        mListener = listener;
    }

    public void setOnDismissListener(OnDismissListener destroyListener) {
        OnDismissListener mDismissListener = destroyListener;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // position 4 is mapped with the custom view
        //will close with the done button click
        if(position==4)
        {
            LinearLayout customTimeSelection = (LinearLayout)v.findViewById(R.id.customTimePeriodSelection);
            customTimeSelection.setVisibility(View.VISIBLE);
            RelativeLayout layoutStartDate = (RelativeLayout)v.findViewById(R.id.layoutStartDate);
            RelativeLayout layoutEndDate = (RelativeLayout)v.findViewById(R.id.layoutEndDate);

           // txtViewStartDate= (TextView)v.findViewById(R.id.txtViewstartDateValue);
            //txtViewEndDate = (TextView)v.findViewById(R.id.txtViewendDateValue);
            layoutEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker();
                    labelDateLayoutClicked = "endDateLabel";
                }
            });
            layoutStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker();
                    labelDateLayoutClicked = "startDateLabel";
                }
            });
        }

        else
        {
            //will write the logic for setting the selected value or how to return
            //calling dismiss as of now to show the previous screen
            dismiss();
        }


    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putLong("minDate", calender.getTimeInMillis());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(dateListener);
        date.show(getFragmentManager(), "Date Picker");
    }
    //listener upon dateSelection
    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            Log.v("YEAR : Month: day", year + "  :  " + monthOfYear + "  :  " + dayOfMonth);
            if(labelDateLayoutClicked.equalsIgnoreCase("startDateLabel"))
                 txtViewStartDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
            else if(labelDateLayoutClicked.equalsIgnoreCase("endDateLabel"))
                txtViewEndDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
            Calendar dateSelectedCalender = Calendar.getInstance();
            dateSelectedCalender.set(Calendar.YEAR, year);
            dateSelectedCalender.set(Calendar.MONTH, monthOfYear);
            dateSelectedCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        }
    };

    public void show(FragmentManager fm, int container) {
        FragmentManager mFragmentManager = fm;
        if (mFragmentManager == null) {
            mFragmentManager = getFragmentManager();
        }
        android.support.v4.app.FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        ft.addToBackStack("DrivingDataSelectTimePeriodFragment");
        ft.replace(container, this,"DrivingDataSelectTimePeriodFragment");
        ft.commit();

        getHomeFragment().pushFragmentStack("DrivingDataSelectTimePeriodFragment");

    }
    public void dismiss() {
        getHomeFragment().popFragmentStack();

    }
    @Override
    public void onTopBarItemClick(View aView) {
        if(aView.getId()== HomeActivity.DONE_BUTTON_ID)
        {
            //write the logic for setting the values and then call the dismiss
            dismiss();
        }
    }

    //Adapter class for the layout
    private class CustomOptionListAdapter extends ArrayAdapter<String>{
        private final Context context;
        private final List<String> lstTimePeriods;

        public CustomOptionListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.lstTimePeriods = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.driving_data_time_period, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.txtViewTImePeriod);
            textView.setText(lstTimePeriods.get(position));
            LinearLayout customTimeSelection = (LinearLayout)rowView.findViewById(R.id.customTimePeriodSelection);
            customTimeSelection.setVisibility(View.GONE);
            return rowView;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity)getActivity()).showSlidingMenus();
        ((HomeActivity)getActivity()).setTitle(getActivity().getResources().getStringArray(R.array.module_array)[2]);
        ((HomeActivity)getActivity()).setCustomActionBarView("", false,false,true,false);

    }
}
