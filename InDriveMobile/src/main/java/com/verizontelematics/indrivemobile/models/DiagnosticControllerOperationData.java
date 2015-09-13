package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.httpwrapper.Impl.Response;

/**
 * Created by z688522 on 10/23/14.
 */
public class DiagnosticControllerOperationData {
    private String mMessage = "";
    private Response mResponseData;

    public void setMessage(String aMessage) {
        mMessage = aMessage;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setData(Response obj) {
        mResponseData =  obj;
    }

    public Response getData() { return mResponseData; }

}
