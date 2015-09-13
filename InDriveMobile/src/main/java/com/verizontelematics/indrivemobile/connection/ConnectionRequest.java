package com.verizontelematics.indrivemobile.connection;

import android.util.Log;

import com.android.volley.Request;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.connection.errorlisteners.AuthenticateErrorListener;
import com.verizontelematics.indrivemobile.connection.errorlisteners.DevicePairErrorListener;
import com.verizontelematics.indrivemobile.connection.errorlisteners.FindUserErrorListener;
import com.verizontelematics.indrivemobile.connection.errorlisteners.GenerateCertIdErrorListener;
import com.verizontelematics.indrivemobile.connection.errorlisteners.ValidateRegistrationErrorListener;
import com.verizontelematics.indrivemobile.connection.protocols.VerizonProtocolFactory;
import com.verizontelematics.indrivemobile.connection.requests.AuthenticateRequest;
import com.verizontelematics.indrivemobile.connection.requests.DevicePairRequest;
import com.verizontelematics.indrivemobile.connection.requests.FindUserRequest;
import com.verizontelematics.indrivemobile.connection.requests.GetCertificateIdRequest;
import com.verizontelematics.indrivemobile.connection.requests.RequestParams;
import com.verizontelematics.indrivemobile.connection.requests.RequestType;
import com.verizontelematics.indrivemobile.connection.requests.ValidateRegistrationRequest;
import com.verizontelematics.indrivemobile.connection.responselisteners.AuthenticateResponseListener;
import com.verizontelematics.indrivemobile.connection.responselisteners.DevicePairResponseListener;
import com.verizontelematics.indrivemobile.connection.responselisteners.FindUserResponseListener;
import com.verizontelematics.indrivemobile.connection.responselisteners.GenerateCertIdResponseListener;
import com.verizontelematics.indrivemobile.connection.responselisteners.ValidateRegistrationResponseListener;
import com.verizontelematics.indrivemobile.connection.utils.WebServiceConstants;

import org.json.JSONObject;

/**
 * Created by bijesh on 2/19/2015.
 * Class used as an entry point for connection request.
 */
public class ConnectionRequest implements WebServiceConstants{

    private static final String TAG = ConnectionRequest.class.getCanonicalName();

    public void requestSecuredConnection(RequestParams requestParams){
        Request<?> request = getConnectionRequest(requestParams);
        new VerizonProtocolFactory().getRequestQueue(requestParams).add(request);

    }

    private Request<JSONObject> getConnectionRequest(RequestParams requestParams){

        switch (requestParams.getRequestType()){
            case AUTHENTICATE:
               return getAuthenticateRequest(requestParams);
            case PAIR_DEVICE:
                return getDevicePairRequest(requestParams);
            case GET_CERTIFICATE_ID:
                return getGetCertificateIdRequest(requestParams);
            case VALIDATE_REGISTRATION_CODE:
                return getValidateRegistrationRequest(requestParams);
            case FIND_USER:
                return getFindUserRequest(requestParams);
        }
        return null;
    }



    private AuthenticateRequest getAuthenticateRequest(RequestParams requestParams){
        AuthenticateRequest authenticateRequest = new AuthenticateRequest(Request.Method.POST, AUTH_URL, null,//getAuthParams(),
                new AuthenticateResponseListener(),new AuthenticateErrorListener(),requestParams.getUserId(),requestParams.getPassword());

        authenticateRequest.setTag(getTagRequest(requestParams.getRequestType()));
        return authenticateRequest;
    }

    private FindUserRequest getFindUserRequest(RequestParams requestParams){
        FindUserRequest findUserRequest = new FindUserRequest(Request.Method.POST,FIND_USER_URL,null,
                new FindUserResponseListener(),new FindUserErrorListener(),"","");
//        FindUserRequest findUserRequest = new FindUserRequest(Request.Method.POST,requestParams,new FindUserResponseListener(),new FindUserErrorListener());
        findUserRequest.setTag(getTagRequest(requestParams.getRequestType()));
        return findUserRequest;
    }

    private DevicePairRequest getDevicePairRequest(RequestParams requestParams){
        DevicePairRequest devicePairRequest = new DevicePairRequest(Request.Method.POST,DEVICE_PAIR_URL, new DevicePairResponseListener(),
                new DevicePairErrorListener(),"");
        devicePairRequest.setTag(getTagRequest(requestParams.getRequestType()));
        return devicePairRequest;
    }

    private GetCertificateIdRequest getGetCertificateIdRequest(RequestParams requestParams){


        GetCertificateIdRequest getcertificateIdRequest = new GetCertificateIdRequest(Request.Method.POST,GET_CERT_ID_URL,new GenerateCertIdResponseListener(),
                new GenerateCertIdErrorListener(),"");
        getcertificateIdRequest.setTag(getTagRequest(requestParams.getRequestType()));
        return getcertificateIdRequest;
    }

    private ValidateRegistrationRequest getValidateRegistrationRequest(RequestParams requestParams){
        String deviceName = IndriveApplication.getInstance().getDeviceName();
        Log.d(TAG, "$$$ deviceName "+deviceName);

        ValidateRegistrationRequest validateRegistrationRequest = new ValidateRegistrationRequest(Request.Method.POST,VALIDATE_REG_CODE_URL,
                new ValidateRegistrationResponseListener(),
                new ValidateRegistrationErrorListener(),"",deviceName) ;
        validateRegistrationRequest.setTag(getTagRequest(requestParams.getRequestType()));
//        Layer7Application.getInstance().getRequestQueue(RequestType.VALIDATE_REGISTRATION_CODE,"10562468",deviceName,"1234").add(validateRegistrationRequest);
        return validateRegistrationRequest;
    }



    private String getTagRequest(RequestType requestType){
        return requestType.toString();
    }

}
