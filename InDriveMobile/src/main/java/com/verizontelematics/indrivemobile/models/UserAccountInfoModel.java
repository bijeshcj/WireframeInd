package com.verizontelematics.indrivemobile.models;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.ControllerUtils;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.models.data.UserAccountData;
import com.verizontelematics.indrivemobile.models.data.UserAccountInfoData;
import com.verizontelematics.indrivemobile.models.data.UserDetail;
import com.verizontelematics.indrivemobile.models.data.UserPreferenceData;
import com.verizontelematics.indrivemobile.models.data.Vehicle;
import com.verizontelematics.indrivemobile.utils.ui.DBUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z688522 on 3/6/15.
 */
public class UserAccountInfoModel extends BaseModel {

    private static final String TAG = UserAccountInfoModel.class.getCanonicalName();
    List<UserDetail> UserDetails;

    public UserAccountInfoModel() {
        mData = new UserAccountData();
    }
    public void setData(JSONObject inData) throws JSONException {
        // parse input data as per the format
        // store the corresponding.
        //
        // mUserPreferenceData = extract the UserVehicles
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<UserDetail>>(){}.getType();
        String strData = inData.get("UserDetails").toString();
        Log.d(TAG,"strData is "+strData);
        if (strData !=null && !strData.isEmpty() && !strData.equalsIgnoreCase("null")) {
//            UserDetails = gson.fromJson(inData.getJSONArray("UserDetails").toString(), listType);
            // mUserAcctInfoData = extract the UserAccountInfoData
            DBUtils.clearLoginModuleDBData(IndriveApplication.getInstance());
            StorageTransaction storageTransaction = new StorageTransaction(IndriveApplication.getInstance());
            UserDetails = new ArrayList<UserDetail>();
            UserDetail userDetail;
            JSONArray userDetailsJSONArray = inData.getJSONArray("UserDetails");
            for (int i = 0; i < userDetailsJSONArray.length(); i++) {
                userDetail = new UserDetail();
                JSONObject userDetailsObject = (JSONObject) userDetailsJSONArray.get(i);
                UserAccountInfoData userAccountInfoData = gson.fromJson(userDetailsObject.get("AccountInfo").toString(), UserAccountInfoData.class);
                userDetail.setAccountInfo(userAccountInfoData);


//
//            // mUserVehicles = extract the UserVehicles
                JSONObject vehicleInfoObj = userDetailsObject.getJSONObject("VehiclesInfo");
                Type listVehileType = new TypeToken<ArrayList<Vehicle>>() {}.getType();
                String strVehicleLstData = vehicleInfoObj.get("Vehicle").toString();
                if (strVehicleLstData != null && !strVehicleLstData.isEmpty() && !strVehicleLstData.equalsIgnoreCase("null")) {
                    List<Vehicle> vehicles = gson.fromJson(vehicleInfoObj.getJSONArray("Vehicle").toString(), listVehileType);
                    userDetail.setVehiclesInfo((ArrayList) vehicles);
                }else{
                    Toast.makeText(IndriveApplication.getInstance(), IndriveApplication.getInstance().getResources().getString(R.string.kitchen_sink_failure), Toast.LENGTH_LONG).show();
                    ControllerUtils.appLogout(IndriveApplication.getInstance());
                }
                // mUserPreferenceData = extract the UserVehicles
                UserPreferenceData userPreferenceData = gson.fromJson(userDetailsObject.get("NotificationPreferenceInfo").toString(), UserPreferenceData.class);
                userDetail.setNotificationPreferenceInfo(userPreferenceData);
                UserDetails.add(userDetail);
                storageTransaction.insertLoginModule(userAccountInfoData,userDetail.getVehiclesInfo(),userPreferenceData);

            }
        }
        setChanged();
        notifyObservers();
    }

    public List<UserDetail> getUserDetails() {
        return UserDetails;
    }

    public void setUserDetails(ArrayList<UserDetail> userDetails) {
        UserDetails = userDetails;
    }

    public UserDetail getPrimary() {
        if (UserDetails == null)
            return null;
        for(UserDetail userDetail : UserDetails) {
            if (userDetail.getAccountInfo().getAccountType().equalsIgnoreCase("Primary")) {
                return userDetail;
            }
        }
        return null;
    }
}
