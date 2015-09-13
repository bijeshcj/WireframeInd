package com.verizontelematics.indrivemobile.connection.responselisteners;

import android.util.Log;

import com.android.volley.Response;
import com.verizontelematics.indrivemobile.connection.ConnectionRequest;
import com.verizontelematics.indrivemobile.connection.requests.RequestParams;
import com.verizontelematics.indrivemobile.connection.requests.RequestType;
import com.verizontelematics.indrivemobile.connection.responses.AuthenticateResponse;


import org.json.JSONObject;

/**
 * Created by bijesh on 2/16/2015.
 */
public class AuthenticateResponseListener implements Response.Listener<JSONObject> {
    private static String TAG = AuthenticateResponseListener.class.getCanonicalName();
    @Override
    public void onResponse(JSONObject s) {
        Log.d(TAG, "^^^^^^^^^^^^^^ onResponse in Authenticate &&&&&&&&&&&&&&");
        Log.d(TAG,"Authenticate response "+s.toString());
        Log.d(TAG,"Authenticate Response Data "+ AuthenticateResponse.getInstance());
        new ConnectionRequest().requestSecuredConnection(new RequestParams(RequestType.PAIR_DEVICE));
    }
}
