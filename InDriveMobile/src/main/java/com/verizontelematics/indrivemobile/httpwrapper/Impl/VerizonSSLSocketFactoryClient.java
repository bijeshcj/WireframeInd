package com.verizontelematics.indrivemobile.httpwrapper.Impl;

import android.os.Environment;
import android.util.Log;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.utils.FileUtils;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;
import com.verizontelematics.indrivemobile.utils.phone.KeyStoreWrapper;
import com.verizontelematics.indrivemobile.utils.security.CreatePKCS12;
import com.verizontelematics.indrivemobile.utils.security.PKCS12Attributes;
import com.verizontelematics.indrivemobile.utils.security.SecurityUtil;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by z688522 on 3/5/15.
 */
public class VerizonSSLSocketFactoryClient  {

    private static final String TAG = VerizonSSLSocketFactoryClient.class.getCanonicalName();
    private static VerizonSSLSocketFactoryClient ourInstance;
    private SSLContext context;

    // Stubbed certificates and keys.
    String signed_cert = "-----BEGIN CERTIFICATE-----\n" +
            "    MIIEADCCAuigAwIBAgIBADANBgkqhkiG9w0BAQUFADBjMQswCQYDVQQGEwJVUzEh\n" +
            "    MB8GA1UEChMYVGhlIEdvIERhZGR5IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBE\n" +
            "    YWRkeSBDbGFzcyAyIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA0MDYyOTE3\n" +
            "    MDYyMFoXDTM0MDYyOTE3MDYyMFowYzELMAkGA1UEBhMCVVMxITAfBgNVBAoTGFRo\n" +
            "    ZSBHbyBEYWRkeSBHcm91cCwgSW5jLjExMC8GA1UECxMoR28gRGFkZHkgQ2xhc3Mg\n" +
            "    MiBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTCCASAwDQYJKoZIhvcNAQEBBQADggEN\n" +
            "    ADCCAQgCggEBAN6d1+pXGEmhW+vXX0iG6r7d/+TvZxz0ZWizV3GgXne77ZtJ6XCA\n" +
            "    PVYYYwhv2vLM0D9/AlQiVBDYsoHUwHU9S3/Hd8M+eKsaA7Ugay9qK7HFiH7Eux6w\n" +
            "    wdhFJ2+qN1j3hybX2C32qRe3H3I2TqYXP2WYktsqbl2i/ojgC95/5Y0V4evLOtXi\n" +
            "    EqITLdiOr18SPaAIBQi2XKVlOARFmR6jYGB0xUGlcmIbYsUfb18aQr4CUWWoriMY\n" +
            "    avx4A6lNf4DD+qta/KFApMoZFv6yyO9ecw3ud72a9nmYvLEHZ6IVDd2gWMZEewo+\n" +
            "    YihfukEHU1jPEX44dMX4/7VpkI+EdOqXG68CAQOjgcAwgb0wHQYDVR0OBBYEFNLE\n" +
            "    sNKR1EwRcbNhyz2h/t2oatTjMIGNBgNVHSMEgYUwgYKAFNLEsNKR1EwRcbNhyz2h\n" +
            "    /t2oatTjoWekZTBjMQswCQYDVQQGEwJVUzEhMB8GA1UEChMYVGhlIEdvIERhZGR5\n" +
            "    IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBEYWRkeSBDbGFzcyAyIENlcnRpZmlj\n" +
            "    YXRpb24gQXV0aG9yaXR5ggEAMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQAD\n" +
            "    ggEBADJL87LKPpH8EsahB4yOd6AzBhRckB4Y9wimPQoZ+YeAEW5p5JYXMP80kWNy\n" +
            "    OO7MHAGjHZQopDH2esRU1/blMVgDoszOYtuURXO1v0XJJLXVggKtI3lpjbi2Tc7P\n" +
            "    TMozI+gciKqdi0FuFskg5YmezTvacPd+mSYgFFQlq25zheabIZ0KbIIOqPjCDPoQ\n" +
            "    HmyW74cNxA9hi63ugyuV+I6ShHI56yDqg+2DzZduCLzrTia2cyvk0/ZM/iZx4mER\n" +
            "    dEr/VxqHD3VILs9RaRegAhJhldXRQLIQTO7ErBBDpqWeCtWVYpoNz4iCxTIM5Cuf\n" +
            "    ReYNnyicsbkqWletNw+vHX/bvZ8=\n" +
            "    -----END CERTIFICATE-----";
    final String priate_key = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDC/AYbs+ywlCYx\n" +
            "lk3RZWWUuhPSX25BxGH4f3ncDwQO43ALKDh9xqsbMbg/ZuKzgFThg4Jcxvt3k1IX\n" +
            "k0UDYRwvJGbIlk80iE6mnFeC/XCJqgWWIsyogQQeasaqVeAvtGEA7dsZwaDDdIoT\n" +
            "T88Q+3WNNsoKn+K6l/XtS/gBPkc+loIRdJLsOCBbuFEE4u0LHSnJyA1B7fZKJv6i\n" +
            "/FM29+UkR4nmaH2QYNcQFBiJtCZRq+9wqkp3Y081TCmy+fUBrzjzfQlgJ5CuBZF6\n" +
            "10HmXrAs37uX3fXvSYD73Ig1fj0AuU34z5qrsP2yRWSLtXuXpUvmQmf3hJZz9HHM\n" +
            "u5W6zKvlAgMBAAECggEAVkMtaKAnPM+uPg3jC5qyYCQalN3HZ608MrYi8WprPzL7\n" +
            "5NO8elikZnmOq+4WqZdC9io6SFV39yTkEq4CwEX8NBcEqgVlsTYEpc1RIhCB80WX\n" +
            "Bx8KMBoz+kj+72jir/g7Wi/g2PpC53XtiDGsyDSXgwwUQrMNmcOU95C+fb5RwqwJ\n" +
            "nDnmaa/JHgOu4nJl8iQ+6JJMy3cqlRA+QcZ7vwSFjVAlM2apRt+Kq7lU3ldfeOlE\n" +
            "K3BASD7c1unSULIVUO7Jg+BMWkCLA/vXqLGO6+fUvdO0KNvULEJTO7L19qjSp3rF\n" +
            "90hJPn4CuVM6p9bkvcdrshfBIrICVGdUjcmAYhBYgQKBgQDq2275ylltdYvYIhXC\n" +
            "3Z26P2JvYgGpOAJnD3319AacIoopFoxOgfJvzmxLF6bUgGQA+2m13B8dSnTrjsSr\n" +
            "ZIm/4zYrufoI55l4hQhXN/Yv5oTeYFrTMi049x2cHP3wijogIlxvlKETOTk/oEAT\n" +
            "IzCeZGhYjQ5WXh7QEgZ+x6ge0QKBgQDUia0Rn0Nz9v7Z/Z0lnfKP3eCQI2FO2jXM\n" +
            "PA7n8M8XuPPncmSlLiyy6dIJhhMznk+jiwWaPDvwURjjLR9R4woCbPy/7Kn6hHoE\n" +
            "DCLMrwUF7//VP2xq1sOsfz5MX/6r2mpdO8YkepOEMuOrnKI0DiOcWN7aQkKkZsSV\n" +
            "IPOVLcyI1QKBgFuqF69g3fSN+235J7st2kD2Oko022cbmDx3XHdhri7EgF43Eb4W\n" +
            "k/Q1gijw60lnbyC+/+xeJLq+89F4oKQwkl8VpG/+n2tYUZsreSzph6n8dAGDZA+o\n" +
            "NuRT9veWjTqUCMm//4oBXbjuDb0ao8eUZYZ8tAlTzLPC+o3Fa0mSTbjxAoGBAJHU\n" +
            "MUrBE0AzPiQGbtfLqw3peFaN4AuBj54lqzibswyc28V3aPUpXIQaCQJ92xj0y8Hz\n" +
            "9pEgOSQoEMJ3SGEgPdLyQu3RDcQVN78lMI9umyy6wrfnv5UC7NmjqPQwfYYe5BI6\n" +
            "nDaVYDg5zpsGawj6lpaFJ8tr4Vv6I3ILXPaDqS5NAoGBALKVIww/ieOCw6wztJfm\n" +
            "LNupDzuQC8kLNLou4xJaVrh39ugocC/vLrtx6/Q9E2oj9W5cv+g4Ggy+6Pzy5PjW\n" +
            "vZSqDkJGW3WMEOqVgeCoJe9lWDdJPVCPeu2ZS7wNBzVVvdhuZbLJ9om6lNMIPTR+\n" +
            "667n+u7mB5db5FvXGpFMhfY+\n" +
            "-----END PRIVATE KEY-----";
    String clientcert_plus_pk_12 = "MIIJeQIBAzCCCT8GCSqGSIb3DQEHAaCCCTAEggksMIIJKDCCA98GCSqGSIb3DQEHBqCCA9AwggPM\n" +
            "AgEAMIIDxQYJKoZIhvcNAQcBMBwGCiqGSIb3DQEMAQYwDgQI13oWrh9dPxwCAggAgIIDmAFZHK6E\n" +
            "FYrnL5IeJER8enH7CPk6Nu6O7yfXP5DHk738isMlE0Li/lizr0LtBTK/JoK4biMivw8Y1cOewNPJ\n" +
            "KXCX3C3OAoUvqnSg9upU2OHcbktQ463723/CmlZKuE3oPfd8p2bsaGPlPuOXXJoA3ERO5a1OR7yj\n" +
            "/BS+BGWbZoTpQ81pF0UAinzynBu36r36hqQFB/u1ECJVM02/2AsjoQWRNhJTwELToa7+4Hgm8ETm\n" +
            "CTQQFdtCIb986yjGowQEDWSbXqaMRTkTFxK3R6TUmoQcQGHTGYLBRS08EeQF8S1MNkIExEngMnM9\n" +
            "xopcq+X3IB6+7/CbVO8/95tpt3+QUjj6xnpl+Mfjau/i7WEWhHFO6XJ30aI9ejr56KR9rnkRuSHI\n" +
            "W/gA7E6txhlS9VZ3FCLb4MeiXvPRRJHRjodP1QMXXhURasKf+JeleRspBN/ZpGPPTcbbwQtExiZY\n" +
            "bBCaxQ03P5yHvzolD3bEtVyQxbEDBxzc6e0KQ9gFIop1c1dOgLnWeiXl0qNWreqeg8Z558qiksxP\n" +
            "ewjhdIfo1HFOFpiR8k5xfMgVQGe5iFPePhAgBgs16Ksfek393SNbw7KDp7k7MrfQT52BdCGe/Z3O\n" +
            "kJS83cSpUqTsQfGS4F33wJFnoRSX94Ir2erF4E2aBm+pXRb5lzePCK6T70oS5Z16r/d+vxA4+W2C\n" +
            "iiJ7Y7FGsbS5qBqRuVxnMT2iT54FEdP8p2hW7Q+blAw92JTDO/86orCA7MJ0PBGVR7zZc/MR2s2j\n" +
            "Gn5bXDeAkDUrFNqWpZwyc7PyicoK7AY7GCvRwGEqJdptcYZBCAlKbaLFHeAom1QhbMFvC0tesOio\n" +
            "u/MZdQiraPxNNUzUcU9DVSoZGg1/gZj5ymXs3cu3ju2eMdch+lGbxJVHq1SIhsh5wgEwhFB7MITn\n" +
            "IIZ4sWVj/KTlVTcorK/k4N+alXB4y8WltQhIoIKzYiNVtYigvlNYRQLkzEl1BKtxfwfU7j7H5l+9\n" +
            "+9N6cTmos7mdmQx2YNhfkwIPM14A4CViuWC2KXgVU60ZLH9+FT9TMkA/l3nmV8swgza5pqGDz17J\n" +
            "meSz9oBAXDcjNNS/r/4lq3r/a2CqxyPhzzTpGPhPrlrVWjbBQ4JBuwemH7HI1wN/qIsDxBZSL0+/\n" +
            "ckJakE2NBushJWi12z9a6BEUvADn1I3qWWDvd5gHnBgEQKwOTgRAlUStrEVioYb/Okp+lO13PfgO\n" +
            "o8nxMIIFQQYJKoZIhvcNAQcBoIIFMgSCBS4wggUqMIIFJgYLKoZIhvcNAQwKAQKgggTuMIIE6jAc\n" +
            "BgoqhkiG9w0BDAEDMA4ECG/AxUWZ2EWzAgIIAASCBMjO6PSD3Ln9tV/bDy/74CAVnkMAXcb/S9v2\n" +
            "OvyErmvaWqTYJmBnRZ4R2RmECf6z3ZE3eAMvyEoMbB8v2EVK40t4Sn34tu9OpSZucaAviGyYPyDA\n" +
            "PQCVCwUVexX6vNi6XeDqasF0xvGLexWTYFNP4ji6RuSChel5NYal6MkZbZoGupXnrSn+nQB/HDoQ\n" +
            "tPHgJuzth5hXy/xswx6pdUaZbPRMkrhUOPaOxCH/S4wP9n7qPii6kLEuB0bBA5MpPHlkvrAUMibE\n" +
            "5KGNqSngxldJpuMf51UwpFU5QIvBlCjZ3nVrdATi6h/KeGtH6sw23pMeepA60mBlnO5/L2p9C19j\n" +
            "YNZdXMRZuf1nTRdzTsWipn0eY9E/X9slTN9dlM7MzmwvbCn2aN7QJy4VXdLaQoJJ/qyotczwRqP/\n" +
            "Lr0dYEk2Znw115TsPv6OvRuMwtFTSek59M2zHaWfLZnJsmcQQmEgAtcNPKbOcnR0/X/rP4uPJ/Yn\n" +
            "7keujP/5ojBA5cizUtfY4shfvTpzhgFtPZUyetTpYx3MIS6y0QWWpOfbxVD1UeE40P/HZE1RJ726\n" +
            "SIpMPjlJXRZY4C+GZnCrkqV4vObIOH8cs4yneE1d8pnajPo07MJGT8YznuYJjGhIUAQHv1bDdt1p\n" +
            "/UXphwo+h58Q9b/W0/RXqWz759y0qIHqBmHlPzmqyqZzq5+Ulh9HrhH4sVQhcYW2zFfc8iTumz9n\n" +
            "HG2JkiWS7Q8cgiEdIhGCwuFDSwFbNqsxb9rTMC/VuCgptMpoM6QklXqTEvG2Jo4LMDqF3RPgpb3P\n" +
            "hae0ZkzUVZPUkLPYep4WBiwcTm8EeM4tcjKjZdgg/7SB/lxLeoLMjQNQvLi4IFLciZaTY9thMUgV\n" +
            "1p2dNgIeexL0WrtivvyQGqn6IjS+Nv4mdJmRveeVXrzBrCw4cooyZ0HyP1WFl5X5LBlQ40/O4xfQ\n" +
            "1aeHAZS8d0WkgiSjP+4GpfglXfYfV7TwPgGTQYfwE9qG39fjMGupslMFyUHE2ntQ03Ycrj7/yHhZ\n" +
            "3tJkT17PC1RQR0CURAIcRjkomwnKzt+kBNz+i3/Nkx6dH2Zyy+A3fQGs6lxC1WwqR81SWtepuk6s\n" +
            "xrrKaa9B2Am+viJzvOPhsjTxASHevc7Onavuud+VIhs/eWyU6fg0eUigmOX56BBufRvtyA8ZQzXz\n" +
            "yxmmvuj+QcSLfrGzFBRtSDxt7U3MUh8mZQkEW0ubqBocr3ED38FtU1fUgbjILGczllntXKuK6S2/\n" +
            "ULp5ljGB3jpnJd9LoDif6VSlov5PvDeD8rWaxQAE6L7BtTc16amaaqxBMlOWPo6icR1RoERfD48Z\n" +
            "rW/3DdmQs/OZKsR+ooRc4HhszVsatraUNGg0861DweoNwyXi2B4FzdfvRIoKYtwi1TtA57Q3nowP\n" +
            "Yg4Vm/TL/tn2Fe/WMQQdTtyZIK0GbItpBIdhyzGAKP61eUonISpbDZwI6Ht7kbZQ+CsnpjCrjfCq\n" +
            "Ka/n6N4+mdf8kzCnuH+2JBvc4gpMxfUb0WenKmh53BWR4Z4OngiXMdijYy/hM7UvXSwQ9NGIVKUx\n" +
            "bnuyHU2kWDfKq9g5IeaLveeP2yXVHBEaYCOJtpIsdSKSoCQjvQomnXl1TLe2HvVcbIaz21KxY4Qm\n" +
            "txAxJTAjBgkqhkiG9w0BCRUxFgQU6tYv4UWTXicDVouZSlAU3kFvRHEwMTAhMAkGBSsOAwIaBQAE\n" +
            "FGEX1O+zdd5jTc95Z0H+FptHaU64BAgC7V+DwNt8fgICCAA=";
    // Stub ended.

