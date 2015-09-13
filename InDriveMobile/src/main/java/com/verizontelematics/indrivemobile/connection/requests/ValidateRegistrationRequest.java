package com.verizontelematics.indrivemobile.connection.requests;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.verizontelematics.indrivemobile.connection.responses.ValidateRegistrationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by bijesh on 2/18/2015.
 */
public class ValidateRegistrationRequest extends Request<JSONObject> {

    private static final String TAG = ValidateRegistrationRequest.class.getCanonicalName();
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public ValidateRegistrationRequest(int request, String url, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener, String userId, String deviceName){
        super(request,url,errorListener);//Request.Method.POST
        this.listener = responseListener;
        this.params = params;//getAuthParams(userId,password);
        String mDeviceName = deviceName;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Log.d(TAG, "$$$ parseNetworkResponse");
        try {
            Log.d(TAG," status Code "+response.statusCode);
            initValidateRegistrationResponseData(response.statusCode, response.headers, new String(response.data));
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

    private void initValidateRegistrationResponseData(int statusCode,Map<String,String> headers,String data){
        String token = headers.get("token");
        ValidateRegistrationResponse.setInstance(data, statusCode, token);
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        listener.onResponse(jsonObject);
    }
}
