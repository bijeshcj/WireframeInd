package com.verizontelematics.indrivemobile.httpwrapper;


import android.util.Log;

import com.verizontelematics.indrivemobile.httpwrapper.Impl.VerizonRestClient;

public class RestClientFactory {

    private static final String TAG = RestClientFactory.class.getCanonicalName();
    private RestClientFactory() {

    }

    public static synchronized RestClient getDefaultRestClient() {
        Log.d(TAG,"layer7 getDefaultRestClient ");
        return VerizonRestClient.getInstance();
    }
}
