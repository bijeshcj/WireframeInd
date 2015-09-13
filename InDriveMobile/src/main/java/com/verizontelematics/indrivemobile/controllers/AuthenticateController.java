package com.verizontelematics.indrivemobile.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.connection.utils.Base64Util;
import com.verizontelematics.indrivemobile.connection.utils.Constants;
import com.verizontelematics.indrivemobile.cryptography.Algorithams;
import com.verizontelematics.indrivemobile.cryptography.CryptoManager;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.Response;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.VerizonHttpRequest;
import com.verizontelematics.indrivemobile.httpwrapper.Impl.VerizonSSLSocketFactoryClient;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;
import com.verizontelematics.indrivemobile.models.AlertsControllerOperationData;
import com.verizontelematics.indrivemobile.models.BaseModel;
import com.verizontelematics.indrivemobile.models.GetUserPreferencesModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.UserAccountInfoModel;
import com.verizontelematics.indrivemobile.models.UserAccountModel;
import com.verizontelematics.indrivemobile.models.data.UserAccountData;
import com.verizontelematics.indrivemobile.models.data.UserDetail;
import com.verizontelematics.indrivemobile.models.data.UserPreferenceData;
import com.verizontelematics.indrivemobile.models.data.UserRegistrationData;
import com.verizontelematics.indrivemobile.models.data.UserVehicleData;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.FileUtils;
import com.verizontelematics.indrivemobile.utils.NetworkUtil;
import com.verizontelematics.indrivemobile.utils.config.Config;
import com.verizontelematics.indrivemobile.utils.config.ConfigKeys;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;
import com.verizontelematics.indrivemobile.utils.phone.CryptoWrapper;
import com.verizontelematics.indrivemobile.utils.phone.KeyStoreWrapper;
import com.verizontelematics.indrivemobile.utils.security.CreatePKCS12;
import com.verizontelematics.indrivemobile.utils.security.PKCS12Attributes;
import com.verizontelematics.indrivemobile.utils.security.SecurityUtil;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jcajce.provider.keystore.PKCS12;
import org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import javax.security.auth.x500.X500Principal;

/**
 * Created by z688522 on 2/25/15.
 */
public class AuthenticateController extends BaseController {

    private static final String TAG = "AuthenticateController";
    private static final int USER_ACCT_MODEL = 0;
    // creating model as per response need to re-factored.
    private static final int USER_ACCT_INFO_MODEL = 1;
    private static final int GET_USER_PREFERENCES_MODEL = 2;
    // ended

    private static final String MOBILE_DEVICE_ID = "MobileDeviceID";
    public static final String AUTH_DEVICE_AND_USER = "AuthorizeDeviceAndUser";
    public static final String AUTH_FORGOT_PASSWORD = "ForgotPassword";
    public static final String AUTH_DEVICE = "AuthorizeDevice";

    private static final int FIND_USER = Operation.OperationCode.FIND_USER.ordinal();
    private static final int SEND_AUTH_TOKEN = Operation.OperationCode.SEND_AUTH_TOKEN.ordinal();
    private static final int SEND_AUTH_TOKEN_NEW_DEV_REG = Operation.OperationCode.SEND_AUTH_TOKEN_NEW_DEV_REG.ordinal();
    private static final int GET_CERT_ID = Operation.OperationCode.GET_CERT_ID.ordinal();
    private static final int VALIDATE_REGISTRATION = Operation.OperationCode.VALIDATE_REGISTRATION.ordinal();
    private static final int AUTHENTICATE = Operation.OperationCode.AUTHENTICATE.ordinal();
    private static final int GET_USER_ACCT_VECH_DEV_INFO = Operation.OperationCode.GET_USER_ACCT_VECH_DEV_INFO.ordinal();
    private static final int GET_USER_VEHICLES = Operation.OperationCode.GET_USER_VEHICLES.ordinal();
    private static final int FORGOT_PASSWORD = Operation.OperationCode.FORGOT_PASSWORD.ordinal();
    private static final int FORGOT_USERNAME = Operation.OperationCode.FORGOT_USERNAME.ordinal();
    private static final int UPDATE_PASSWORD = Operation.OperationCode.UPDATE_PASSWORD.ordinal();
    private static final int GET_USER_PREFERENCE = Operation.OperationCode.GET_USER_PREFERENCE.ordinal();
    private static final int UPDATE_USER_PREFERENCE = Operation.OperationCode.UPDATE_USER_PREFERENCE.ordinal();
    private static final int REFRESH = Operation.OperationCode.REFRESH.ordinal();


