package com.verizontelematics.indrivemobile.connection.requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by bijesh on 2/26/2015.
 */
public class FindUserRequest extends Request<JSONObject> {

    private static final String TAG = FindUserRequest.class.getCanonicalName();
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private RequestParams requestParams;

    public FindUserRequest(int request,String url,Map<String,String> params,Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener,String userId,String password){
        super(request,url,errorListener);//Request.Method.POST
        this.listener = responseListener;
        this.params = params;//getAuthParams(userId,password);
    }

//    public FindUserRequest(int request,RequestParams requestParams,Response.Listener<JSONObject> responseListener,Response.ErrorListener errorListener){
//        super(request,requestParams.getUrl(),errorListener);
//        this.listener = responseListener;
//        this.requestParams = requestParams;
//    }

    protected Map<String,String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    /*
         * Override to provide custom body type, if using default form parameter
         * body type use the base class implementation. (non-Javadoc)
         *
         * @see com.android.volley.Request#getBody()
         */
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (requestParams != null) {
            // send our custom body data here.
            return prepareBody().getBytes();
        }
        return super.getBody();
    }

    private String prepareBody() {
        return "Data\" : {\n" +
                "    \"VIN\" : null,\n" +
                "    \"MobileDeviceID\" : \"41901C59-271C-4EC6-A767-816BC61FF542\",\n" +
                "    \"LastName\" : \"ESALast4\",\n" +
                "    \"IMEI\" : \"358111040001581\"\n" +
                "  }\n" ;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        Log.d(TAG, "$$$ parseNetworkResponse");
        try {
            Log.d(TAG," status Code "+response.statusCode);
//            initAuthenticateResponseData(response.statusCode,response.headers,new String(response.data));
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

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
         listener.onResponse(jsonObject);
    }
}
