package com.verizontelematics.indrivemobile.connection.ssl;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.verizontelematics.indrivemobile.connection.client.VerizonHttpsClient;
import com.verizontelematics.indrivemobile.connection.requests.RequestParams;
import com.verizontelematics.indrivemobile.connection.requests.RequestType;
import com.verizontelematics.indrivemobile.connection.responses.AuthenticateResponse;
import com.verizontelematics.indrivemobile.connection.responses.DevicePairResponse;
import com.verizontelematics.indrivemobile.connection.responses.GetCertificateIdResponse;
import com.verizontelematics.indrivemobile.connection.utils.Base64Util;
import com.verizontelematics.indrivemobile.connection.utils.Constants;
import com.verizontelematics.indrivemobile.connection.utils.RequestPayloadUtil;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bijesh on 2/10/2015.
 */
public class VerizonSSLHttpStack implements HttpStack {

    private static String TAG = VerizonSSLHttpStack.class.getCanonicalName();
    private Context mContext;
    private RequestType mRequestType;
    private String mUserId;
    private String mRegistrationCode;
    private String mDeviceName;
    private RequestParams mRequestParams;

    public VerizonSSLHttpStack(Context context){
        this.mContext = context;
    }

    public VerizonSSLHttpStack(Context context, RequestType requestType){
        Log.d(TAG,"$$$ VerizonSSLHttpStack ");
        this.mContext = context;
        this.mRequestType = requestType;
    }

    public VerizonSSLHttpStack(Context context, RequestType requestType, String userId, String registrationCode, String deviceName){
        this.mContext = context;
        this.mRequestType = requestType;
        this.mUserId = userId;
        this.mRegistrationCode = registrationCode;
        this.mDeviceName = deviceName;

    }

    public VerizonSSLHttpStack(Context context,RequestParams requestParams){
        this.mContext = context;
        this.mRequestParams = requestParams;
        this.mRequestType = requestParams.getRequestType();
    }