    private static AuthenticateController mInstance = null;
    //private String mCertId = "";
    private HashMap<String, String> mCertIdUserMap = new HashMap<String, String>();
    private UserRegistrationData mUserRegistrationData;
    private String mServerSignedCertificate;
    private KeyPair mKeyPair;
    private X509Certificate mClientCert;
    // stubbed


    public static AuthenticateController instance() {
        if (mInstance == null)
            mInstance = new AuthenticateController();
        return mInstance;
    }

    private OperationFactory mOperationFactory = AlertsControllerOperationFactory.instance();

    private AuthenticateController() {
        super();
        mUserRegistrationData = new UserRegistrationData();

        mModels.add(new UserAccountModel());
        // creating model as per response need to re-factored.
        mModels.add(new UserAccountInfoModel());
        // ended.
        mModels.add(new GetUserPreferencesModel());
    }

    public BaseModel getUserAccountModel() {
        return mModels.get(USER_ACCT_MODEL);
    }

    public BaseModel getUserPreferencesModel() {
        return mModels.get(GET_USER_PREFERENCES_MODEL);
    }

    private String prepareBody(UserVehicleData data) {
        Gson gson = new GsonBuilder().create();
        String strData = "{\"Data\":" + gson.toJson(data).toString() + "}";
        // stubbed
        UserVehicleData mUserVehicleData = data;
        // stub ended.
        return strData;
    }

