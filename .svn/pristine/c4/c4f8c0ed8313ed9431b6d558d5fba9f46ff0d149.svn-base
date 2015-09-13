package com.verizontelematics.indrivemobile.connection.requests;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.verizontelematics.indrivemobile.connection.responses.AuthenticateResponse;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by bijesh on 2/16/2015.
 */
public class AuthenticateRequest extends Request<JSONObject> {

    private static final String TAG = AuthenticateRequest.class.getCanonicalName();
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;


    public AuthenticateRequest(int request, String url, Map<String, String> params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener, String userId, String password){
        super(request,url,errorListener);//Request.Method.POST
        this.listener = responseListener;
        this.params = params;//getAuthParams(userId,password);
    }

    protected Map<String,String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }

//    private Map<String,String> getAuthParams(String userId,String password){
//        Map<String,String> authHeader = new HashMap<String, String>();
//        authHeader.put("Content-Type", Constants.APP_JSON);
//        authHeader.put("api_key",Constants.API_KEY);
//        authHeader.put(Constants.AUTHORIZATION, Base64Util.getB64Auth(userId, password));
//        return authHeader;
//    }

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Log.d(TAG,"$$$ parseNetworkResponse");
        try {
            Log.d(TAG," status Code "+response.statusCode);
            initAuthenticateResponseData(response.statusCode, response.headers, new String(response.data));
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    private void initAuthenticateResponseData(int statusCode, Map<String, String> headers, String data){
        Log.d(TAG,"$$$ setting Authenticate data "+data);
        String token = headers.get("token");
        AuthenticateResponse.setInstance(data, statusCode, token);
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
         listener.onResponse(jsonObject);
    }
}
