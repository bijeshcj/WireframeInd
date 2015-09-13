package com.verizontelematics.indrivemobile.httpwrapper.Impl;

import com.android.volley.NetworkResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;

import java.util.Map;


public class VerizonHttpResponse implements RestClient.HttpResponse {

    private NetworkResponse response;

    public VerizonHttpResponse(NetworkResponse aResponse) {
        response = aResponse;
    }

    public NetworkResponse getResponse() {
        return response;
    }

    @Override
    public byte[] getRawData() {
        return getResponse().data;
    }

    // forcefully putting the string to encode utf-8 set
    @Override
    public String getDataAsString() {
        try{
            return new String(getRawData(),"UTF-8");

        }
        catch (Exception e)
        {
           return "";
        }
    }

    @Override
    public JsonObject getDataAsJson() {
        Gson gson = new Gson();
        return gson.fromJson(getDataAsString(),
                JsonElement.class)
                .getAsJsonObject();
    }

    @Override
    public Map<String, String> getHeaders() {
        return getResponse().headers;
    }

    @Override
    public int getHttpStatus() {
        return getResponse().statusCode;
    }

}
