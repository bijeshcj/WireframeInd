package com.verizontelematics.indrivemobile.controllers;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.Response;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.VerizonHttpRequest;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;
import com.verizontelematics.indrivemobile.models.AlertsControllerOperationData;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.NetworkUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by z688522 on 3/17/15.
 */
public class MessageController extends BaseController {

    private static final int REGISTER = Operation.OperationCode.REGISTER_PUSH.ordinal();
    private static final int DEREGISTER = Operation.OperationCode.DEREGISTER_PUSH.ordinal();
    private static String TAG = MessageController.class.getCanonicalName();
    private Operation opr;

    private static MessageController mInstance = null;

    public static MessageController instance() {
        if (mInstance == null)
            mInstance = new MessageController();
        return mInstance;
    }

    private OperationFactory mOperationFactory = MessageControllerOpertationFactory.instance();

    private String getRegistrationToken(String regToken){
        if(AppController.instance().getGCMToken().equals(regToken)){
            return regToken;
        }else{
            String retVal = AppController.instance().getGCMToken();
            AppController.instance().setGCMToken(regToken);
            return retVal;
        }
    }

    public Operation registerDevice(Context ctx, String registrationToken, String accountId, String AppId) {
        Log.d(TAG,"gcm registerDevice");
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(REGISTER, Operation.CREATED))) {
            opr = mOperationsModel.getOperation(REGISTER);
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                String url = "https://api-test.hughestelematics.com/registerdevice/"+getRegistrationToken(registrationToken);// old replace here
                Log.d(TAG,"gcm url "+url);
                final String body = prepareBody(registrationToken, accountId, AppId);
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new com.android.volley.Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                opr.setState(Operation.FINISHED);
                            }
                        },
                        new com.android.volley.Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                                opr.setState(Operation.ERROR);
                            }
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String> ();
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders(){
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("operation", Integer.toString(REGISTER));
                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        return body.getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                };
                putRequest.setTag("PNR");
                submit(putRequest);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(REGISTER);
    }

    private String prepareBody(String regToken, String accountId, String AppId) {
        // Please remove hardcoded stuff as much
        // OS Version get it from Device.
        String str = "DeviceType=Android,1&Latitude=0&Platform=Android&AccountId="+accountId+"&OSVersion=5.1&AppId="+AppId+"&Locale=en-US&Token="+regToken+"&EnvironmentId=0&Longitude=0";
        Log.d(TAG,"payload for gcm registration "+str);
        return str;
    }

    public Operation deRegisterDevice(Context ctx, String registrationToken, String accountId, String appId) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(DEREGISTER, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = "https://api-test.hughestelematics.com/deregisterdevice/"+registrationToken;
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(DEREGISTER));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBody(accountId, appId));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(DEREGISTER);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(DEREGISTER);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(DEREGISTER);
    }

    private String prepareBody(String accountId, String appId) {
        return "Platform=Android&AccountId="+accountId+"&AppId="+3;
    }

    @Override
    public void handleErrorForOperation(Response aResponse, Operation aOperation) {
        Log.d(TAG,"gcm error "+aResponse.getResponseDescription());
        aOperation.setState(Operation.ERROR);
        aOperation.setInformation(aResponse.getResponseDescription());
        onError(aOperation);
    }

    @Override
    public void handleResponseForOperation(Response aResponse, Operation aOperation){
        Log.d(TAG, "handleResponse for operation " + aOperation.getClass());

        try {
            Gson gson = new GsonBuilder().create();
            JSONObject lData = aResponse.getData();
            if (aOperation.getId() == Operation.OperationCode.REGISTER_PUSH.ordinal()) {
                String status = lData.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    Log.d(TAG,"gcm : status"+status);
                    aOperation.setState(Operation.FINISHED);
                    onSuccess(aOperation);
                }
                else {
                    aOperation.setState(Operation.ERROR);
                    aOperation.setInformation("Application ID not found");
                    onError(aOperation);
                }
            }


        }catch (JSONException e){
            aOperation.setState(Operation.ERROR);
            aOperation.setInformation(e.getLocalizedMessage());
            onError(aOperation);
        } catch (JsonSyntaxException syntaxException) {
            aOperation.setState(Operation.ERROR);
            aOperation.setInformation(syntaxException.getLocalizedMessage());
            onError(aOperation);
        }
    }

    @Override
    public void onResponse(RestClient.HttpResponse aResponse, RestClient.HttpRequest aReq) {
        Operation opr = mOperationsModel.getOperation(aReq);

        if (opr == null) {
            return;
        }
        Response lResponse = new Response();

        if (aResponse == null || aResponse.getDataAsString().isEmpty()) {
            if (opr != null) {
                //((UnifiedVehicleStatusOperationData)opr.getData()).setMessage("Fetch recent vehicle status Success");
                opr.setState(Operation.ERROR);
                onError(opr);
            }
        }

        Log.d("BaseController", "RESPONSE is ---->" + aResponse.getDataAsString());
        try {
            JSONObject json = new JSONObject(aResponse.getDataAsString());
            // set response.
            lResponse.setData(json);
            lResponse.setHttpStatusCode(aResponse.getHttpStatus());
            handleResponseForOperation(lResponse, opr);

        } catch( JSONException e ){
            lResponse.setHttpStatusCode(aResponse.getHttpStatus());
            lResponse.setResponseDescription("Invalid response " + aResponse.getDataAsString());
            opr.setState(Operation.ERROR);
            handleErrorForOperation(lResponse, opr);
        }
    }
}
