package com.verizontelematics.indrivemobile.utils.security;

import android.util.Log;

import java.security.PrivateKey;

/**
 * Created by Bijesh on 25-04-2015.
 */
public class PKCS12Attributes {

    private static final String TAG = PKCS12Attributes.class.getCanonicalName();
    private static PKCS12Attributes mInstance;
    private PrivateKey privateKey;

    private PKCS12Attributes(){

    }

    public static PKCS12Attributes getInstance(){
        if(mInstance == null){
            mInstance = new PKCS12Attributes();
        }
        return mInstance;
    }


    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        Log.d(TAG, "layer7 private key "+privateKey);
        this.privateKey = privateKey;
    }
}
