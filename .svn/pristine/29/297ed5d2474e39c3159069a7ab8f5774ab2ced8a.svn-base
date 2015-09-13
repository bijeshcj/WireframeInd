package com.verizontelematics.indrivemobile.httpwrapper;

import android.os.Handler;

import com.android.volley.Request;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * 1. Make the Implementation Thread safe.
 * 2. Deliver the callbacks on request threads.
 * 3. There is no synchronous mode.
 * 4. Hide Volley details.
 *
 * @author Ravinder
 */
public interface RestClient {

    public static final int GET = 1;
    public static final int POST = 2;
    public static final int PUT = 3;
    public static final int DELETE = 4;

    public void cancelRequest(HttpRequest aRequest);

    public void addRequest(HttpRequest aRequest, Handler aHandler);

    public void addRequest(Request request);

    public void cancelAll();

    public interface ErrorListener {
        public void onError(HttpResponse aData, HttpRequest aReq);
    }

    public interface ResponseListener {
        public void onResponse(HttpResponse aResponse, HttpRequest aReq);
    }

    public interface HttpRequest {
        public int getMethod();

        public String getUrl();

        public String getData();

        public Map<String, String> getHeaders();

        public ErrorListener getErrorListener();

        public ResponseListener getResponseListener();

        public Object getTag();
    }

    public interface HttpResponse {
        public byte[] getRawData();

        public String getDataAsString();

        public JsonObject getDataAsJson();

        public Map<String, String> getHeaders();

        public int getHttpStatus();
    }
}
