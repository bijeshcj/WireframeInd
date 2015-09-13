package com.verizontelematics.indrivemobile.httpwrapper.Impl;

import android.util.Log;

import com.android.volley.toolbox.HurlStack;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;


/**
 * Created by bijesh on 4/23/2015.
 */
public class VerizonHttpsStack extends HurlStack {

    private static final String TAG = VerizonHttpsStack.class.getCanonicalName();

    public VerizonHttpsStack(SSLSocketFactory socketFactory) {

        Log.d(TAG, "verizonHttpStack consrtuctor");
    }

    /**
     * Create an {@link java.net.HttpURLConnection} for the specified {@code url}.
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if ("http".equals(url.getProtocol())) {
            return connection;
        }
        ((HttpsURLConnection)connection).setHostnameVerifier(new X509HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                Log.d(TAG, "verify hostname " + s+" SSLSession protocol "+sslSession.getProtocol());
                Certificate[] certificates = sslSession.getLocalCertificates();
                if(certificates != null) {
                    for (Certificate certificate : certificates) {
                        Log.d(TAG, "Algorithm " + certificate.getPublicKey().getAlgorithm() + " type " + certificate.getType() + " " + certificate.getPublicKey().toString());
                        Log.d(TAG, "layer7 ca=" + ((X509Certificate) certificate).getSubjectDN());
                        Log.d(TAG, "layer7 public key " + ((X509Certificate) certificate).getPublicKey());
                    }
                }
                return true;
            }

            @Override
            public void verify(String s, SSLSocket sslSocket) throws IOException {
                Log.d(TAG, "verify hostname " + s);
                String[] supportedProtocol = sslSocket.getSupportedProtocols();
                for(String sp:supportedProtocol){
                    Log.d(TAG," supported protocols "+sp);
                }
            }

            @Override
            public void verify(String s, X509Certificate x509Certificate) throws SSLException {
                Log.d(TAG, "verify hostname " + s+" X509Certificate not before "+x509Certificate.getNotBefore()+" not after "+x509Certificate.getNotAfter()+" version "+x509Certificate.getVersion());
            }

            @Override
            public void verify(String s, String[] strings, String[] strings2) throws SSLException {

            }
        });
        return connection;
    }
}
