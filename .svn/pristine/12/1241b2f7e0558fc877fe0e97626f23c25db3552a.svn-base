package com.verizontelematics.indrivemobile.connection.ssl;

import android.content.Context;
import android.util.Log;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by bijesh on 2/4/2015.
 *
 */
public class VerizonSSLSocketFactory extends SSLSocketFactory {

    private static final String TAG = VerizonSSLSocketFactory.class.getCanonicalName();

    private SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    private  SSLSocketFactory delegate = null;

    public VerizonSSLSocketFactory(KeyStore trustStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(trustStore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, getTrustManagers1() , null);//new TrustManager[]{tm}
//        printSupportedCipherSuites();
    }



    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        Log.d(TAG, "$$$ create Socket1 ");
        Socket returnSocket = sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//        ((SSLSocket)returnSocket).setEnabledCipherSuites(PREFERRED_ALL_CIPHER_SUITES);
        ((SSLSocket)returnSocket).setEnabledProtocols(new String[] {"TLSv1.2"} );
        return returnSocket;
    }

    @Override
    public Socket createSocket() throws IOException {
        Log.d(TAG, "$$$ create Socket2 ");
        Socket returnSocket = sslContext.getSocketFactory().createSocket();
//        ((SSLSocket)returnSocket).setEnabledCipherSuites(PREFERRED_ALL_CIPHER_SUITES);
        ((SSLSocket)returnSocket).setEnabledProtocols(new String[] {"TLSv1.2"} );
        return returnSocket;
    }





    private TrustManager[] getTrustManagers1(){

        CertificateFactory cf = null;
        InputStream caInput = null;
        TrustManagerFactory tmf = null;
        KeyStore keyStore = null;
        // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        try {
            cf = CertificateFactory.getInstance("X.509");
        }catch (CertificateException e){
            e.printStackTrace();
        }
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
        try {
//            caInput = new BufferedInputStream(new FileInputStream("/storage/emulated/0/api-sit.vtitel.com.cer"));
            String cert = InDrivePreference.getInstance().getStringData("cert","");
            Log.d(TAG,"layer7 mySocketFactory "+cert);
            caInput = new BufferedInputStream(new ByteArrayInputStream(cert.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
            Log.d(TAG,"ca=" + ((X509Certificate) ca).getSubjectDN());
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

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        try {
            keyStore = loadPEMKeystoreStore();
//            keyStore = KeyStore.getInstance("BKS");
//            InputStream in = IndriveApplication.getInstance().getResources().openRawResource(R.raw.my_keystore);
//            keyStore.load(in, "my_password".toCharArray());
//            keyStore.setCertificateEntry("ca", ca);
        }catch (Exception e){
            e.printStackTrace();
        }

//        keyStore = loadPEMKeystoreStore();

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


    private KeyStore loadPEMKeystoreStore() throws Exception {
        InputStream caInput = new BufferedInputStream(new ByteArrayInputStream(InDrivePreference.getInstance().getStringData("cert","").getBytes()));

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keystore = KeyStore.getInstance(keyStoreType);


        CertificateFactory certificateFactory = CertificateFactory
                .getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory
                .generateCertificate(caInput);


        keystore.load(null);
        keystore.setCertificateEntry("cert-alias", cert);
        keystore.setKeyEntry("key-alias", null, "mypassword".toCharArray(),
                new Certificate[]{cert});
        FileOutputStream out = IndriveApplication.getInstance().openFileOutput("tess.txt", Context.MODE_PRIVATE);
        keystore.store(out, "mypassword".toCharArray());

        return keystore ;
    }

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


//    private void printSupportedCipherSuites(){
//            String[] defaultCipherSuites = sslContext.getSocketFactory().getDefaultCipherSuites();
//            for(String s:defaultCipherSuites){
//                System.out.println("$$$ printing default ciphers "+s);
//            }
//    }

}
