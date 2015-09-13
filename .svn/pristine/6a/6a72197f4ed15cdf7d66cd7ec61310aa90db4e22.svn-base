package com.verizontelematics.indrivemobile.connection.utils;

import android.util.Base64;

/**
 * Created by bijesh on 2/16/2015.
 */
public class Base64Util {
    public static String getB64Auth (String login, String pass) {
        String source=login+":"+pass;
        String ret="Basic "+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }

    public static String getStringAsBase64(String string){
        return Base64.encodeToString(string.getBytes(),Base64.URL_SAFE | Base64.NO_WRAP);
    }


    public static String decodeBase64AsString(String string){
        return new String(Base64.decode(string,Base64.URL_SAFE | Base64.NO_WRAP));
    }

    public static byte[] decodeBase64AsByte(byte[] bytes){
        return Base64.encode(bytes,Base64.URL_SAFE | Base64.NO_WRAP);
    }


}
