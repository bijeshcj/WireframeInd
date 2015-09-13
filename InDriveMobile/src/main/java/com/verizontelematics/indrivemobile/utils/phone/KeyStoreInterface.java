package com.verizontelematics.indrivemobile.utils.phone;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

/**
 * Created by z688522 on 9/22/14.
 */
public interface KeyStoreInterface {
    public void setKey(Key aKey);
    public Key getKey();
    public void store(String property, Key aKey);
    public Key get(String property);
    public void store(String alias, Certificate cert) throws KeyStoreException;
    public void store(String alias, byte[] key, Certificate[] certChain) throws KeyStoreException;
    public Certificate getCertificate(String alias) throws KeyStoreException;
    public KeyStore getKeyStore();
}
