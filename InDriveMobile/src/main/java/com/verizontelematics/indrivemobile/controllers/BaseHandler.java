package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.httpwrapper.RestClient;

/**
 * Created by z688522 on 10/22/14.
 */
public class BaseHandler implements RestClient.ErrorListener, RestClient.ResponseListener {

    @Override
    public void onError(RestClient.HttpResponse aData, RestClient.HttpRequest aReq) {

    }

    @Override
    public void onResponse(RestClient.HttpResponse aResponse, RestClient.HttpRequest aReq) {

    }
}
