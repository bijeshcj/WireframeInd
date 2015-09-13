package com.verizontelematics.indrivemobile.fragments;

/**
 * Created by z684985 on 9/11/14.
 */

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.verizontelematics.indrivemobile.activity.CreateMaintenanceLogActivity;
import com.verizontelematics.indrivemobile.activity.CreateMaintenanceReminderActivity;

import java.lang.reflect.Field;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private OnDateSetListener dateListener;
    private int year, month, day;
    private String screenNameText;
    private static final String TAG = DatePickerFragment.class.getCanonicalName();

    private DatePickerDialog mDatePickerDialog;

    public DatePickerFragment() {
    }

    public void setCallBack(OnDateSetListener dateListener) {
        this.dateListener = dateListener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        screenNameText = args.getString("Screen");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDatePickerDialog = new DatePickerDialog(getActivity(), dateListener, year, month, day);

        Bundle args = getArguments();
        if (args != null) {
            long minDate = getArguments().getLong("minDate");
            long maxDate = getArguments().getLong("maxDate");
            if (minDate > 0) {
                setMinDate(minDate);
            }
            if (maxDate > 0) {
                setMaxDate(maxDate);
            }
        }
        return mDatePickerDialog;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setMinDate(long dt) {
        if (mDatePickerDialog != null) {
            mDatePickerDialog.getDatePicker().setMinDate(dt);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setMaxDate(long dt) {
        if (mDatePickerDialog != null) {
            mDatePickerDialog.getDatePicker().setMaxDate(dt);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (screenNameText.equals("LOG"))
            CreateMaintenanceLogActivity.setDateUponCancel(getSpinnerTime());
        else
            CreateMaintenanceReminderActivity.setDateUponCancel(getSpinnerTime());
    }

    private Calendar getSpinnerTime() {
        Calendar retCal = null;
        try {
            Field[] fields = mDatePickerDialog.getClass().getDeclaredFields();
            for (Field field : fields) {

                if (field.getName().equals("mDatePicker")) {
                    field.setAccessible(true);
                    android.widget.DatePicker picker = (android.widget.DatePicker) field.get(mDatePickerDialog);
                    Field[] allFields = picker.getClass().getDeclaredFields();
                    for (Field ff : allFields) {
                        if (ff.getName().equals("mCurrentDate")) {
                            ff.setAccessible(true);
                            retCal = (Calendar) ff.get(picker);
                            Log.d(TAG, "$$$ cal " + retCal);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return retCal;
    }

    public DatePickerDialog getDatePickerDialog() {
        return mDatePickerDialog;
    }
}