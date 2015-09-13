package com.verizontelematics.indrivemobile.utils.phone;

import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by z688522 on 9/22/14.
 */
public class PreferenceKeyStoreImpl implements KeyStoreInterface {

    private static PreferenceKeyStoreImpl mInstance = null;
    private static final int KEY_LENGTH = 32;

    /*
    ** Method : setKey()
    ** parameters : String
    ** return : stores key in Application's Preference file as a string and returns true for successful operation.
    **          returns false for unsuccessful operation.
    **
    */
    @Override
    public void setKey(Key aKey) {
        SecretKey key = (SecretKey) aKey;
        String strKey = null;
        try {
            strKey = new String(key.getEncoded(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InDrivePreference.getInstance().setStringData(KeyStoreWrapper.APP_KEY_NAME, strKey);
    }

    public void setKey(String aKey) {
        InDrivePreference.getInstance().setStringData(KeyStoreWrapper.APP_KEY_NAME, aKey);
    }

    /*
    ** Method : getKeyString()
    ** parameters : String
    ** return : returns key from Application's Preference file
    */
    @Override
    public Key getKey() {
        if (InDrivePreference.getInstance() != null) {
            String strKey =  InDrivePreference.getInstance().getStringData(KeyStoreWrapper.APP_KEY_NAME, "");
            if(strKey == null || strKey.length() == 0){
                strKey = "password";
            }
            byte[] lByes = new byte[KEY_LENGTH];
            StringBuilder temp= new StringBuilder(strKey);
            if(temp.toString().getBytes().length < KEY_LENGTH) {
                do {
                    temp.append(temp);
                } while (temp.toString().getBytes().length < KEY_LENGTH);
            }

            setKey(temp.toString());

            ByteBuffer.wrap(temp.toString().getBytes(), 0, KEY_LENGTH).get(lByes);
            SecretKey key = null;
            key = new SecretKeySpec(lByes, CryptoWrapper.CIPHER_ALGORITHM);
            return key;
        }
        return null;
    }

    @Override
    public void store(String property, Key aKey) {
        SecretKey key = (SecretKey) aKey;
        String strKey = null;
        try {
            strKey = new String(key.getEncoded(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InDrivePreference.getInstance().setStringData(property, strKey);
    }

    @Override
    public Key get(String property) {
        if (InDrivePreference.getInstance() != null) {
            String strKey =  InDrivePreference.getInstance().getStringData(property, "");
            if(strKey == null || strKey.length() == 0){
                strKey = "password";
            }
            byte[] lByes = new byte[KEY_LENGTH];
            StringBuilder temp= new StringBuilder(strKey);
            if(temp.toString().getBytes().length < KEY_LENGTH) {
                do {
                    temp.append(temp);
                } while (temp.toString().getBytes().length < KEY_LENGTH);
            }

            setKey(temp.toString());

            ByteBuffer.wrap(temp.toString().getBytes(), 0, KEY_LENGTH).get(lByes);
            SecretKey key = null;
            key = new SecretKeySpec(lByes, CryptoWrapper.CIPHER_ALGORITHM);
            return key;
        }
        return null;
    }

    @Override
    public void store(String alias, Certificate cert) throws KeyStoreException {
        InDrivePreference.getInstance().setCustomObject(alias, cert);
    }

    @Override
    public void store(String alias, byte[] key, Certificate[] certChain) throws KeyStoreException {
        InDrivePreference.getInstance().setStringData(alias, new String(key));
        InDrivePreference.getInstance().setCustomObject(alias, certChain);
    }

    @Override
    public Certificate getCertificate(String alias) throws KeyStoreException {
        return InDrivePreference.getInstance().getCustomObject(alias, Certificate.class);
    }

    public static KeyStoreInterface getInstance() {
        if (mInstance == null) {
            mInstance = new PreferenceKeyStoreImpl();
        }
        return mInstance;
    }

    @Override
    public KeyStore getKeyStore() {
        return null;
    }
}
