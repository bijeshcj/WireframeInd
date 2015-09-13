package com.verizontelematics.indrivemobile.connection.errorlisteners;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by bijesh on 2/16/2015.
 */
public class AuthenticateErrorListener implements Response.ErrorListener {
    private static final String TAG = AuthenticateErrorListener.class.getCanonicalName();
    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e(TAG,"%%%%%%%%%%% error %%%%%%%%%%% "+volleyError.getMessage()+" error code "+volleyError.networkResponse.statusCode);
    }
}
