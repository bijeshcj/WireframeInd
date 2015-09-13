package com.verizontelematics.indrivemobile.connection.protocols;

import com.android.volley.RequestQueue;
import com.verizontelematics.indrivemobile.connection.requests.RequestType;

/**
 * Created by bijesh on 2/18/2015.
 */
public class VerizonHttpsProtocol implements VerizonProtocols {

    @Override
    public RequestQueue getRequestQueue(RequestType requestType) {
        return null;
    }

    @Override
    public RequestQueue getRequestQueue(RequestType requestType, String userId, String deviceName, String registrationCode) {
        return null;
    }

}
