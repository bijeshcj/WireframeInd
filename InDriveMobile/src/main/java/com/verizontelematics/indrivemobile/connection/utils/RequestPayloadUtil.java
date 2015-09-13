package com.verizontelematics.indrivemobile.connection.utils;


import android.util.Log;

import com.verizontelematics.indrivemobile.connection.requests.RequestParams;
import com.verizontelematics.indrivemobile.connection.responses.AuthenticateResponse;
import com.verizontelematics.indrivemobile.connection.responses.DevicePairResponse;


import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.security.auth.x500.X500Principal;

/**
 * Created by bijesh on 2/17/2015.
 */
@SuppressWarnings("ALL")
public class RequestPayloadUtil {

    public static String getDevicePairPayLoad(String userId, AuthenticateResponse responseData){
        String requestData = "";

        try {
            JSONObject reqJson = new JSONObject();
            reqJson.put(Constants.CONTEXT, ContextUtils.getContextJson());
            JSONObject json2 = new JSONObject();
            json2.put(Constants.USER_ID, userId);
            JSONObject jResData = new JSONObject(responseData.getData());
            jResData = jResData.getJSONObject(Constants.DATA);
            json2.put(Constants.EMAIL, jResData.getJSONArray(Constants.EMAIL).getString(0));
            json2.put(Constants.PHONE, jResData.getJSONArray(Constants.PHONE).getString(0));
            reqJson.put(Constants.DATA, json2);
            requestData = reqJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestData;
    }

    public static String getCertificateIdPayLoad(String userId, DevicePairResponse responseData){
        String requestData = "";

        try {
            JSONObject reqJson = new JSONObject();
            reqJson.put(Constants.CONTEXT, ContextUtils.getContextJson());
            JSONObject json2 = new JSONObject();
            json2.put(Constants.USER_ID, userId);
            JSONObject jResData = new JSONObject(responseData.getData());
//            jResData = jResData.getJSONObject(Constants.DATA);
//            json2.put(Constants.EMAIL, jResData.getJSONArray(Constants.EMAIL).getString(0));
//            json2.put(Constants.PHONE, jResData.getJSONArray(Constants.PHONE).getString(0));
//            reqJson.put(Constants.DATA, json2);
            requestData = reqJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestData;
    }


    public static String getFirstUserPayLoad(RequestParams requestParams){
        return "Data\" : {\n" +
                "    \"VIN\" : null,\n" +
                "    \"MobileDeviceID\" : \"41901C59-271C-4EC6-A767-816BC61FF542\",\n" +
                "    \"LastName\" : \"ESALast4\",\n" +
                "    \"IMEI\" : \"358111040001581\"\n" +
                "  }\n" ;
//        String requestData = "";
//        try {
//            JSONObject reqJson = new JSONObject();
//            JSONObject dataJson = new JSONObject();
//            dataJson.put("LastName", "ESALast4");
//            dataJson.put("MobileDeviceID","123");
//            dataJson.put("VIN","");
//            dataJson.put("IMEI","358111040001581");
//            reqJson.put(Constants.DATA, dataJson);
//            requestData = reqJson.toString();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return requestData;
    }



    public static String getValidateRegistrationPayLoad(String userId,String registrationCode,String deviceName){
        String requestData = "";
        try {
            JSONObject reqJson = new JSONObject();
            reqJson.put(Constants.CONTEXT, ContextUtils.getContextJson());
//            requestData = reqJson.toString();
            JSONObject dataJson = new JSONObject();
            dataJson.put(Constants.USER_ID, userId);
            dataJson.put(Constants.REG_CODE, registrationCode);
            dataJson.put(Constants.DEVICE_NAME, deviceName);
            dataJson.put(Constants.CSR, csr());
            reqJson.put(Constants.DATA, dataJson);
            requestData = reqJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestData;
    }



    private static String csr() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            X500Principal subject = new X500Principal("CN = edea87b4-034d-48dc-94dd-e7cdcfdde370/10562468, OU = fgdfgretertgdfg, O = VW, L = US");
            ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").build(keyPair.getPrivate());
            PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic());
            PKCS10CertificationRequest csr = builder.build(signer);

            String type = "CERTIFICATE REQUEST";
            PemObject pem = new PemObject(type, csr.getEncoded());
            StringWriter str = new StringWriter();
            PEMWriter pemWriter = new PEMWriter(str);
            pemWriter.writeObject(pem);
            pemWriter.close();
            str.close();
            Log.d("Test", "" + str);
            return Base64Util.getStringAsBase64(str.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