    private String prepareBodyForCert() {
        String retData = null;
        try {
            JSONObject dataJson = new JSONObject();
            JSONObject reqJson = new JSONObject();
//            reqJson.put("MobileDeviceID", "12E25D3C-B2FA-48C2-A181-9AEDD55652CB");
            reqJson.put(MOBILE_DEVICE_ID,InDrivePreference.getInstance().getStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,""));
            dataJson.put(Constants.DATA, reqJson);
            retData = reqJson.toString();
            Log.d(TAG, "$$$ request payload getCert " + retData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retData;
    }

    private String prepareBody() {
        String retData = null;
        try {
            JSONObject dataJson = new JSONObject();
            JSONObject reqJson = new JSONObject();
//            reqJson.put("MobileDeviceID", "12E25D3C-B2FA-48C2-A181-9AEDD55652CB");
            reqJson.put(MOBILE_DEVICE_ID,InDrivePreference.getInstance().getStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,""));
            dataJson.put(Constants.DATA, reqJson);
            retData = dataJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retData;
    }

    private String prepareBody(String mobileUserId) {
        String retData = null;
        try {
            JSONObject reqJson = new JSONObject();
            JSONObject dataJson = new JSONObject();
            dataJson.put("MobileUserID", mobileUserId);
            reqJson.put(Constants.DATA, dataJson);
            retData = reqJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retData;
    }

    private String prepareBodyForForgotPassword(UserRegistrationData userRegistrationData) {
        // Stubbed parameter
        String str = "{"
                + "\"Data\"" + ":" + "{"
                + "\"Token\"" + ":" + "\"" + userRegistrationData.getToken() + "\""
                + ","
                + "\"MobileDeviceID\"" + ":" +"\""+InDrivePreference.getInstance().getStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,"")+"\""
                + ","
                + "\"MobileUserID\"" + ":" + "\""+userRegistrationData.getMobileUserID()+"\""
                + ","
                + "\"Password\"" + ":" + "\""+userRegistrationData.getPassword()+"\""
                + "}"
                + "}";
        // Stub ended.
        return str;
    }

    private String prepareBodyForUpdatePassword(UserRegistrationData userRegistrationData) {
        // Stubbed parameter
        String str = "{"
                + "\"Data\"" + ":" + "{"
                + "\"MobileUserID\"" + ":" + "\""+userRegistrationData.getMobileUserID()+"\""
                + ","
                + "\"NewPassword\"" + ":" +"\""+ userRegistrationData.getPassword()+"\""
                + "}"
                + "}";
        // Stub ended.

        return str;
    }

    private String prepareBodyForForgotUsername(UserAccountData userAccountData) {
        // Stubbed parameter
        String str = "{"
                + "\"Data\"" + ":" + "{"
                + "\"AccountID\"" + ":" + "\"" + userAccountData.getAccountID() + "\""
                + ","
                //+ "\"Email\"" + ":" + "\"" + userAccountData.getEmail() + "\""
                + ((userAccountData.getEmail() != null) ? "\"EmailToken\"" + ":" + "\"" + userAccountData.getEmail().getEmailToken()
                   : "\"PhoneToken\"" + ":" + "\"" + userAccountData.getPhone().getPhoneToken()) + "\""
//                + ","
                //+ "\"Phone\"" + ":" + userAccountData.getPhone()
                + "}"
                + "}";
        // Stub ended.
        return str;
    }

    private String prepareBodyForGetUserPreferences(String accountId) {
        // Stubbed parameter
        String str = "{"
                + "\"Data\"" + ":" + "{"
                + "\"AccountID\"" + ":" + "\"" + accountId + "\""
                + "}"
                + "}";
        // Stub ended.
        return str;
    }

    private String prepareBodyForUpdateUserPreferences(UserPreferenceData userPreferenceData) {
        // Stubbed parameter
        String str = "{"
                + "\"Data\"" + ":" + "{"
                + "\"AccountID\"" + ":" + "\"" + getPrimaryUserDetail().getAccountInfo().getAccountID() + "\""
                + ","
                + "\"PushNotification\"" + ":" + userPreferenceData.getPushNotification()
                + ","
                + "\"EmailNotification\"" + ":" + userPreferenceData.getEmailNotification()
                + ","
                + "\"SmsNotification\"" + ":" + userPreferenceData.getSmsNotification()
                + "}"
                + "}";
        // Stub ended.
        return str;
    }

    private String prepareBodyValidateRegistration(UserRegistrationData userRegistrationData) {
        String requestData = null;

        try {
            JSONObject reqJson = new JSONObject();
            JSONObject dataJson = new JSONObject();
            dataJson.put("Token", userRegistrationData.getToken());
            dataJson.put("MobileDeviceID", InDrivePreference.getInstance().getStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,""));
            dataJson.put("TokenType", userRegistrationData.getTokenType());
            dataJson.put("MobileUserID", userRegistrationData.getMobileUserID());
            dataJson.put("Password", userRegistrationData.getPassword());
            //dataJson.put("csr", csr(mCertId, userRegistrationData.getMobileUserID()));

            dataJson.put("csr", csr(mCertIdUserMap.get(mUserRegistrationData.getMobileUserID()), userRegistrationData.getMobileUserID()));
            reqJson.put(Constants.DATA, dataJson);
            requestData = reqJson.toString();
            Log.d(TAG, "$$$ validate req payload " + requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requestData;
    }

    public Operation validateRegistration(Context ctx, UserRegistrationData userRegistrationData) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(VALIDATE_REGISTRATION, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                //if (mCertId.isEmpty()) {
                if (mCertIdUserMap.get(mUserRegistrationData.getMobileUserID()) == null) {
                    getCerId(ctx);
                    Operation opr = mOperationsModel.getOperation(VALIDATE_REGISTRATION);
                    opr.setState(Operation.PENDING);
                    onProgress(opr);
                    return opr;
                } else {
                    VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                    String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_VALIDATE_REGISTRATION);
                    aHttpRequest.setUrl(url);

                    HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                    if (map == null)
                        map = new HashMap<String, String>();
                    map.put("operation", Integer.toString(VALIDATE_REGISTRATION));


                    aHttpRequest.setMethod(RestClient.POST);
                    aHttpRequest.setHeaders(map);

                    aHttpRequest.setData(prepareBodyValidateRegistration(userRegistrationData));
                    // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                    // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                    aHttpRequest.setTag(TAG);
                    submit(aHttpRequest);
                    Operation opr = mOperationsModel.getOperation(VALIDATE_REGISTRATION);
                    opr.setState(Operation.PENDING);
                    onProgress(opr);
                }
            } else {
                Operation opr = mOperationsModel.getOperation(VALIDATE_REGISTRATION);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        } else {
            Operation opr = mOperationsModel.getOperation(VALIDATE_REGISTRATION);
            if (opr.getState() == Operation.PENDING) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
//                String url = "https://atllaywb02d.hughestelematics.net:8443/REST/SFUS_AuthorizationV5_0/ValidateRegistration";
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_VALIDATE_REGISTRATION);
                aHttpRequest.setUrl(url);

                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(VALIDATE_REGISTRATION));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyValidateRegistration(userRegistrationData));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
            }
        }
        return mOperationsModel.getOperation(VALIDATE_REGISTRATION);
    }


    public Operation getCerId(Context ctx) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(GET_CERT_ID, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
//                String url = "https://atllaywb02d.hughestelematics.net:8443/REST/SFUS_AuthorizationV5_0/GetCertID";
//                /REST/SFUS_AuthorizationV5_0/GetCertID
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_GET_CERT_ID);
                aHttpRequest.setUrl(url);

                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(GET_CERT_ID));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyForCert());
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(GET_CERT_ID);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(GET_CERT_ID);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }

        }
        return mOperationsModel.getOperation(GET_CERT_ID);
    }

    public Operation findUsers(Context ctx, UserVehicleData userVehicleData) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(FIND_USER, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_FIND_USER);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(FIND_USER));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBody(userVehicleData));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(FIND_USER);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(FIND_USER);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(FIND_USER);
    }



    private String prepareBody(UserAccountData data, String tokenType) {
        String requestData = null;
        // prepare request body.
//        String strData  = "{\"Data\": {"
//                                + "\"AccountID\"" + ":" + "\""+1277+"\""+","
//                                + "\"MobileDeviceID\"" + ":" +"\""+"12E25D3C-B2FA-48C2-A181-9AEDD55652CB"+"\""+","
//                                + "\"Email\"" + ":" +"\""+data.getEmail()+"\""+","
//                                + "\"Phone\"" + ":" + "null"+","
//                                + "\"TokenType\"" + ":" +"\""+"AuthorizeDeviceAndUser"+"\""
//                                +     "}"
//                           +"}";

        try {
            JSONObject reqJson = new JSONObject();
            JSONObject dataJson = new JSONObject();
            dataJson.put("AccountID", data.getAccountID());
            dataJson.put("MobileDeviceID", InDrivePreference.getInstance().getStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,""));

            //dataJson.put("Email", data.getEmail());
            //dataJson.put("Phone", data.getPhone());
            if (data.getEmail() != null) {
                dataJson.put("EmailToken", data.getEmail().getEmailToken());
            }
            else if (data.getPhone() != null) {
                dataJson.put("PhoneToken", data.getPhone().getPhoneToken());
            }
            dataJson.put("TokenType", tokenType);
            reqJson.put(Constants.DATA, dataJson);
            requestData = reqJson.toString();
            Log.d(TAG, "$$$ requestData Authenticate " + requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestData;
    }

    private String prepareBodyNewDeviceRegistration(UserAccountData data){
        String requestData = null;
        // prepare request body.
//        String strData  = "{\"Data\": {"
//                                + "\"AccountID\"" + ":" + "\""+1277+"\""+","
//                                + "\"MobileDeviceID\"" + ":" +"\""+"12E25D3C-B2FA-48C2-A181-9AEDD55652CB"+"\""+","
//                                + "\"Email\"" + ":" +"\""+data.getEmail()+"\""+","
//                                + "\"Phone\"" + ":" + "null"+","
//                                + "\"TokenType\"" + ":" +"\""+"AuthorizeDeviceAndUser"+"\""
//                                +     "}"
//                           +"}";

        try {
            JSONObject reqJson = new JSONObject();
            JSONObject dataJson = new JSONObject();
            dataJson.put("AccountID", data.getAccountID());
            dataJson.put("MobileDeviceID", InDrivePreference.getInstance().getStringData(AppConstants.MOBILE_UNIQUE_DEVICE_ID_KEY,""));
            if (data.getEmail() != null) {
                dataJson.put("EmailToken", data.getEmail().getEmailToken());
            }
            else if (data.getPhone() != null) {
                dataJson.put("PhoneToken", data.getPhone().getPhoneToken());
            }
            dataJson.put("TokenType", "AuthorizeDevice");
            reqJson.put(Constants.DATA, dataJson);
            requestData = reqJson.toString();
            Log.d(TAG, "$$$ requestData Authenticate " + requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestData;
    }


    public Operation sendAuthorizationTokenNewDeviceRegistration(Context ctx, UserAccountData userAccountData) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(SEND_AUTH_TOKEN_NEW_DEV_REG, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost()+ Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_SEND_AUTHORIZATION_TOKEN);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(SEND_AUTH_TOKEN_NEW_DEV_REG));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyNewDeviceRegistration(userAccountData));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(SEND_AUTH_TOKEN_NEW_DEV_REG);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(SEND_AUTH_TOKEN_NEW_DEV_REG);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(SEND_AUTH_TOKEN_NEW_DEV_REG);
    }



    public Operation sendAuthorizationToken(Context ctx, UserAccountData userAccountData, String tokenType) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(SEND_AUTH_TOKEN, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_SEND_AUTHORIZATION_TOKEN);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(SEND_AUTH_TOKEN));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBody(userAccountData, tokenType));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(SEND_AUTH_TOKEN);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(SEND_AUTH_TOKEN);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(SEND_AUTH_TOKEN);
    }

    public Operation authenticate(Context ctx, String username, String password) {
//        SecurityUtil.requiresCertificateReload();
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(AUTHENTICATE, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_AUTHENTICATE);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(AUTHENTICATE));

                //map.put("api_key", "582c33ebcbebb02d9bc77558c9e7b025");
                map.put("Authorization", Base64Util.getB64Auth(username, password));
                //map.put("Authorization", Base64Util.getB64Auth("10562468","8989"));
                // stub
                AppController.instance().setPassword(password);
                AppController.instance().setMobileUserId(username);
                // stub ened
                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                // stubbed
                aHttpRequest.setData(prepareBody());
                /*aHttpRequest.setData("{\n" +
                        "    \"Data\": {\n" +
                        "        \"Type\": \"primary\",\n" +
                        "        \"SourceName\": \"MAPP\"\n" +
                        "    }\n" +
                        "}");
                */
                // stubened.
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(AUTHENTICATE);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(AUTHENTICATE);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(AUTHENTICATE);
    }

    public Operation getUserVehicles(Context ctx, String mobileUserId) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(GET_USER_VEHICLES, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_GET_USER_VEHICLES);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(GET_USER_VEHICLES));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBody(mobileUserId));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(GET_USER_VEHICLES);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(GET_USER_VEHICLES);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(GET_USER_VEHICLES);
    }

    public Operation getUserAccountVehicleDeviceInfo(Context ctx, String mobileUserId) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(GET_USER_ACCT_VECH_DEV_INFO, Operation.CREATED))) {
            Operation refreshOpr = mOperationsModel.getOperation(REFRESH);

            if ((ctx == null && refreshOpr != null && refreshOpr.getState() == Operation.PENDING) || NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_GET_USER_ACCOUNT_DEVICE_INFO);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(GET_USER_ACCT_VECH_DEV_INFO));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                // stubed for getting subscription
                aHttpRequest.setData(prepareBody(mobileUserId));
                // stub enede. below code uncomment once stub reomoved.
                //aHttpRequest.setData(prepareBody(mobileUserId));

                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(GET_USER_ACCT_VECH_DEV_INFO);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(GET_USER_ACCT_VECH_DEV_INFO);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(GET_USER_ACCT_VECH_DEV_INFO);
    }


    public Operation forgotPassword(Context ctx, UserRegistrationData userRegistrationData) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(FORGOT_PASSWORD, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_FORGOT_USER_PASSWORD);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(FORGOT_PASSWORD));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyForForgotPassword(userRegistrationData));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(FORGOT_PASSWORD);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(FORGOT_PASSWORD);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(FORGOT_PASSWORD);
    }

    public Operation updatePassword(Context ctx, UserRegistrationData userRegistrationData) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(UPDATE_PASSWORD, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();

                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_UPDATE_PASSWORD);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(UPDATE_PASSWORD));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyForUpdatePassword(userRegistrationData));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(UPDATE_PASSWORD);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(UPDATE_PASSWORD);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(UPDATE_PASSWORD);
    }


    public Operation forgotUsername(Context ctx, UserAccountData userAccountData) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(FORGOT_USERNAME, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_FORGOT_USER_NAME);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(FORGOT_USERNAME));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyForForgotUsername(userAccountData));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(FORGOT_USERNAME);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(FORGOT_USERNAME);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(FORGOT_USERNAME);
    }

    public Operation getUserPreferences(Context ctx, String mobileDeviceId) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(GET_USER_PREFERENCE, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_GET_USER_PREFERENCE);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(GET_USER_PREFERENCE));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyForGetUserPreferences(mobileDeviceId));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(GET_USER_PREFERENCE);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(GET_USER_PREFERENCE);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(GET_USER_PREFERENCE);
    }

    public Operation updateUserPreferences(Context ctx, UserPreferenceData userPreferenceData) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(UPDATE_USER_PREFERENCE, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                VerizonHttpRequest aHttpRequest = (VerizonHttpRequest) createRequest();
                String url = getHost() + Config.getInstance(null).getUrlProperty(ConfigKeys.PROP_UPDATE_USER_PREFERENCE);
                aHttpRequest.setUrl(url);
                HashMap<String, String> map = (HashMap<String, String>) aHttpRequest.getHeaders();
                if (map == null)
                    map = new HashMap<String, String>();
                map.put("operation", Integer.toString(UPDATE_USER_PREFERENCE));

                aHttpRequest.setMethod(RestClient.POST);
                aHttpRequest.setHeaders(map);
                aHttpRequest.setData(prepareBodyForUpdateUserPreferences(userPreferenceData));
                // Set tag is required, as Volley checks for the Tag at the time of cancelling the request.
                // If Tag is not set and if app is trying to cancel the request then this causes crash with message "Cannot cancelAll with a null tag".
                aHttpRequest.setTag(TAG);
                submit(aHttpRequest);
                Operation opr = mOperationsModel.getOperation(UPDATE_USER_PREFERENCE);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(UPDATE_USER_PREFERENCE);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(UPDATE_USER_PREFERENCE);
    }

    public Operation refresh(Context ctx, String username, String password) {
        if (mOperationsModel.addOperation(mOperationFactory.createOperation(REFRESH, Operation.CREATED))) {
            if (NetworkUtil.isNetworkAvailable(ctx)) {
                authenticate(ctx, username, password);
                Operation opr = mOperationsModel.getOperation(REFRESH);
                opr.setState(Operation.PENDING);
                onProgress(opr);
            } else {
                Operation opr = mOperationsModel.getOperation(REFRESH);
                opr.setState(Operation.ERROR);
                ((AlertsControllerOperationData) opr.getData()).setMessage(AppConstants.NETWORK_ERROR);
                onError(opr);
            }
        }
        // if no internet connection
        // get from database
        // else
        // create request submit.
        return mOperationsModel.getOperation(REFRESH);
    }

    public UserRegistrationData getUserRegistrationData() {
        return mUserRegistrationData;
    }

    public void setUserRegistrationData(UserRegistrationData userRegistrationData) {
        mUserRegistrationData = userRegistrationData;
    }

    public String getServerSignedCertificate() {
        if (mServerSignedCertificate.isEmpty()) {
            mServerSignedCertificate = InDrivePreference.getInstance().getStringData("cert", "");
        }
        return mServerSignedCertificate;
    }

    public UserDetail getPrimaryUserDetail() {
        UserDetail userDetail = ((UserAccountInfoModel) getModel(USER_ACCT_INFO_MODEL)).getPrimary();
        return userDetail;
    }

    private String csr(String certId, String mobileUserId) {
        Log.d(TAG,"layer7 csr certId "+certId+" mobileUserId "+mobileUserId);
        try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            X500Principal subject = new X500Principal("CN = " + certId + "/" + mobileUserId + ", OU = fgdfgretertgdfg, O = " + "SF" + ", L = US");
            ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").build(keyPair.getPrivate());
            PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic());
            PKCS10CertificationRequest csr = builder.build(signer);
            Log.d(TAG, "csr = " + csr.getSubject());
            PKCS12Attributes.getInstance().setPrivateKey(keyPair.getPrivate());

            String type = "CERTIFICATE REQUEST";
            PemObject pem = new PemObject(type, csr.getEncoded());
            StringWriter str = new StringWriter();
            PEMWriter pemWriter = new PEMWriter(str);
            pemWriter.writeObject(pem);
            pemWriter.close();
            str.close();
            Log.d(TAG, "" + str);
            //KeyStoreWrapper.getInstance().store("cert", keyPair.getPrivate());
            //KeyStoreWrapper.getInstance().store("csr-publickey", keyPair.getPublic());
            //mKeyPair = keyPair;
            //mClientCert = createCertificate("CN="+certId, "CN=CA", keyPair.getPublic(), keyPair.getPrivate());
            return Base64Util.getStringAsBase64(str.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static X509Certificate createCertificate(String dn, String issuer, PublicKey publicKey, PrivateKey privateKey) throws Exception {
        X509V3CertificateGenerator certGenerator = new X509V3CertificateGenerator();
        certGenerator.setSerialNumber(BigInteger.valueOf(Math.abs(new Random().nextLong())));
        certGenerator.setIssuerDN(new X509Name(dn));
        certGenerator.setSubjectDN(new X509Name(dn));
        certGenerator.setIssuerDN(new X509Name(issuer)); // Set issuer!
        certGenerator.setNotBefore(Calendar.getInstance().getTime());
        certGenerator.setNotAfter(Calendar.getInstance().getTime());
        certGenerator.setPublicKey(publicKey);
        certGenerator.setSignatureAlgorithm("SHA1withRSA");
        X509Certificate certificate = (X509Certificate) certGenerator.generate(privateKey, "BC");
        return certificate;
    }

    public void handleErrorForOperation(Response aResponse, Operation aOperation) {
        if (aOperation.getId() == Operation.OperationCode.FIND_USER.ordinal()
                || aOperation.getId() == Operation.OperationCode.SEND_AUTH_TOKEN.ordinal()
                || aOperation.getId() == Operation.OperationCode.SEND_AUTH_TOKEN_NEW_DEV_REG.ordinal()
                || aOperation.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()
                || aOperation.getId() == Operation.OperationCode.GET_CERT_ID.ordinal()
                || aOperation.getId() == Operation.OperationCode.AUTHENTICATE.ordinal()
                || aOperation.getId() == Operation.OperationCode.GET_USER_ACCT_VECH_DEV_INFO.ordinal()
                || aOperation.getId() == Operation.OperationCode.GET_USER_PREFERENCE.ordinal()
                || aOperation.getId() == Operation.OperationCode.UPDATE_USER_PREFERENCE.ordinal()
                || aOperation.getId() == Operation.OperationCode.FORGOT_USERNAME.ordinal()
                || aOperation.getId() == Operation.OperationCode.FORGOT_PASSWORD.ordinal()
                || aOperation.getId() == Operation.OperationCode.UPDATE_PASSWORD.ordinal()) {


            Operation refreshOpr = mOperationsModel.getOperation(REFRESH);
            if(refreshOpr == null){
                aOperation.setInformation(aResponse.getResponseCode()+"");
            }
            onError(aOperation);
            if (refreshOpr != null && refreshOpr.getState() == Operation.PENDING) {
                refreshOpr.setState(Operation.ERROR);
                refreshOpr.setInformation("Invalid Data");
                onError(refreshOpr);
            }
        }
    }


    public void handleResponseForOperation(Response aResponse, Operation aOperation) {
        Gson gson = new GsonBuilder().create();
        JSONObject lData = aResponse.getData();
        if (aOperation.getId() == Operation.OperationCode.FIND_USER.ordinal()) {
            if (lData == null) {
                aOperation.setState(Operation.ERROR);
                onError(aOperation);
                return;
            }
            UserAccountData data = gson.fromJson(lData.toString(), UserAccountData.class);
            getUserAccountModel().setData(data);
            onSuccess(aOperation);
        } else if (aOperation.getId() == Operation.OperationCode.SEND_AUTH_TOKEN.ordinal()
                || aOperation.getId() == Operation.OperationCode.SEND_AUTH_TOKEN_NEW_DEV_REG.ordinal()) {
            if (lData == null) {

            }
            onSuccess(aOperation);
        } else if (aOperation.getId() == Operation.OperationCode.GET_CERT_ID.ordinal()) {
            if (lData == null) {

                return;
            }

            try {
                String mCertId = lData.getString("CertID");
                mCertIdUserMap.put(mUserRegistrationData.getMobileUserID(), mCertId);
                validateRegistration(null, mUserRegistrationData);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            onSuccess(aOperation);
        } else if (aOperation.getId() == Operation.OperationCode.VALIDATE_REGISTRATION.ordinal()) {
            if (lData == null) {

                return;
            }
//             String token = aResponse.getFirstHeader(Constants.TOKEN).getValue();
            try {
                mServerSignedCertificate = lData.getString("cert");
                Log.d(TAG,"layer7 got cert from the server "+mServerSignedCertificate);

                String serverSignedCertificate =  Base64Util.decodeBase64AsString(mServerSignedCertificate);
//                InDrivePreference.getInstance().setStringData("cert", Base64Util.decodeBase64AsString(serverSignedCertificate));
//                Log.d(TAG,"layer7 got cert from the server after decode "+InDrivePreference.getInstance().getStringData("cert",serverSignedCertificate));

                CryptoManager cryptoManager = new CryptoManager(Algorithams.AES);
                FileUtils.saveCert(IndriveApplication.getInstance(),cryptoManager.process(serverSignedCertificate));
//                String cert = FileUtils.readCertFromAppInternal(IndriveApplication.getInstance());
//                Log.d(TAG,"after decoded the decrypted "+Base64Util.decodeBase64AsString(cert));


//
//                String encryptedCert = CryptoWrapper.encrypt(mServerSignedCertificate);
//                Log.d(TAG,"layer7 encrypted cert by CryptoWrapper "+encryptedCert);
//
//                String decryptedCert = CryptoWrapper.decrypt(encryptedCert);
//                Log.d(TAG,"layer7 decrypted cert by CryptoWrapper "+decryptedCert);


//                InDrivePreference.getInstance().setStringData("cert", serverSignedCertificate);
//
//                InDrivePreference.getInstance().setStringData(AppController.instance().getMobileUserId(), serverSignedCertificate);

                secureCertificate(AppController.instance().getMobileUserId());

//                Certificate chain[] = { mClientCert, VerizonSSLSocketFactoryClient.instance().getCertificate(mServerSignedCertificate)};
//                KeyStoreWrapper.getInstance().store("cert", VerizonSSLSocketFactoryClient.instance().getCertificate(mServerSignedCertificate));
//                KeyStoreWrapper.getInstance().store("cert", "mypass".getBytes(), chain);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            catch (KeyStoreException e){
//                e.printStackTrace();
//            }
            onSuccess(aOperation);
        } else if (aOperation.getId() == Operation.OperationCode.AUTHENTICATE.ordinal()) {
            // Get token from header and store for next request.
            onSuccess(aOperation);
            Operation refreshOpr = mOperationsModel.getOperation(REFRESH);
            if (refreshOpr != null && refreshOpr.getState() == Operation.PENDING) {
                getUserAccountVehicleDeviceInfo(null, AppController.instance().getMobileUserId());
            }
        } else if (aOperation.getId() == Operation.OperationCode.GET_USER_ACCT_VECH_DEV_INFO.ordinal()) {
            Log.d(TAG,"fetching kitchen sink");
            Operation refreshOpr = mOperationsModel.getOperation(REFRESH);
            if (lData == null) {
                aOperation.setState(Operation.ERROR);
                onError(aOperation);
                if (refreshOpr != null && refreshOpr.getState() == Operation.PENDING) {
                    refreshOpr.setState(Operation.ERROR);
                    onError(refreshOpr);
                }
                Toast.makeText(IndriveApplication.getInstance(),IndriveApplication.getInstance().getResources().getString(R.string.kitchen_sink_failure),Toast.LENGTH_LONG).show();
                ControllerUtils.appLogout(IndriveApplication.getInstance());
                return;
            }
            // creating model as per response need to re-factored.
            try {
                ((UserAccountInfoModel) getModel(USER_ACCT_INFO_MODEL)).setData(lData);
            } catch (JSONException e) {
                e.printStackTrace();
                aOperation.setState(Operation.ERROR);
                aOperation.setInformation("Invalid Data");
                onError(aOperation);

                if (refreshOpr != null && refreshOpr.getState() == Operation.PENDING) {
                    refreshOpr.setState(Operation.ERROR);
                    refreshOpr.setInformation("Invalid Data");
                    onError(refreshOpr);
                }
            }
            // ended
            onSuccess(aOperation);
            if (refreshOpr != null && refreshOpr.getState() == Operation.PENDING) {
                refreshOpr.setState(Operation.FINISHED);
                onSuccess(refreshOpr);
            }
        } else if (aOperation.getId() == Operation.OperationCode.GET_USER_VEHICLES.ordinal()) {
            if (lData == null) {
                aOperation.setState(Operation.ERROR);
                onError(aOperation);
                return;
            }
            onSuccess(aOperation);
        } else if (aOperation.getId() == Operation.OperationCode.GET_USER_PREFERENCE.ordinal()) {
            if (lData == null) {
                aOperation.setState(Operation.ERROR);
                onError(aOperation);
                return;
            }
            UserPreferenceData data = gson.fromJson(lData.toString(), UserPreferenceData.class);
            getUserPreferencesModel().setData(data);
            onSuccess(aOperation);
        } else if (aOperation.getId() == Operation.OperationCode.UPDATE_USER_PREFERENCE.ordinal()){
            onSuccess(aOperation);
        } else if (aOperation.getId() == Operation.OperationCode.FORGOT_USERNAME.ordinal()) {
            onSuccess(aOperation);
        }
        else if (aOperation.getId() == Operation.OperationCode.FORGOT_PASSWORD.ordinal()) {
            onSuccess(aOperation);
        }
        else if (aOperation.getId() == Operation.OperationCode.UPDATE_PASSWORD.ordinal()) {
            onSuccess(aOperation);
        }
    }

    private void secureCertificate(String mobileUserId){
//        CryptoManager cryptoManager = new CryptoManager(Algorithams.AES);
//        FileUtils.saveCert(IndriveApplication.getInstance(),cryptoManager.process(serverSignedCertificate));
//        String cert = FileUtils.readCertFromAppInternal(IndriveApplication.getInstance());
        CreatePKCS12.createPKCS12(mobileUserId);
        IndriveApplication.getInstance().reloadCertificate("");
//        IndriveApplication.getInstance().reloadCertificate1();
    }

    public BaseModel getUserAccountVehicleDeviceInfoModel() {
        return getModel(USER_ACCT_INFO_MODEL);
    }
}
