package com.verizontelematics.indrivemobile.connection.requests;

/**
 * Created by bijesh on 2/18/2015.
 */
public class RequestParams {

    private RequestType requestType;
    private String userId;
    private String password;
    private String deviceName;
    private String registrationCode;
    private String url;

    public RequestParams(RequestType requestType){
        this.requestType = requestType;
    }

    public RequestParams(RequestType requestType,String userId,String password,String deviceName,String registrationCode,String url){
        this.requestType = requestType;
        this.userId = userId;
        this.password = password;
        this.deviceName = deviceName;
        this.registrationCode = registrationCode;
        this.url = url;
    }

    public RequestParams(RequestType requestType,String userId,String password,String deviceName,String registrationCode){
        this.requestType = requestType;
        this.userId = userId;
        this.password = password;
        this.deviceName = deviceName;
        this.registrationCode = registrationCode;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }
}
