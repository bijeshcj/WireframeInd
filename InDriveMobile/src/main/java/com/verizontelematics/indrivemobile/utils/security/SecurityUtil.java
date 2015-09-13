package com.verizontelematics.indrivemobile.utils.security;

import android.util.Log;

import com.verizontelematics.indrivemobile.controllers.AppController;

/**
 * Created by bijesh on 4/30/2015.
 */
public class SecurityUtil {

    private static final String TAG = SecurityUtil.class.getCanonicalName();


    private static boolean hasToReloadCertificate(){
        boolean retFlag = false;



        return retFlag;
    }


    public static void requiresCertificateReload(){

    }


    public static String getCurrentUserPk12FileName(){
        String retString = AppController.instance().getMobileUserId()+".p12";
        Log.d(TAG, "gettting pk12 for the current user "+retString);
        return retString;
    }


}
