package com.verizontelematics.indrivemobile.controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.Response;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.VerizonHttpRequest;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;
import com.verizontelematics.indrivemobile.models.BaseModel;
import com.verizontelematics.indrivemobile.models.DrivingDataModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.DrivingData;
import com.verizontelematics.indrivemobile.models.data.DrivingDataControllerOperationData;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.NetworkUtil;
import com.verizontelematics.indrivemobile.utils.config.Config;
import com.verizontelematics.indrivemobile.utils.config.ConfigKeys;
import com.verizontelematics.indrivemobile.utils.ui.CalendarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by z688522 on 12/9/14.
 */
public class DrivingDataController extends BaseController {

    private static final String  TAG = "DrivingDataController";

    private static final int DRIVING_DATA_MODEL = 0;

    private static DrivingDataController mInstance = null;

    public static DrivingDataController instance() {
        if (mInstance == null)
            mInstance = new DrivingDataController();
        return mInstance;
    }

    // DrivingData controller Operation Codes.
    private int GET_DRIVING_DATA = Operation.OperationCode.GET_DRIVING_DATA.ordinal();


    private OperationFactory mOperationFactory = DrivingDataControllerOperationFactory.instance();

    private DrivingDataController() {
        super();
        mModels.add(new DrivingDataModel());

    }

    public BaseModel getDrivingDataModel() {
        return mModels.get(DRIVING_DATA_MODEL);
    }




    public Operation getDrivingData(Context ctx, long start_time, long end_time) {

        // Need to remove from here and move to UI where the call made.
        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(start_time);
        temp.set(temp.get(Calendar.YEAR),temp.get(Calendar.MONTH),temp.get(Calendar.DATE),0,0,0);
        start_time= temp.getTimeInMillis() + CalendarUtil.getOffSetGMTTime(temp);
        start_time = CalendarUtil.getUtcOfCurrentTime(start_time);//CalendarUtil.getGMTTime(start_time);

        temp.setTimeInMillis(end_time);
        temp.set(temp.get(Calendar.YEAR),temp.get(Calendar.MONTH),temp.get(Calendar.DATE),23,59,59);
        end_time = temp.getTimeInMillis() + CalendarUtil.getOffSetGMTTime(temp);
        end_time = CalendarUtil.getUtcOfCurrentTime(end_time);
//        start_time = 1434844800000L; // TODO: remove after testing
        ////////////////////////////////////////
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(GET_DRIVING_DATA, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest)createRequest();
//                System.out.println("$$$ url "+getHost()+ Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_FETCH_DRIVING_DATA));
//                String url = "https://portali.hughestelematics.com/HTIWebGateway/EnterpriseGatewayServices/SFUS_DrivingReport/GetDrivingData";
                aHttpRequest.setUrl(getHost()+ Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_FETCH_DRIVING_DATA));
//                aHttpRequest.setUrl("https://portali.hughestelematics.com/HTIWebGateway/EnterpriseGatewayServices/SFUS_DrivingReport/GetDrivingData");
//                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>)aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(GET_DRIVING_DATA));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBody(start_time, end_time));
                Log.v(TAG,"Driving data request is ----->"+prepareBody(start_time, end_time));


                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(GET_DRIVING_DATA);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            }
            else{
                Operation opr = mOperationsModel.getOperation(GET_DRIVING_DATA);
                opr.setState(Operation.ERROR);
                ((DrivingDataControllerOperationData)opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(GET_DRIVING_DATA);
    }

    private String prepareBody(long start_time, long end_time) {
        // Stubbed parameter
        String str = "{"
                +    "\"Data\"" + ":"+ "{"
                +        "\"VehicleID\"" + ":" + "\""+ AppController.instance().getVehicleId() +"\""+","
                +        "\"StartDate\"" + ":" + "\""+start_time+"\""+","
                +        "\"EndDate\""   + ":" + "\""+end_time+"\""
                +    "}"
                +"}";
        // Stub ended.
        return str;
    }

    public void handleErrorForOperation(Response aResponse, Operation aOperation) {

        aOperation.setState(Operation.ERROR);
        aOperation.setInformation(aResponse.getResponseDescription());
        onError(aOperation);
        getDrivingDataModel().clear();
    }

    public void handleResponseForOperation(Response aResponse, Operation aOperation){
        Log.d(TAG, "handleResponse for operation " + aOperation.getClass());

        try {
            Gson gson = new GsonBuilder().create();
            JSONObject lData = aResponse.getData();
            if (aOperation.getId() == Operation.OperationCode.GET_DRIVING_DATA.ordinal()) {
                // parse and extract data

                String strDrivingDataInfo = lData.get("DrivingData").toString();


                if (!strDrivingDataInfo.isEmpty() && !strDrivingDataInfo.equalsIgnoreCase("null")) {
                    // set date to DrivingData model.
                    Type listType = new TypeToken<ArrayList<DrivingData>>(){}.getType();
                    List<DrivingData> drivingDataList = gson.fromJson(lData.getJSONArray("DrivingData").toString(), listType);

                    if (drivingDataList != null) {
                        DrivingDataModel lModel = (DrivingDataModel) getDrivingDataModel();
                        lModel.setData(drivingDataList);
                    }

                }
                else {
                    getDrivingDataModel().clear();
                }
            }
            aOperation.setState(Operation.FINISHED);
            onSuccess(aOperation);

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
}
