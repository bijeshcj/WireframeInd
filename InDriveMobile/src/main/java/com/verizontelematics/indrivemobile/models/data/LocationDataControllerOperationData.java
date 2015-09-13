package com.verizontelematics.indrivemobile.models.data;

import com.verizontelematics.indrivemobile.httpwrapper.Impl.Response;

/**
 * Created by bijesh on 1/30/2015.
 */
public class LocationDataControllerOperationData {
    private String mMessage = "";
    private Response mResponseData;
    private String action = "RealTime";

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

    public void setAction(String str) {
        action = str;
    }

    public String getAction() {
        return action;
    }
}
