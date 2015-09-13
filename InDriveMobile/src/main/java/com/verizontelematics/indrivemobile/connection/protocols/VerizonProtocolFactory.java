package com.verizontelematics.indrivemobile.connection.protocols;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.connection.requests.RequestParams;
import com.verizontelematics.indrivemobile.connection.requests.RequestType;
import com.verizontelematics.indrivemobile.connection.ssl.VerizonSSLHttpStack;

/**
 * Created by bijesh on 2/18/2015.
 */
public class VerizonProtocolFactory {

    private static final String TAG = VerizonProtocolFactory.class.getCanonicalName();

    public RequestQueue getRequestQueue(RequestParams requestParams){
       Log.d(TAG, "$$$ getRequestQueue ");
       Context context = IndriveApplication.getInstance().getInDriveApplicationContext();
       RequestType requestType = requestParams.getRequestType();
       String userId = requestParams.getUserId();
       Log.d(TAG, "$$$ getRequestQueue userId "+userId);
       String deviceName = requestParams.getDeviceName();
       String registrationCode = requestParams.getRegistrationCode();

        RequestQueue mRequestQueue = Volley.newRequestQueue(context, new VerizonSSLHttpStack(context, requestType, userId, deviceName, registrationCode));
//       mRequestQueue = Volley.newRequestQueue(context,new VerizonSSLHttpStack(context,requestParams));
       return mRequestQueue;
   }

}
