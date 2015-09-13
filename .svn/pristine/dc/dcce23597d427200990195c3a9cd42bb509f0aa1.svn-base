package com.verizontelematics.indrivemobile.connection.protocols;

import com.android.volley.RequestQueue;
import com.verizontelematics.indrivemobile.connection.requests.RequestType;

/**
 * Created by bijesh on 2/18/2015.
 */
public interface VerizonProtocols {

    /**
     * Determines the RequestQueue type
     * @param requestType
     * @return
     */
    public RequestQueue getRequestQueue(RequestType requestType);

    /**
     * Determines the RequestQueue type with additional parameters
     * @param requestType
     * @param userId
     * @param deviceName
     * @param registrationCode
     * @return
     */
    public RequestQueue getRequestQueue(RequestType requestType,String userId,String deviceName,String registrationCode);


}
