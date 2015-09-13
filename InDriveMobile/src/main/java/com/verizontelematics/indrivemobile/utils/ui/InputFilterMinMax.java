package com.verizontelematics.indrivemobile.utils.ui;

/**
 * Created by Z681639 on 11/5/2014.
 */

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.utils.AppConstants;

public class InputFilterMinMax implements InputFilter {

    private static final String TAG = InputFilterMinMax.class.getSimpleName();
    private int min, max;
    private Context context;
    private AppConstants.USER_SELECTION selection;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max, Context context, AppConstants.USER_SELECTION selection) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
        this.context = context;
        this.selection = selection;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
            else {
                if (selection == AppConstants.USER_SELECTION.MILEAGE) {
                    Toast.makeText(context, context.getResources().getString(R.string.alert_mileage_validation), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.alert_frequency_validation), Toast.LENGTH_SHORT).show();
                }

            }

        } catch (NumberFormatException nfe) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(nfe));
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}