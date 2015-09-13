package com.verizontelematics.indrivemobile.connection.errorlisteners;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by bijesh on 2/26/2015.
 */
public class FindUserErrorListener implements Response.ErrorListener {
    private static final String TAG = "FindUserErrorListener";

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        Log.d(TAG, volleyError.getMessage());
        volleyError.printStackTrace();
    }
}
