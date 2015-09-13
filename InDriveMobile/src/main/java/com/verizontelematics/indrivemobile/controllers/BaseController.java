package com.verizontelematics.indrivemobile.controllers;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.Response;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.VerizonHttpRequest;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;
import com.verizontelematics.indrivemobile.httpwrapper.RestClientFactory;
import com.verizontelematics.indrivemobile.models.BaseModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.OperationsModel;
import com.verizontelematics.indrivemobile.models.PersistRequest;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.config.Config;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;
import com.verizontelematics.indrivemobile.utils.errormanagement.AuthenticateErrorController;
import com.verizontelematics.indrivemobile.utils.errormanagement.ErrorDescriptionGenerator;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by z688522 on 10/22/14.
 */
public class BaseController implements RestClient.ResponseListener,
        RestClient.ErrorListener,ControllerConstants {

    private static final String TAG = "BaseController";
    ArrayList<BaseModel> mModels = new ArrayList<BaseModel>();
    private ArrayList<UIInterface> mUIs =  new ArrayList<UIInterface>();
    OperationsModel mOperationsModel = OperationsModel.instance();


    private AuthenticateErrorController authenticateErrorController = new AuthenticateErrorController();
    private ErrorDescriptionGenerator authenticateErrorDescriptorGenerator = new ErrorDescriptionGenerator(authenticateErrorController);

//    protected List<PersistRequest> mInProgressRequest = new CopyOnWriteArrayList<PersistRequest>();


    // Temp fix
    static private String mHost = "";
    // Temp fix ended.


    public BaseModel getModel(int index) {
        if (index < mModels.size())
            return mModels.get(index);
        return null;
    }

    public boolean register(UIInterface aUI) {
        if (aUI != null && !mUIs.contains(aUI)) {
            mUIs.add(aUI);
            return true;
        }
        return false;
    }

    public boolean unregister(UIInterface aUI) {
        if (aUI == null || mUIs.size() == 0 || !mUIs.contains(aUI)) {
            return false;
        }
        mUIs.remove(aUI);
        return true;
    }

    public void onError(Operation opr) {
        for( UIInterface aUI : mUIs) {
            aUI.onError(opr);
        }
    }

    public void onProgress(Operation opr) {
        for( UIInterface aUI : mUIs) {
            aUI.onProgress(opr);
        }
    }

    public void onSuccess(Operation opr) {
        UIInterface[] uis = new UIInterface[mUIs.size()];
        mUIs.toArray(uis);
        for( UIInterface aUI : uis) {
            aUI.onSuccess(opr);
        }
    }

    public String getHost(){
        // Read the URl details.
        if (mHost == null || mHost.isEmpty()) {
            mHost = Config.getInstance(null).getHost();
        }
        //Temp fix:  Hardcoded
        if (mHost == null || mHost.isEmpty()) {
            mHost = "http://atlmsaap02d.hughestelematics.net:8180";
        }
        // Temp fix ended.
        // above fix need to be removed once we fix Config.getInstance(null).getHost().
        return mHost;
    }

    public String getHost(String appEnvType) {
        return Config.getInstance(null).getHost(appEnvType);
    }

    public String getVehicleId() {
        return InDrivePreference.getInstance().getStringData("VehicleId", "");
    }

    public void submit(RestClient.HttpRequest aHttpRequest) {
        Log.d(TAG, " url "+aHttpRequest.getUrl());
        Log.d(TAG," $$ payload on submit "+aHttpRequest.getData());
//        PersistRequest persistRequest = new PersistRequest(aHttpRequest.getUrl(),aHttpRequest.getData());
//        mInProgressRequest.add(persistRequest);
        RestClientFactory.getDefaultRestClient().addRequest(aHttpRequest, RestResponseHandlerThread.getHandler());
    }

    public void submit(Request request){
        RestClientFactory.getDefaultRestClient().addRequest(request);
    }

    public void cancel(RestClient.HttpRequest aHttpRequest) {
        RestClientFactory.getDefaultRestClient().cancelRequest(aHttpRequest);
    }

    public void cancelAll(){
        RestClientFactory.getDefaultRestClient().cancelAll();
    }

    public RestClient.HttpRequest createRequest() {
        Log.d(TAG,"sessiontoken createRequest ");
        VerizonHttpRequest aHttpRequest = new VerizonHttpRequest();
        HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
        if (map == null) {
            map = new HashMap<String, String>();
        }
        map.put("Content-Type", "application/json; charset=UTF-8");
        map.put("api_key", "258418e2062469a73bf179805e13b98f");
        map.put(TOKEN,"Bearer "+getSessionToken());
        Log.d(TAG,"sessiontoken Request Header Token "+getSessionToken());
        aHttpRequest.setHeaders(map);
        aHttpRequest.setErrorListener(this);
        aHttpRequest.setResponseListener(this);
        return aHttpRequest;
    }

    public void clearAllModels() {
        for (BaseModel baseModel : mModels) {
            baseModel.clear();
        }
    }

    @Override
    public void onError(RestClient.HttpResponse aResponse, RestClient.HttpRequest aReq) {
        Operation opr = mOperationsModel.getOperation(aReq);
        ControllerUtils.validateSessionToken(aResponse);
        ControllerUtils.validateCertificatesErrorResponse(aResponse);
        Log.d(TAG,"$$$ handleErrorForOperation "+aResponse.getDataAsString()+" Operaation "+opr.getId()+" authid "+Operation.OperationCode.AUTHENTICATE.ordinal());
        if (opr == null) {
            return;
        }
        opr.setState(Operation.ERROR);
        opr.setInformation(aResponse.getDataAsString());
        onError(opr);
        if(opr.getId() == Operation.OperationCode.AUTHENTICATE.ordinal() || AppController.instance().getStayLoggedSetting()){
            String message = authenticateErrorDescriptorGenerator.executeGetResponseDescription(IndriveApplication.getInstance(),
                    aResponse.getDataAsString(), Operation.OperationCode.AUTHENTICATE.ordinal(),"");
            ControllerUtils.showToast(IndriveApplication.getInstance(),message);//"Session Expired. Please try again"
            ControllerUtils.appLogout(IndriveApplication.getInstance());
            return;
        }

        if(opr.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()){
            String message = authenticateErrorDescriptorGenerator.executeGetResponseDescription(IndriveApplication.getInstance(),
                    aResponse.getDataAsString(), Operation.OperationCode.VALIDATE_REGISTRATION.ordinal(),"");
            ControllerUtils.showToast(IndriveApplication.getInstance(),message);
        }


        if(opr.getId() == Operation.OperationCode.GET_USER_ACCT_VECH_DEV_INFO.ordinal()){
            ControllerUtils.showToast(IndriveApplication.getInstance(),"Could not fetch User Details. Logging out.");
            ControllerUtils.appLogout(IndriveApplication.getInstance());
        }else{
            String message = authenticateErrorDescriptorGenerator.executeGetResponseDescription(IndriveApplication.getInstance(),
                    aResponse.getDataAsString(), Operation.OperationCode.AUTHENTICATE.ordinal(),"");
            ControllerUtils.showToast(IndriveApplication.getInstance(),message);
            ControllerUtils.appLogout(IndriveApplication.getInstance());
        }
    }

    private String getSessionToken(){
        return AppController.instance().getSessionToken();
    }

    public void handleErrorForOperation(Response aResponse, Operation aOperation){
        Log.d(TAG,"handleErrorForOperation Operation "+aOperation.getId());
    }


    public void handleResponseForOperation(Response aResponse, Operation aOperation){

    }


    @Override
    public void onResponse(RestClient.HttpResponse aResponse, RestClient.HttpRequest aReq) {
        Operation opr = mOperationsModel.getOperation(aReq);

        if (opr == null) {
            return;
        }
        Response lResponse = new Response();

        ControllerUtils.persistResponseToken(aResponse);
//        Log.d(TAG,"onResponse mInProgressRequest "+mInProgressRequest);

        if (aResponse == null || aResponse.getDataAsString().isEmpty()) {
            if (opr != null) {
                //((UnifiedVehicleStatusOperationData)opr.getData()).setMessage("Fetch recent vehicle status Success");
                opr.setState(Operation.ERROR);
                onError(opr);
            }
        }
        JSONObject json = null;
        Log.d("BaseController", "RESPONSE is ---->" + aResponse.getDataAsString());
        try {
            json = new JSONObject(aResponse.getDataAsString());
            // set response.
            lResponse.setData(json.has("Data") ? json.getJSONObject("Data") : (json.has("data") ? json.getJSONObject("data") : null));
            lResponse.setHttpStatusCode(aResponse.getHttpStatus());
            JSONObject responseJson = json.has("Response") ? json.getJSONObject("Response") : json.getJSONObject("response");
            lResponse.setResponseStatus(responseJson.getString("ResponseStatus"));
            lResponse.setResponseCode(responseJson.getInt("ResponseCode"));
            lResponse.setResponseDescription(responseJson.getString("ResponseDescription"));

            opr.setState(Operation.FINISHED);
            if(lResponse.getResponseCode() != AppConstants.REQUEST_SUCCESS){
                // this is error .
                if(lResponse.getResponseCode() == AppConstants.ACCOUNT_ISSUE){
                    ControllerUtils.showToast(IndriveApplication.getInstance(),IndriveApplication.getInstance().getResources().getString(R.string.error_account_issue));
                }else if(lResponse.getResponseCode() == AppConstants.PASSWORD_ISSUE){
                    ControllerUtils.showToast(IndriveApplication.getInstance(),IndriveApplication.getInstance().getResources().getString(R.string.password_validation));
                }else if(lResponse.getResponseCode() == AppConstants.TECHNICAL_ISSUE){
                    ControllerUtils.showToast(IndriveApplication.getInstance(),IndriveApplication.getInstance().getResources().getString(R.string.error_technical_issue));
                }else if(lResponse.getResponseCode() == AppConstants.USERNAME_ISSUE){
                    ControllerUtils.showToast(IndriveApplication.getInstance(),IndriveApplication.getInstance().getResources().getString(R.string.error_username_issue));
                }else if(lResponse.getResponseCode() == AppConstants.INVALID_AUTH_CODE){
                    ControllerUtils.showToast(IndriveApplication.getInstance(),IndriveApplication.getInstance().getResources().getString(R.string.invalid_auth_code));
                }
                handleErrorForOperation(lResponse, opr);
                return;
            }
            handleResponseForOperation(lResponse, opr);

        } catch( JSONException e ){
            try {
                JSONObject responseJson = json.has("Response") ? json.getJSONObject("Response") : json.getJSONObject("response");
                lResponse.setResponseStatus(responseJson.getString("ResponseStatus"));
                lResponse.setResponseCode(responseJson.getInt("ResponseCode"));
                lResponse.setResponseDescription(responseJson.getString("ResponseDescription"));
                lResponse.setHttpStatusCode(aResponse.getHttpStatus());
//            lResponse.setResponseCode(aResponse.ge);
                lResponse.setResponseDescription("Invalid response " + aResponse.getDataAsString());
                opr.setState(Operation.ERROR);
                handleErrorForOperation(lResponse, opr);
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }



}
