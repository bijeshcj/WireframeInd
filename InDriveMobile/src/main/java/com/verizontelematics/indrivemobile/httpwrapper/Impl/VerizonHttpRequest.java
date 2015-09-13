package com.verizontelematics.indrivemobile.httpwrapper.Impl;

import com.verizontelematics.indrivemobile.httpwrapper.RestClient;

import java.util.Map;


public class VerizonHttpRequest implements RestClient.HttpRequest {
    private String url;
    private Map<String, String> headers;
    private String data;
    private int method;
    private RestClient.ErrorListener errorListener;
    private RestClient.ResponseListener responseListener;
    private Object tag;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public com.verizontelematics.indrivemobile.httpwrapper.RestClient.ErrorListener getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(RestClient.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public RestClient.ResponseListener getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(RestClient.ResponseListener responseListener) {
        this.responseListener = responseListener;
    }


}
