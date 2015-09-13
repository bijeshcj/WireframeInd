package com.verizontelematics.indrivemobile.controllers;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.activity.LoginActivity;
import com.verizontelematics.indrivemobile.activity.NewDeviceLoginActivity;
import com.verizontelematics.indrivemobile.activity.SplashIntermediateActivity;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;
import com.verizontelematics.indrivemobile.userprofile.UserFactory;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;

import java.util.List;
import java.util.Map;

/**
 * Created by bijesh on 1/21/2015.
 */
public class ControllerUtils implements ControllerConstants{

    private static final String TAG = ControllerUtils.class.getCanonicalName();


    public static void appLogout(Context context){
        boolean isLoginOnTop = isLoginActivityOnTop(context);
        if(!isLoginOnTop) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(loginIntent);
        }
        UserFactory.clearFactoryInstance();
        InDrivePreference.getInstance().setBooleanData("login", false);
    }


    private static boolean isLoginActivityOnTop(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        String activityName =  taskInfo.get(0).topActivity.getClassName();
//        ComponentName componentInfo = taskInfo.get(0).topActivity;
//        componentInfo.getPackageName();
        Log.d(TAG,"Current Top Activity :: "+activityName);
        if(activityName.equals("com.verizontelematics.indrivemobile.activity.LoginActivity")){
            return true;
        }else
            return false;
    }

    public static void validateSessionToken(RestClient.HttpResponse aResponse){
        String responseString = aResponse.getDataAsString();
        final String ERROR_CODE = String.valueOf(ERROR_CODE_TOKEN_EXPIRED);
        if(responseString != null && responseString.contains(ERROR_CODE)){
            //             show splash and call the Authenticate
//            showSplash(IndriveApplication.getInstance().getApplicationContext());
//            showToast(IndriveApplication.getInstance().getApplicationContext());
            AuthenticateController.instance().refresh(IndriveApplication.getInstance(), AppController.instance().getMobileUserId(), AppController.instance().getPasword());
//            InDrivePreference.getInstance().setBooleanData("login", false);
        }

    }

    public static void validateCertificatesErrorResponse(RestClient.HttpResponse aResponse){
        String responseString = aResponse.getDataAsString();
        if(responseString != null && (responseString.contains(ERROR_CODE_MISSING_CERTIFICATE) || responseString.contains(ERROR_CODE_INVALID_CERTIFICATE)
           || responseString.contains(ERROR_CODE_EXPIRED_CERTIFICATE))){
            Intent intent = new Intent(IndriveApplication.getInstance(), NewDeviceLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            IndriveApplication.getInstance().startActivity(intent);
            UserFactory.clearFactoryInstance();
            InDrivePreference.getInstance().setBooleanData("login", false);
        }
    }



    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private static void showSplash(Context context){
        Intent intent = new Intent(context, SplashIntermediateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void persistResponseToken(RestClient.HttpResponse aResponse){
        Map<String,String> headers = aResponse.getHeaders();
        if(headers != null){
            Log.d(TAG, "$$$ All headers " + headers);
            String token = headers.get("token");
            if(token != null)
               AppController.instance().setSessionToken(token);
        }
    }


    public static <T extends BaseController> void refreshControllerCache(T anyController){
        if(anyController instanceof DiagnosticController){
            DiagnosticController.instance().getVehicleHealthModel().clear();
        }
    }
}
