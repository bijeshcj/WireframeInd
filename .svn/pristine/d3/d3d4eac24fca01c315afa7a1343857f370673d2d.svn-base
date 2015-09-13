package com.verizontelematics.indrivemobile.connection.client;

import android.content.Context;
import android.util.Log;

import com.verizontelematics.indrivemobile.connection.ssl.VerizonSSLSocketFactory;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * Created by bijesh on 2/18/2015.
 */
public class VerizonHttpsClient {

    private static final String TAG = VerizonHttpsClient.class.getCanonicalName();
    public VerizonHttpsClient(){

    }

    public HttpClient getVerizonHttpClient(Context context) {
        try {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);

//            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
//            InputStream in = context.getResources().openRawResource(R.raw.mykeystore);
            try {
                // Initialize the keystore with the provided trusted certificates
                // Also provide the password of the keystore
//                trusted.load(in, "mysecret".toCharArray());
            } finally {
//                in.close();
            }
            Log.d(TAG, " $$$ getVerizonHttpClient ");
            SSLSocketFactory sf = new VerizonSSLSocketFactory(null);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);


            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));


            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

}