    private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }

    private static List<NameValuePair> getPostParameterPairs(Map<String, String> postParams) {
        List<NameValuePair> result = new ArrayList<NameValuePair>(postParams.size());
        for (String key : postParams.keySet()) {
            result.add(new BasicNameValuePair(key, postParams.get(key)));
        }
        return result;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        Log.d(TAG, "$$$ performRequest");
        Log.d(TAG,"$$$ url "+request.getUrl());
        HttpUriRequest httpRequest = null;

//        HttpPost post = new HttpPost(request.getUrl());
//        post.setHeader("Content-Type", Constants.APP_JSON);
//        post.setHeader("api_key", Constants.API_KEY);
//        post.setHeader(Constants.AUTHORIZATION, Base64Util.getB64Auth(login, pass));
        switch (mRequestType){
            case AUTHENTICATE:
                httpRequest = createAuthenticateHttpRequest(request, additionalHeaders);
                break;
            case PAIR_DEVICE:
                httpRequest = createDevicePairHttpRequest(request);
                break;
            case GET_CERTIFICATE_ID:
                httpRequest = createGetCertificateIdHttpRequest(request);
                break;
            case VALIDATE_REGISTRATION_CODE:
                httpRequest = createValidateRegistrationHttpRequest(request);
                break;
            case FIND_USER:
                httpRequest = createFindUserHttpRequest(request);
                break;
        }

        HttpClient client = new VerizonHttpsClient().getVerizonHttpClient(mContext);
        HttpResponse httpResponse = client.execute(httpRequest);
//        AuthenticateResponse.parseAuthenticateResponse(httpResponse);
        return httpResponse;
    }

     HttpUriRequest createAuthenticateHttpRequest(Request<?> request,
                                                        Map<String, String> additionalHeaders) throws AuthFailureError {
        HttpPost post = new HttpPost(request.getUrl());
        Log.d(TAG,"$$$7 url "+request.getUrl()+" user id "+mUserId);
        post.setHeader("Content-Type", Constants.APP_JSON);
        post.setHeader("api_key", Constants.API_KEY);
        post.setHeader(Constants.AUTHORIZATION, Base64Util.getB64Auth(mUserId, "8989"));//"10562468"
        try {
            post.setEntity(new StringEntity(Constants.AUTHORIZATION_DATA));
            printRequestPayload(post);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return post;
    }

    HttpUriRequest createDevicePairHttpRequest(Request<?> request) throws AuthFailureError {
        Log.d(TAG,"$$$ createDevicePairHttpRequest ");
        HttpPost post = new HttpPost(request.getUrl());
        post.setHeader("Content-Type", Constants.APP_JSON);
        post.setHeader("api_key", Constants.API_KEY);
        post.setHeader(Constants.AUTHORIZATION, "Basic MTA1NjI0Njg6ODk4OQ==");
        post.setHeader(Constants.AUTHORIZATION, "Bearer " + AuthenticateResponse.getInstance().getToken());
        try {
            post.setEntity(new StringEntity(RequestPayloadUtil.getDevicePairPayLoad("10562468", AuthenticateResponse.getInstance())));
            printRequestPayload(post);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return post;
    }

    HttpUriRequest createGetCertificateIdHttpRequest(Request<?> request) throws AuthFailureError {
        Log.d(TAG,"$$$ createGetCertificateIdHttpRequest ");
        HttpPost post = new HttpPost(request.getUrl());
        post.setHeader("Content-Type", Constants.APP_JSON);
        post.setHeader("api_key", Constants.API_KEY);
        post.setHeader(Constants.AUTHORIZATION, "Basic MTA1NjI0Njg6ODk4OQ==");
        post.setHeader(Constants.AUTHORIZATION, "Bearer " + DevicePairResponse.getInstance().getToken());
        try {
            post.setEntity(new StringEntity(RequestPayloadUtil.getCertificateIdPayLoad("10562468", DevicePairResponse.getInstance())));
            printRequestPayload(post);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return post;
    }

    HttpUriRequest createFindUserHttpRequest(Request<?> request) throws AuthFailureError{
        Log.d(TAG,"$$$ createFindUserHttpRequest ");
        HttpPost post = new HttpPost(request.getUrl());
        post.setHeader("Content-Type", Constants.APP_JSON);
        post.setHeader("api_key", Constants.API_KEY);
//        post.setHeader(Constants.AUTHORIZATION, "Basic MTA1NjI0Njg6ODk4OQ==");
//        post.setHeader(Constants.AUTHORIZATION, "Bearer " + GetCertificateIdResponse.getInstance().getToken());
        try {
            post.setEntity(new StringEntity(RequestPayloadUtil.getFirstUserPayLoad(mRequestParams)));
            printRequestPayload(post);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return post;
    }

    HttpUriRequest createValidateRegistrationHttpRequest(Request<?> request) throws AuthFailureError {

        Log.d(TAG,"$$$ createValidateRegistrationHttpRequest ");
        HttpPost post = new HttpPost(request.getUrl());
        post.setHeader("Content-Type", Constants.APP_JSON);
        post.setHeader("api_key", Constants.API_KEY);
        post.setHeader(Constants.AUTHORIZATION, "Basic MTA1NjI0Njg6ODk4OQ==");
        post.setHeader(Constants.AUTHORIZATION, "Bearer " + GetCertificateIdResponse.getInstance().getToken());
        try {
            post.setEntity(new StringEntity(RequestPayloadUtil.getValidateRegistrationPayLoad("10562468",mRegistrationCode,mDeviceName)));
            printRequestPayload(post);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return post;
    }

    private static void printRequestPayload(HttpPost post){
        System.out.println("printRequestPayload 1");
        Header[] headers = post.getAllHeaders();
        for(Header header:headers){
            System.out.println("request header: "+header.toString());
        }
        try {

            System.out.println("post entities "+ EntityUtils.toString(post.getEntity()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
