package com.verizontelematics.indrivemobile.httpwrapper.Impl;

import org.json.JSONObject;

public class Response {

    private JSONObject data;
    private String responseStatus;
    private int responseCode;
    private String responseDescription;
    // instead of using this integer use some enumeration.
    private int httpStatusCode = 200;

    public JSONObject getData(){
        return data;
    }

    public void setData(JSONObject aJSon){
        data = aJSon;
    }



    public void setHttpStatusCode(int aStatusCode){
        httpStatusCode = aStatusCode;
    }




    public int getHttpStatusCode(){
        return httpStatusCode;
    }


    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
}
