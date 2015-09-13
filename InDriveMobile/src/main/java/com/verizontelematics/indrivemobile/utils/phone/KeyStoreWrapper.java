package com.verizontelematics.indrivemobile.utils.phone;

import android.util.Log;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

/**
 * Class: KeyStoreWrapper
 * Author: z688522 on 9/18/14.
 * Description: SingleTon Class which provide a wrapper for KeyStore class
 *              secret key's can be stored an retrieved from android's default keystore.
 *              Some devices keystore may not available or locked then secret key cannot store
 *              in KeyStore. So developer responsibility to store key in preference file.
 */
public class KeyStoreWrapper  implements KeyStoreInterface {

    private static final String TAG = KeyStoreWrapper.class.getCanonicalName();
    private KeyStoreInterface mKSImpl;
    public static final String APP_KEY_NAME = "indrive-app-key";
    private static KeyStoreWrapper ourInstance = new KeyStoreWrapper();

    /*
    ** get  the instance of KeyStore
    */
    public static KeyStoreWrapper getInstance() {
        return ourInstance;
    }

    /*
    ** initializes the KeyStore instance.
    */
    private KeyStoreWrapper() {
        mKSImpl = KeyStoreWrapper.create("AndroidKeyStore");
        if (mKSImpl == null) {
            mKSImpl = KeyStoreWrapper.create("PreferenceKeyStore");
        }
    }

    private static KeyStoreInterface create(String factory) {

        if (factory.equalsIgnoreCase("AndroidKeyStore")) {
            Log.d(TAG, "layer7 AndroidKeyStore");
            return AndroidKeyStoreImpl.getInstance();
        }
        if (factory.equalsIgnoreCase("PreferenceKeyStore")) {
            Log.d(TAG,"layer7 PreferenceKeyStore");
            return PreferenceKeyStoreImpl.getInstance();
        }
        return null;
    }


    @Override
    public void setKey(Key aKey) {
        mKSImpl.setKey(aKey);
    }

    @Override
    public Key getKey() {
        return mKSImpl.getKey();
    }

    @Override
    public void store(String property, Key aKey) {
        mKSImpl.store(property, aKey);
    }

    @Override
    public Key get(String property) {
        return mKSImpl.get(property);
    }

    @Override
    public void store(String alias, Certificate cert) throws KeyStoreException {
        mKSImpl.store(alias, cert);
    }

    @Override
    public void store(String alias, byte[] key, Certificate[] certChain) throws KeyStoreException {
        mKSImpl.store(alias, key, certChain);
    }

    @Override
    public Certificate getCertificate(String alias) throws KeyStoreException {
        return mKSImpl.getCertificate(alias);
    }

    @Override
    public KeyStore getKeyStore() {
        return mKSImpl.getKeyStore();
    }
}
