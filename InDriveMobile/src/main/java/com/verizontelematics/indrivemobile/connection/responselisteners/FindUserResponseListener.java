package com.verizontelematics.indrivemobile.connection.responselisteners;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by bijesh on 2/26/2015.
 */
public class FindUserResponseListener implements Response.Listener<JSONObject> {

    private static final String TAG = FindUserResponseListener.class.getCanonicalName();
    @Override
    public void onResponse(JSONObject jsonObject) {
        Log.d(TAG, "$$$$ onResponse FindUserResponseListener "+jsonObject.toString());
    }
}
