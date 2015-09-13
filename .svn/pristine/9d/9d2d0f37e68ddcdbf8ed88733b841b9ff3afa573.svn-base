package com.verizontelematics.indrivemobile.utils.ui;


import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.verizontelematics.indrivemobile.customViews.CustomTimePicker;

import java.lang.reflect.Field;

/**
 * Created by bijesh on 1/18/2015.
 */
public class RefUtil {

    public static void isTimePickerVisible(CustomTimePicker timePicker, int dpi) {
        try {
            Field[] fields = timePicker.getClass().getSuperclass().getDeclaredFields();   // getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("mAmPmSpinner")) {

                    field.setAccessible(true);
                    NumberPicker numberPicker = (NumberPicker) field.get(timePicker);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getAmPmSpinnerWidth(dpi), numberPicker.getLayoutParams().height);
                    numberPicker.setLayoutParams(params);
                }
                if (field.getName().equals("mHourSpinner") || field.getName().equals("mMinuteSpinner")) {

                    field.setAccessible(true);
                    NumberPicker numberPicker = (NumberPicker) field.get(timePicker);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getHourMinuteSpinnerWidth(dpi), numberPicker.getLayoutParams().height);
                    numberPicker.setLayoutParams(params);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static int getHourMinuteSpinnerWidth(int dpi) {
        int width = 0;
        switch (dpi) {
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                width = 80;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                width = 100;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                width = 150;
                break;
            default:
                break;
        }
        return width;
    }

    private static int getAmPmSpinnerWidth(int dpi) {
        int width = 0;
        switch (dpi) {
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                width = 60;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                width = 100;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                width = 150;
                break;
            default:
                break;
        }
        return width;
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }
}