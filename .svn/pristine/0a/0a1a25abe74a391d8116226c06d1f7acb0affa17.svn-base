package com.verizontelematics.indrivemobile.connection.responselisteners;

import android.util.Log;

import com.android.volley.Response;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.connection.ConnectionRequest;
import com.verizontelematics.indrivemobile.connection.requests.RequestParams;
import com.verizontelematics.indrivemobile.connection.requests.RequestType;

import org.json.JSONObject;

/**
 * Created by bijesh on 2/17/2015.
 */
public class GenerateCertIdResponseListener implements Response.Listener<JSONObject> {
    private static final String TAG = GenerateCertIdResponseListener.class.getCanonicalName();
    @Override
    public void onResponse(JSONObject jsonObject) {
        Log.d(TAG, "%%%%%%%%%%%% on response received GenerateCertIdResponseListener &&&&&&&&&&&&&&");
        //        Layer7Application.getInstance().getRequestQueue(RequestType.VALIDATE_REGISTRATION_CODE,"10562468",deviceName,"1234").add(validateRegistrationRequest);
        new ConnectionRequest().requestSecuredConnection(new RequestParams(RequestType.VALIDATE_REGISTRATION_CODE,"10562468","1234",
                "123",IndriveApplication.getInstance().getDeviceName()));
//        getRequestQueue(RequestType.VALIDATE_REGISTRATION_CODE,"10562468",mEdtTxtRegistrationCode.getText().toString().trim(),deviceName).add(validateRegistrationRequest);
    }
}