    /*
    ** Get the instance of KeyStore
    */
    synchronized public static VerizonSSLSocketFactoryClient instance() {
        if (ourInstance == null) {
            ourInstance = new VerizonSSLSocketFactoryClient();
            ourInstance.init();
        }
        return ourInstance;
    }

    synchronized public static void setInstance(){
        Log.d(TAG,"layer7 setting VerizonSSLSocketFactoryClient as null");
        ourInstance = null;
    }

    private void init() {
        // get the TLSV1v2 instance
        Log.d(TAG,"layer7 init");
//        try {
//            context = SSLContext.getInstance("TLSv1.2");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        KeyStore clientStore = null;

//        create pk12
        try {
            clientStore = KeyStore.getInstance("PKCS12", "BC");
//            CreatePKCS12.createPKCS12();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Get the KeyStore
        KeyStore keyStore = getKeyStore(clientStore);
        // Create KeyManagerFactory
        KeyManagerFactory kmf = getKeyManagerFactory();
        try {
            kmf.init(keyStore, "passPhrase".toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            context = getSSLContext(kmf);
        }catch (Exception e){
            e.printStackTrace();
        }
        //Create TrustManagerFactory
//        KeyStore trustStore = getTrustStore();
//        TrustManagerFactory tmf = getTrustManagerFactory();
//        try {
//            tmf.init(trustStore);
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }

        // by pass all
//        TrustManager tm =  new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return null;
//            }
//        };

        // initialize the SSLContext.
        // Stubbed because we are not doing certificate stuff
        // Once certificate machanisam workin in Layer 7 we will have to remove below code.
//        tmf =  null;
//        kmf =  null;
        // stub ended.





//        Log.d(TAG,"layer7 is key manager null "+(kmf == null ) +" is trust manager null "+(tmf == null));
//        try {
//            context.init((kmf == null ? null : kmf.getKeyManagers()), ((tmf == null) ? new TrustManager[] { tm } : tmf.getTrustManagers()), new SecureRandom());
//
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
    }

//    private void init(){
//        Log.d(TAG,"layer7 new init");
//        try {
//            // Load CAs from an InputStream
//// (could be from a resource or ByteArrayInputStream or ...)
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//// From https://www.washington.edu/itconnect/security/ca/load-der.crt
//
//            InputStream caInput = new BufferedInputStream(new ByteArrayInputStream(InDrivePreference.getInstance().getStringData("cert", signed_cert).getBytes()));//new BufferedInputStream(new FileInputStream("load-der.crt"));
//            Certificate ca;
//            try {
//                ca = cf.generateCertificate(caInput);
//                Log.d(TAG,"ca=" + ((X509Certificate) ca).getSubjectDN());
//            } finally {
//                caInput.close();
//            }
//
//// Create a KeyStore containing our trusted CAs
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//// Create a TrustManager that trusts the CAs in our KeyStore
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//// Create an SSLContext that uses our TrustManager
//            SSLContext context = SSLContext.getInstance("TLSv1.2");
//            context.init(null, tmf.getTrustManagers(), null);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }





    private KeyStore getTrustStore() {
        KeyStore keyStore = null;

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        Log.d(TAG, "$$$ default keyStoreType "+keyStoreType);
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            Log.d(TAG,"layer7 getting cert2");
            signed_cert = InDrivePreference.getInstance().getStringData("cert", signed_cert); //FileUtils.readCertFromAppInternal(IndriveApplication.getInstance());
//            signed_cert = KeyStoreWrapper.getInstance().getKeyStore().getCertificate("cert");
            Log.d(TAG,"layer7 signed_cert "+signed_cert);
            X509Certificate x509Certificate = (X509Certificate) getCertificate(signed_cert);
            keyStore.setCertificateEntry(x509Certificate.getSubjectX500Principal().getName(), x509Certificate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return keyStore;
    }

//    private KeyStore getTrustStore(KeyStore clientStore) {
//
//        // Create a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
////        Log.d(TAG, "$$$ default keyStoreType "+keyStoreType);
//        try {
//            clientStore = KeyStore.getInstance("PKCS12", "BC");
//            clientStore.load(null, null);
//            clientStore = getKeyStore(clientStore);
//            Log.d(TAG,"layer7 getting cert2");
//            signed_cert = InDrivePreference.getInstance().getStringData("cert", signed_cert); //FileUtils.readCertFromAppInternal(IndriveApplication.getInstance());
////            signed_cert = KeyStoreWrapper.getInstance().getKeyStore().getCertificate("cert");
//            Log.d(TAG,"layer7 signed_cert "+signed_cert);
//            X509Certificate x509Certificate = (X509Certificate) getCertificate(signed_cert);
//            clientStore.setCertificateEntry(x509Certificate.getSubjectX500Principal().getName(), x509Certificate);
//
//            Enumeration<String> listAlias = clientStore.aliases();
//            while (listAlias != null && listAlias.hasMoreElements()){
//                Log.d(TAG,"alias available in keystore in truststore method"+listAlias.nextElement());
//            }
//
//
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return clientStore;
//    }


    private KeyStore getKeyStore( KeyStore clientStore) {
        Log.d(TAG,"layer7 getKeyStore()");
//        KeyStore clientStore =  null;

//        clientStore = KeyStoreWrapper.getInstance().getKeyStore();
//        if (clientStore != null) {
//            Log.d(TAG,"layer7 clientStore "+clientStore.getProvider().getName());
//            try {
//                Log.d(TAG,"layer7 before setting keyEntry "+PKCS12Attributes.getInstance().getPrivateKey());
////                clientStore.setEntry("iTest", new KeyStore.PrivateKeyEntry(PKCS12Attributes.getInstance().getPrivateKey(), CreatePKCS12.getChain()),
////                        new KeyStore.PasswordProtection("passPhrase".toCharArray()));
//                clientStore.setKeyEntry("iTest",PKCS12Attributes.getInstance().getPrivateKey(),"passPhrase".toCharArray(),CreatePKCS12.getChain());
////                clientStore.setCertificateEntry("iTest",getCertificate(InDrivePreference.getInstance().getStringData("cert", signed_cert)));
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
////            KeyStoreWrapper.getInstance().store("iTest", PKCS12Attributes.getInstance().getPrivateKey());
////            try {
////                KeyStoreWrapper.getInstance().store("iTest", getCertificate(InDrivePreference.getInstance().getStringData("cert", signed_cert)));
////            }catch (Exception e){
////                e.printStackTrace();
////            }
//
//            Log.d(TAG,"layer7 after keystored ");
//            try {
//                Enumeration<String> enumerationAlias = clientStore.aliases();
//                if(enumerationAlias != null && enumerationAlias.hasMoreElements()){
//                    Log.d(TAG,"layer7 keystore alias "+enumerationAlias.nextElement());
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return clientStore;
//        }
        try {
            Log.d(TAG,"layer7 clientStore null trying to initialize again");
//            clientStore = KeyStore.getInstance("PKCS12", "BC");
            //clientStore = KeyStoreWrapper.getInstance().getKeyStore();
            try {
//                clientStore.load(new BufferedInputStream(new ByteArrayInputStream(Base64.decode(clientcert_plus_pk_12))), "12345".toCharArray());
                FileInputStream fis = new FileInputStream(getClientCertFile1(SecurityUtil.getCurrentUserPk12FileName()));//    "client-cert.p12"
                clientStore.load(fis,"passPhrase".toCharArray());



                Log.d(TAG,"layer7 loaded clientStore from the hard coded pk12");
                Enumeration<String> listAlias = clientStore.aliases();
                while (listAlias != null && listAlias.hasMoreElements()){
                    Log.d(TAG,"alias available in keystore "+listAlias.nextElement());
                }


                return clientStore;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientStore;
    }


    public SSLContext getSSLContext(KeyManagerFactory kmf)
            throws Exception {
        TrustManager[] trustManagers = new TrustManager[] {
                new ReloadableX509TrustManager()
        };
        context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), trustManagers,  new SecureRandom());
        return context;
    }


    private File getClientCertFile(String clientCertificateName) {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        return new File(externalStorageDir, clientCertificateName);
    }

    private File getClientCertFile1(String clientCertificateName){
        return FileUtils.readInternalFile(clientCertificateName);
    }


    private TrustManagerFactory getTrustManagerFactory() {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            return tmf;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyManagerFactory getKeyManagerFactory() {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            return kmf;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Certificate getCertificate(String mServerSignedCertificate) {
        Log.d(TAG,"layer7 $$ getCertificate "+mServerSignedCertificate);
        CertificateFactory cf = null;
        InputStream caInput = null;
        try {
            cf = CertificateFactory.getInstance("X.509","BC");
        }catch (CertificateException e){
            e.printStackTrace();
        }catch (NoSuchProviderException e){
            e.printStackTrace();
        }
        Certificate ca = null;
        caInput = new BufferedInputStream(new ByteArrayInputStream(mServerSignedCertificate.getBytes()));
        try {
            Log.d(TAG,"layer7 before generating certificate");
            ca = cf.generateCertificate(caInput);
            if(ca != null) {
                Log.d(TAG, "layer7 ca=" + ((X509Certificate) ca).getSubjectDN());
                Log.d(TAG, "layer7 public key " + ((X509Certificate) ca).getPublicKey());

            }
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

    private String getTestCert(){
        return "-----BEGIN CERTIFICATE REQUEST-----\n" +
                "    MIICsDCCAZgCAQAwazELMAkGA1UEBxMCVVMxCzAJBgNVBAoTAlNGMRgwFgYDVQQL\n" +
                "    Ew9mZ2RmZ3JldGVydGdkZmcxNTAzBgNVBAMTLDYwYTc1Njg5LTAxZDAtNDA4MC1h\n" +
                "    MzMyLTcyMjFjZjg1ZGJlYy9iaWplc2g4MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A\n" +
                "    MIIBCgKCAQEA1K8omqb7DAW8hg37Q2dh9wl22szgenImLXWlslEnr1yBnFoQ2AYV\n" +
                "    DIAErZaVYSTCdiAkPAH5Nt5Uxc25d7Ue0bGlYaTeGaWXMcemvBJ7SzdoW2HXzYH2\n" +
                "    FXiYT7YWMAx/36pzbQmQW7h4C+tg/Fe48HRW40Yz7IJNTCZies7FGN5eCyWhiyXc\n" +
                "    tJEtV7KiuHUO6tbfB1NxstvpBnuHEMSC9DRY3tNIA+PlANbEYCY4PCLETJGDBo3i\n" +
                "    bkZ5mzaeePXTJMxAepiKH/zrEoOlpn+6OtRoXqWrUPat+bUBMJXFTf4YzNs4S13T\n" +
                "    D0ItRHwm2XSQGbLy7WHmbJ9G/pcNDn1geQIDAQABoAAwDQYJKoZIhvcNAQEFBQAD\n" +
                "    ggEBAMBAEJfXMnDnOfyp+G4wWtSFA+7mM1xI7trTmmOrnQrxhGhiievARLR7lyM/\n" +
                "    LcQ0QNe4JA5RcHKIuVplHqmkmDl33MydjUDQiFxI+RU3vgiC99+++8baTcZSLDbu\n" +
                "    eDqIQZO9PNyFVPS4qDIPk+pdwYCIEKaFxBZWNmIvvqkeCSubgrWXP7btTgubwObE\n" +
                "    0jq+9+YVMPdCaPEVbR9pFvJb0CLLJKu3b3oS2+OQ0yDyW/d1Wsfrl4S7wZDUX83J\n" +
                "    +XVxTXRxvUa8KFARm/5XqecoAbP8eQTS+biZ9EdtYhBUKpumTMp3mUi1ZnZ4E3jr\n" +
                "    uEiilagH9ASDFZNaLoCeAX0I5vE=\n" +
                "    -----END CERTIFICATE REQUEST-----";
    }

    public SSLSocketFactory getSocketFactory() {
        if (context == null)
            return null;
        return  context.getSocketFactory();
    }
}
