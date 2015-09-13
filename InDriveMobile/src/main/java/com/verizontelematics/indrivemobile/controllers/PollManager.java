package com.verizontelematics.indrivemobile.controllers;

import android.util.Log;

import com.verizontelematics.indrivemobile.httpwrapper.Impl.Response;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.VerizonHttpRequest;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;
import com.verizontelematics.indrivemobile.models.PollOperation;
import com.verizontelematics.indrivemobile.models.RealVehicleRequestStatusPollOperation;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.phone.RetryTimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by z688522 on 2/6/15.
 */
public class PollManager extends BaseController {
    private List<PollOperation> mPollOperations = null;
    static private PollManager mInstance = null;

    private final String TAG = "PollManager";

    private PollManager() {

    }
    public static synchronized  PollManager instance() {
        if (mInstance == null) {
            mInstance = new PollManager();
            mInstance.initialize();
        }
        return mInstance;
    }
    private void initialize() {
        mPollOperations = new ArrayList<PollOperation>();
    }

    private boolean add(PollOperation pollOperation) {
        for (PollOperation opr : mPollOperations) {
            if (opr.getRequestURL().equals(pollOperation.getRequestURL()))
                return false;
        }
        return mPollOperations.add(pollOperation);
    }

    public boolean remove(PollOperation pollOperation) {
        return mPollOperations != null && mPollOperations.remove(pollOperation);
    }

    private PollOperation create(String aURL, PollOperation.OnCheckPollListener onCheckPollListener) {
        if (mPollOperations == null) {
            mPollOperations = new ArrayList<PollOperation>();
        }
        if(mPollOperations.size() > 0) {
            for (PollOperation opr : mPollOperations) {
                if (opr.getRequestURL().equals(aURL)) {
                    return opr;
                }
            }
        }
        RealVehicleRequestStatusPollOperation pollOperation = new RealVehicleRequestStatusPollOperation();

        if (add(pollOperation)) {
            pollOperation.setOnCheckPollListener(onCheckPollListener);
            return pollOperation;
        }
        return null;

    }

    public PollOperation createPollRequest(String operation, String aURL, String body, PollOperation.OnCheckPollListener listener) {
        PollOperation pollOperation = create(aURL, listener);
        if (pollOperation == null) {
            Log.e(TAG, "Poll operation already created "+operation);
            return null;
        }
        VerizonHttpRequest aHttpRequest = (VerizonHttpRequest)createRequest();
        aHttpRequest.setUrl(aURL);
        HashMap<String, String> map = (HashMap<String, String>)aHttpRequest.getHeaders();
        if (map == null)
            map = new HashMap<String, String>();
        aHttpRequest.setMethod(RestClient.POST);
        aHttpRequest.setData(body);
        aHttpRequest.setHeaders(map);

        // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
        // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
        aHttpRequest.setTag(TAG);

        submit(aHttpRequest);
        pollOperation.setOperation(operation);
        pollOperation.setRequest(aHttpRequest);
        return pollOperation;
    }

    PollOperation getPollOperation(RestClient.HttpRequest aReq) {
        for (PollOperation opr : mPollOperations) {
            if (opr.getRequestURL().equals(aReq.getUrl()))
                return opr;
        }
        return null;
    }

    public void handleErrorForOperation(Response aResponse, PollOperation pollOperation) {
        if (pollOperation == null) {
            return;
        }
        pollOperation.setResponse(aResponse);
        RealVehicleEventManager.instance().onComplete(pollOperation);
        mPollOperations.remove(pollOperation);

    }
    public void handleResponseForOperation(Response aResponse, PollOperation pollOperation) {
        pollOperation.setResponse(aResponse);
        if (pollOperation.checkIfPollNeeded()) {
            Log.d(TAG, "Poll Request url : " + pollOperation.getRequestURL());
            if(pollOperation.getRemainingPolls() > 0) {
                RetryTimer aRetryTimer = pollOperation.getRetryTimer();
                if (aRetryTimer != null) {
                    // Poll is done at an poll interval of 5 second and the total poll duration is 5 min- Total no of polls is 60
                    aRetryTimer.start();
                }
                pollOperation.decrementPolls();
            }else{
                // Poll count reached 60 - stop polling
                pollOperation.setResponse(aResponse);
                RealVehicleEventManager.instance().onComplete(pollOperation);
                mPollOperations.remove(pollOperation);
            }

        }
        else {
            // Finish poll operation
            pollOperation.setResponse(aResponse);
            RealVehicleEventManager.instance().onComplete(pollOperation);
            mPollOperations.remove(pollOperation);
        }
    }

    @Override
    public void onResponse(RestClient.HttpResponse aResponse, RestClient.HttpRequest aReq) {
        PollOperation pollOperation = getPollOperation(aReq);

        if (pollOperation == null) {
            return;
        }
        Response lResponse = new Response();

        if (aResponse == null || aResponse.getDataAsString().isEmpty()) {
            pollOperation.setResponse("null");
            RealVehicleEventManager.instance().onComplete(pollOperation);
            mPollOperations.remove(pollOperation);
        }

        Log.d(TAG, "RESPONSE is : " + aResponse.getDataAsString());
        try {
            JSONObject json = new JSONObject(aResponse.getDataAsString());
            // set response.
            lResponse.setData(json.has("Data") ? json.getJSONObject("Data") : json.getJSONObject("data"));
            lResponse.setHttpStatusCode(aResponse.getHttpStatus());
            JSONObject responseJson = json.has("Response") ? json.getJSONObject("Response") : json.getJSONObject("response");
            lResponse.setResponseStatus(responseJson.getString("ResponseStatus"));
            lResponse.setResponseCode(responseJson.getInt("ResponseCode"));
            lResponse.setResponseDescription(responseJson.getString("ResponseDescription"));
             if(lResponse.getResponseCode() != AppConstants.REQUEST_SUCCESS){
                // this is error .
                handleErrorForOperation(lResponse, pollOperation);
                return;
            }
            handleResponseForOperation(lResponse,  pollOperation);

        } catch( JSONException e ){
            lResponse.setHttpStatusCode(aResponse.getHttpStatus());
            lResponse.setResponseDescription("Invalid response "+aResponse.getDataAsString());
            handleErrorForOperation(lResponse, pollOperation);
        }
    }

    @Override
    public void onError(RestClient.HttpResponse aData, RestClient.HttpRequest aReq) {
        PollOperation pollOperation = getPollOperation(aReq);
        if (pollOperation == null) {
            return;
        }
        Response lResponse = new Response();
        lResponse.setResponseDescription("error in getting vehicle location");
        lResponse.setResponseStatus("Error");
        pollOperation.setResponse(lResponse);
        RealVehicleEventManager.instance().onComplete(pollOperation);
        mPollOperations.remove(pollOperation);
    }

    public void clean() {

        // Check if no poll operation are created.
        if(mPollOperations == null || mPollOperations.isEmpty()) {
            return;
        }

        // Clean each poll operation.
        for(PollOperation pollOpr : mPollOperations){
            pollOpr.clean();
        }
        // remove all poll operations
        mPollOperations.clear();
        mPollOperations = null;

        //Clear Event manager
        RealVehicleEventManager.instance().clean();
    }
}
