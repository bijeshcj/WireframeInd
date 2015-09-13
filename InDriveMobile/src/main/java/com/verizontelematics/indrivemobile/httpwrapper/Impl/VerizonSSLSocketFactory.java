package com.verizontelematics.indrivemobile.httpwrapper.Impl;

import android.util.Log;

import com.github.mikephil.charting.utils.FileUtils;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.connection.utils.Base64Util;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by z688522 on 2/16/15.
 */
public class VerizonSSLSocketFactory {
    private static final String TAG = VerizonSSLSocketFactory.class.getCanonicalName();
    private static VerizonSSLSocketFactory ourInstance;
    private SSLContext context;


    /*
    ** Get the instance of KeyStore
    */
    synchronized public static VerizonSSLSocketFactory instance() {
        if (ourInstance == null) {
            ourInstance = new VerizonSSLSocketFactory();
            ourInstance.init();
        }
        return ourInstance;
    }

    private void init() {
        // SSLSocketFactory Creation.
        // Create X509KeyManager using CSR stored in key store.
        // below is stub.
//        KeyStoreInterface keyStoreInterface = KeyStoreWrapper.getInstance().getKeyStore();
//        if (!AndroidKeyStoreImpl.class.isInstance(keyStoreInterface)) {
//            return;
//        }
//
//        KeyStore keyStore = ((AndroidKeyStoreImpl)keyStoreInterface).getKeyStore();
//        String algorithm = KeyManagerFactory.getDefaultAlgorithm();
//        KeyManagerFactory kmf = null;
//        try {
//            kmf = KeyManagerFactory.getInstance(algorithm);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            kmf.init(keyStore, KeyStoreWrapper.getInstance().getKey().getEncoded().toString().toCharArray());
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        }
        // stub ended.

        try {
            context = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (context != null) {
            TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{ (X509Certificate)getCertificate() };
                }
            };

            if (tm != null) {
                try {
                    KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
                    KeyStore keyStore = KeyStore.getInstance("PKCS12");
                    keyStore.load(null, "sree".toCharArray());
                    final X509Certificate x509Certificate = (X509Certificate) getCertificate("dfd");
//                    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//                    keyGen.initialize(2048);
//                    KeyPair keyPair = keyGen.generateKeyPair();
//                    Certificate cert = x509Certificate;
//                    keyStore.setKeyEntry(x509Certificate.getSubjectX500Principal().getName(), keyPair.getPrivate().getEncoded(), new Certificate[]{cert});
//                    kmf.init(keyStore, "sree".toCharArray());

                    // context.init(kmf.getKeyManagers(), getTrustManagers1(), null);
                    // context.init(null, new TrustManager[]{ tm }, null);
                    X509KeyManager km = new X509KeyManager() {


                        @Override
                        public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
                            Log.d(TAG, "");
                            return x509Certificate.getSigAlgName();
                        }

                        @Override
                        public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
                            Log.d(TAG, "");
                            return null;
                        }

                        @Override
                        public X509Certificate[] getCertificateChain(String s) {
                            Log.d(TAG, "");
                            return new X509Certificate[] { x509Certificate };
                        }

                        @Override
                        public String[] getClientAliases(String s, Principal[] principals) {
                            Log.d(TAG, "");
                            return new String[]{ x509Certificate.getSubjectX500Principal().getName()};
                        }

                        @Override
                        public String[] getServerAliases(String s, Principal[] principals) {
                            Log.d(TAG, "");
                            return new String[0];
                        }

                        @Override
                        public PrivateKey getPrivateKey(String s) {

                            return null;
                        }
                    };
                    context.init(new KeyManager[]{ km }, getTrustManagers1(), null);
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public SSLSocketFactory getSocketFactory() {
        if (context == null)
            return null;
        return  context.getSocketFactory();
    }



