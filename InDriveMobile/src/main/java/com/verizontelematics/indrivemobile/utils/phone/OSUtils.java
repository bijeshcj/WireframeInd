package com.verizontelematics.indrivemobile.utils.phone;

import android.os.Build;

/**
 * Created by bijesh on 8/25/2014.
 */
public class OSUtils {
    public static boolean isKitkat(){
        boolean retFlag = false;
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT){
            retFlag = true;
        }
        return retFlag;
    }
}
