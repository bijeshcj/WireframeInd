package com.verizontelematics.indrivemobile.utils.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.verizontelematics.indrivemobile.utils.AppConstants;

import java.lang.reflect.Type;

/**
 * Created by z688522 on 9/19/14.
 */
public class InDrivePreference {

    private static final String TAG = InDrivePreference.class.getCanonicalName();
    private static final String INDRIVE_PREFERENCE_NAME = "InDrive_Preference";
    private static InDrivePreference mInstance = null;
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;
    private Gson gson = new Gson();

    //Preference Strings
    public static final String SERVICE_TYPE ="ServiceType";

    @SuppressLint("CommitPrefEdits")
    public InDrivePreference(Context context) {
        mPreference = context.getSharedPreferences(INDRIVE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mPreference.edit();
    }

    public static InDrivePreference getInstance() {

        return mInstance;
    }

    public static InDrivePreference createInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new InDrivePreference(ctx);
            String deviceId  = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
            mInstance.setStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,deviceId);
            Log.d(TAG,"$$$ device unique id "+mInstance.getStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,""));
        }
        return mInstance;
    }

    //Set Methods for all types.

    public void setStringData(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }



    public void setIntData(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void setBooleanData(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void setFloatData(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void setLongData(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    //To Store Custom Object

    public void setCustomObject(String key, Object customObj) {
        String jsonStr = gson.toJson(customObj);
        mEditor.putString(key, jsonStr);
        mEditor.commit();
    }

    //Get Methods for all types.

    public String getStringData(String key, String defaultValue) {
        return mPreference.getString(key, defaultValue);
    }

    public int getIntData(String key, int defaultValue) {
        return mPreference.getInt(key, defaultValue);
    }

    public boolean getBooleanData(String key, boolean defaultValue) {
        return mPreference.getBoolean(key, defaultValue);
    }

    public float getFloatData(String key, float defaultValue) {
        return mPreference.getFloat(key, defaultValue);
    }

    public long getLongData(String key, long defaultValue) {
        return mPreference.getLong(key, defaultValue);
    }

    //To Get Custom Object
    public <T> T getCustomObject(String key, Class<T> customClass) {
        return gson.fromJson(mPreference.getString(key, ""), customClass);

    }

    public  <T> T getCustomObject(String key, Type type) {
        return gson.fromJson(mPreference.getString(key, ""), type);

    }

    //To Clear the Preference
    public void clearPreference()
    {
        mEditor.clear().commit();
    }


}
