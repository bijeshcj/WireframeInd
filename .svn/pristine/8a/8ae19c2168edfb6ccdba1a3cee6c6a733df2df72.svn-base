/*
 *
 * The Verizon Telematics Software is provided by Verizon Telematics on an �AS IS� basis.
 *  Verizon Telematics MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE, REGARDING THE Verizon Telematics SOFTWARE OR ITS USE AND
 * OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * Copyright (C) 2014 Verizon Telematics Inc. All Rights Reserved.
*/

package com.verizontelematics.indrivemobile.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;

import com.verizontelematics.indrivemobile.models.data.BaseData;

import java.lang.reflect.Field;


/**
 *
 * @author Verizon Telematics-Venice Team
 *
 */

public class BasicUtil {
    /**
     *
     * @param tag
     * @param data
     */
    public static void printBytesInByteArray(String tag, byte[] data) {
        StringBuilder s = new StringBuilder();
        s.setLength(0);
        for (byte aData : data) {
            s.append(Byte.toString(aData)).append(" ");
        }
        Log.i("BasicUtil", " printBytesInByteArray : " + s);
    }

    /**
     *
     * @param bytes
     * @return String
     */
    public static String convertToString(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (byte aByte : bytes) {
            s.append((char) aByte);
        }
        return s.toString();
    }

    /**
     *
     * @param data
     * @return byte array
     */
    public static byte[] convertToByteArray(String data) {
        byte[] result = new byte[data.length()];
        for (int i = 0; i < data.length(); i++) {
            result[i] = (byte) data.charAt(i);
        }
        return result;
    }

    /**
     *
     * @param buf
     * @return string
     */
    public static String byteArrayToHexString(byte[] buf) {
        StringBuilder stringBuffer = new StringBuilder(buf.length * 2);
        for (byte aBuf : buf) {
            if (((int) aBuf & 0xff) < 0x10) {
                stringBuffer.append("0");
            }
            stringBuffer.append(Long.toString((int) aBuf & 0xff, 16));
        }
        return stringBuffer.toString();
    }

    /**
     *
     * @param s
     * @return byte array
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i + 1);
            data[i / 2] = (byte) ((Character.digit(c1, 16) << 4) + Character
                    .digit(c2, 16));
        }
        return data;
    }

    /**
     *
     * @param px
     * @return integer
     */
    public static int getDpForXlargeScreen(float px) {
        int dp = (int) ((px / 2.0f) + 0.5);
        return dp;
    }

    /**
     * Use this method to hide the Android Soft keyboard
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity aActivity) {
        InputMethodManager inputManager = (InputMethodManager) aActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = aActivity.getCurrentFocus();
        if (view != null) {
                inputManager.showSoftInput(view,0);
        }
    }


    public static <T extends BaseData> boolean validate(T data){
        Class cls = data.getClass();
        Field[] allFields = cls.getDeclaredFields();
        for(Field field:allFields){
//            String fieldName = field.getName();
            field.setAccessible(true);
//            System.out.println("field name "+fieldName);
            try {
                Object val = field.get(data);
//                System.out.println("val "+val);
                Object fieldType = field.getType();
//                System.out.println("filed type "+fieldType);
                String fType = fieldType.toString();
                if(fType.equals("class java.lang.String")){

                    String valS = (String)val;
                    if(valS == null || valS.length() == 0){
                        return false;
                    }
                }else{

                    if(val.equals(0) || val.equals(0.0)){
                        return false;
                    }
                }

            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
        System.out.println("returning true");
        return true;
    }

    /**
     * Method to set the alpha value to views.
     * Use startAnimation() as a work around for sdk version < 11, as
     * setAlpha() is not supported for lower versions
     * @param alpha
     * @param view
     */
    public static void setAlphaToViews(float alpha, View view){
        if(Build.VERSION.SDK_INT < 11){
            final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        }else
            view.setAlpha(alpha);
    }


    public static String maskText(String actualText){
        String retVal = null;
        char[] chars = null;
        if(actualText != null){
            chars = actualText.toCharArray();
            for(int i=0;i<chars.length;i++){
                if(i == 0 || i == chars.length -1){

                }else{
                    chars[i] = '.';
                }
            }
        }
        retVal = String.valueOf(chars);
        return retVal;
    }


}
