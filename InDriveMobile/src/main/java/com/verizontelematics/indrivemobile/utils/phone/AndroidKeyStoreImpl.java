package com.verizontelematics.indrivemobile.utils.phone;

import android.util.Log;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * Created by z688522 on 9/22/14.
 */
public class AndroidKeyStoreImpl implements KeyStoreInterface {

    private static final String TAG = AndroidKeyStoreImpl.class.getCanonicalName();
    private KeyStore mKS = null;
    private static AndroidKeyStoreImpl mInstance = null;
    /*
    ** Method : getKey()
    ** Return : SecretKey which was stored in KeyStore.
    **          returns null if no key found or KeyStore support is not available in device.
    */
    @Override
    public Key getKey() {
        try {
            return mKS.getKey(KeyStoreWrapper.APP_KEY_NAME, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void store(String property, Key aKey) {
        try {
            mKS.setKeyEntry(property, aKey, null, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Key get(String property) {
        try {
            return mKS.getKey(property, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void store(String alias, Certificate cert) throws KeyStoreException {
        Log.d(TAG, "layer7 alias "+alias+" cert "+cert);
        mKS.setCertificateEntry(alias, cert);
    }

    @Override
    public void store(String alias, byte[] key, Certificate[] certChain) throws KeyStoreException {
        mKS.setKeyEntry(alias, key, certChain);
    }

    @Override
    public Certificate getCertificate(String alias) throws KeyStoreException {
        return mKS.getCertificate(alias);
    }

    /*
    ** Method : setKey()
    ** parameters : Key
    ** return : SecretKey to store in KeyStore and returns true for success store operation.
    **          returns false in case KeyStore not available or locked in device.
    */
    @Override
    public void setKey(Key key) {
        try {
            mKS.setKeyEntry(KeyStoreWrapper.APP_KEY_NAME, key, null, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public static KeyStoreInterface getInstance() {
        if (mInstance == null) {
            mInstance = new AndroidKeyStoreImpl();
        }
        try {
            mInstance.mKS = KeyStore.getInstance("PKCS12", "BC");
            mInstance.mKS.load(null);
            return mInstance;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        mInstance = null;
        return mInstance;
    }

    public KeyStore getKeyStore() {
        return mKS;
    }
}
