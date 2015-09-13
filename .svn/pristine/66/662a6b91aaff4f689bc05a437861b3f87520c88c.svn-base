package com.verizontelematics.indrivemobile.connection.utils;

import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bijesh on 2/17/2015.
 */
public class ContextUtils {
    public static JSONObject getContextJson() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        JSONObject json1 = new JSONObject();
        try {
            json1.put(Constants.SOURCE_NAME, "MAPP");
            json1.put(Constants.TRANSACTION_ID, "SXM-20141224");
            json1.put(Constants.TIME_STAMP, format.format(cal.getTime()) + "Z");
            json1.put(Constants.ORG, "VW");
            json1.put(Constants.REGION, "US");
            json1.put(Constants.APP_NAME, "CWP");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json1;
    }
}
