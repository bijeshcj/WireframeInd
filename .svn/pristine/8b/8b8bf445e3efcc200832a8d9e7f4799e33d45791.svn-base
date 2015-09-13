package com.verizontelematics.indrivemobile.connection.responses;

/**
 * Created by bijesh on 2/17/2015.
 */
public class GetCertificateIdResponse {
    private static GetCertificateIdResponse mInstance;

    private String data;
    private String token;
    private int statusCode;

    private GetCertificateIdResponse(){

    }

    public static GetCertificateIdResponse getInstance(){
        return mInstance;
    }

    public static void setInstance(String data,int statusCode,String token){
        mInstance = new GetCertificateIdResponse();
        mInstance.setData(data);
        mInstance.setStatusCode(statusCode);
        mInstance.setToken(token);
    }

    public int getStatusCode() {
        return statusCode;
    }

    private void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    private void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "GetCertId Response Data "+data+" statusCode "+statusCode+" Token "+token;
    }

}
