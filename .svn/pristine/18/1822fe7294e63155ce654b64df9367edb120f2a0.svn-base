package com.verizontelematics.indrivemobile.connection.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Created by bijesh on 2/23/2015.
 */
public class CertificateUtils {

    private static final String TAG = CertificateUtils.class.getCanonicalName();


    public static Date getExpiryDateFromCertificate(String certificateAsString){
        Date returnDate = null;
        try {
            InputStream in = new ByteArrayInputStream(Base64Util.decodeBase64AsString(certificateAsString).getBytes());
//            InputStream in = this.getResources().openRawResource(R.raw.api_sit_vtitel_com);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate)cf.generateCertificate(in);


            returnDate = certificate.getNotAfter();

            Log.d(TAG, " expiry date not after " + returnDate + " not before " + certificate.getNotBefore());

        }catch (CertificateException e){
            e.printStackTrace();
        }
        return returnDate;
    }

}