    private Certificate getCertificate() {
        CertificateFactory cf = null;
        InputStream caInput = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        }catch (CertificateException e){
            e.printStackTrace();
        }
        Certificate ca = null;
        Log.d(TAG,"layer7 getting Cert1");
//        caInput = new BufferedInputStream(new ByteArrayInputStream(Base64Util.getStringAsBase64(InDrivePreference.getInstance().getStringData("cert","")).getBytes()));
        caInput = new BufferedInputStream(new ByteArrayInputStream(Base64Util.getStringAsBase64(com.verizontelematics.indrivemobile.utils.FileUtils.readCertFromAppInternal(IndriveApplication.getInstance())).getBytes()));
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        }catch (CertificateException e){
            e.printStackTrace();
        }
        return ca;
    }

    private Certificate getCertificate(String cartfilepath) {
        CertificateFactory cf = null;
        InputStream caInput = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        }catch (CertificateException e){
            e.printStackTrace();
        }
        Certificate ca = null;
        String str1 = "-----BEGIN CERTIFICATE-----\n" +
                "MIIEADCCAuigAwIBAgIBADANBgkqhkiG9w0BAQUFADBjMQswCQYDVQQGEwJVUzEh\n" +
                "MB8GA1UEChMYVGhlIEdvIERhZGR5IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBE\n" +
                "YWRkeSBDbGFzcyAyIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA0MDYyOTE3\n" +
                "MDYyMFoXDTM0MDYyOTE3MDYyMFowYzELMAkGA1UEBhMCVVMxITAfBgNVBAoTGFRo\n" +
                "ZSBHbyBEYWRkeSBHcm91cCwgSW5jLjExMC8GA1UECxMoR28gRGFkZHkgQ2xhc3Mg\n" +
                "MiBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTCCASAwDQYJKoZIhvcNAQEBBQADggEN\n" +
                "ADCCAQgCggEBAN6d1+pXGEmhW+vXX0iG6r7d/+TvZxz0ZWizV3GgXne77ZtJ6XCA\n" +
                "PVYYYwhv2vLM0D9/AlQiVBDYsoHUwHU9S3/Hd8M+eKsaA7Ugay9qK7HFiH7Eux6w\n" +
                "wdhFJ2+qN1j3hybX2C32qRe3H3I2TqYXP2WYktsqbl2i/ojgC95/5Y0V4evLOtXi\n" +
                "EqITLdiOr18SPaAIBQi2XKVlOARFmR6jYGB0xUGlcmIbYsUfb18aQr4CUWWoriMY\n" +
                "avx4A6lNf4DD+qta/KFApMoZFv6yyO9ecw3ud72a9nmYvLEHZ6IVDd2gWMZEewo+\n" +
                "YihfukEHU1jPEX44dMX4/7VpkI+EdOqXG68CAQOjgcAwgb0wHQYDVR0OBBYEFNLE\n" +
                "sNKR1EwRcbNhyz2h/t2oatTjMIGNBgNVHSMEgYUwgYKAFNLEsNKR1EwRcbNhyz2h\n" +
                "/t2oatTjoWekZTBjMQswCQYDVQQGEwJVUzEhMB8GA1UEChMYVGhlIEdvIERhZGR5\n" +
                "IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBEYWRkeSBDbGFzcyAyIENlcnRpZmlj\n" +
                "YXRpb24gQXV0aG9yaXR5ggEAMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQAD\n" +
                "ggEBADJL87LKPpH8EsahB4yOd6AzBhRckB4Y9wimPQoZ+YeAEW5p5JYXMP80kWNy\n" +
                "OO7MHAGjHZQopDH2esRU1/blMVgDoszOYtuURXO1v0XJJLXVggKtI3lpjbi2Tc7P\n" +
                "TMozI+gciKqdi0FuFskg5YmezTvacPd+mSYgFFQlq25zheabIZ0KbIIOqPjCDPoQ\n" +
                "HmyW74cNxA9hi63ugyuV+I6ShHI56yDqg+2DzZduCLzrTia2cyvk0/ZM/iZx4mER\n" +
                "dEr/VxqHD3VILs9RaRegAhJhldXRQLIQTO7ErBBDpqWeCtWVYpoNz4iCxTIM5Cuf\n" +
                "ReYNnyicsbkqWletNw+vHX/bvZ8=\n" +
                "-----END CERTIFICATE-----";
        caInput = new BufferedInputStream(new ByteArrayInputStream(str1.getBytes()));
//        try {
//            caInput = new BufferedInputStream(mContext.getResources().getAssets().open("sarek.crt"));
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            ca = cf.generateCertificate(caInput);
            Log.d(TAG, "ca=" + ((X509Certificate) ca).getSubjectDN());
            Log.d(TAG, "public key "+((X509Certificate)ca).getPublicKey());
        }catch (CertificateException e){
            e.printStackTrace();
        }
        finally {
            try {
                caInput.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ca;
    }

    private TrustManager[] getTrustManagers1(){

//        CertificateFactory cf = null;
//        InputStream caInput = null;
        TrustManagerFactory tmf = null;
        KeyStore keyStore = null;
//        // Load CAs from an InputStream
//// (could be from a resource or ByteArrayInputStream or ...)
//        try {
//            cf = CertificateFactory.getInstance("X.509");
//        }catch (CertificateException e){
//            e.printStackTrace();
//        }
////// From https://www.washington.edu/itconnect/security/ca/load-der.crt
////        try {
//////            caInput = new BufferedInputStream(new FileInputStream("/storage/emulated/0/api-sit.vtitel.com.cer"));
////            //caInput = new BufferedInputStream(new FileInputStream("/storage/emulated/0/cert.pem"));
////        }catch (FileNotFoundException e){
////            e.printStackTrace();
////        }
//        Certificate ca = null;
//        caInput = new BufferedInputStream(new ByteArrayInputStream(Base64Util.getStringAsBase64(InDrivePreference.getInstance().getStringData("cert","")).getBytes()));
//
//        try {
//            ca = cf.generateCertificate(caInput);
//            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//        }catch (CertificateException e){
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                caInput.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        Log.d(TAG, "$$$ default keyStoreType "+keyStoreType);
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            X509Certificate x509Certificate = (X509Certificate) getCertificate("dfasd");
            keyStore.setCertificateEntry(x509Certificate.getSubjectX500Principal().getName(), x509Certificate);

        }catch (Exception e){
            e.printStackTrace();
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (KeyStoreException e){
            e.printStackTrace();
        }
        return tmf.getTrustManagers();

    }

}
