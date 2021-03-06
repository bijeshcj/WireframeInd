package com.verizontelematics.indrivemobile.utils.errormanagement;

import android.content.Context;

import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by bijesh on 3/27/2015.
 *
 * Error Manager follows Strategy Pattern
 *
 */
public interface ErrorManager {

    int SUCCESS_RESPONSE_CODE = 2000;
    int FAILURE_RESPONSE_CODE = 1002;


    int MISSING_USER_NAME = 4130001;
    int MISSING_PASSWORD = 4130002;
    int MISSING_CERTIFICATE = 4130003;
    int MISSING_API_KEY = 4130004;
    int MISSING_TOKEN = 4130005;
    int INVALID_USER_NAME_PASSWORD = 4230001;
    int INVALID_CERTIFICATE = 4230002;
    int INVALID_API_KEY = 4230003;
    int INVALID_TOKEN = 4230004;
    int INVALID_PASSWORD = 4330013;


//    Authorization denied
    int SERVICE_NOT_FOUND = 4330001;
    int SERVICE_OPERATION = 4330002;
    int REQUESTOR_IP = 4330003;
    int RATE_LIMIT = 4330004;
    int QUOTA_EXCEED = 4330005;
    int MESSAGE_REPLAY = 4330006;
    int INVALID_CSR = 4330007;
    int REG_EXISTS = 4330008;
    int INVALID_REG = 4330009;
    int USER_NOT_AUTH_1 = 4330010;
    int USER_NOT_AUTH_2 = 4330011;
    int USER_NOT_AUTH_3 = 4330012;



    int GENERAL_FAILURE_1 = 5130001;
    int GENERAL_FAILURE_2 = 5130002;
    int GENERAL_FAILURE_3 = 5230001;
    int GENERAL_FAILURE_4 = 5330001;

    int BAD_REQUEST = 6130001;
    int INVALID_TIMESTAMP = 6330001;




    String getResponseDescription(Context context,int responseCode,Operation.OperationCode operationCode,String exceptionMessage);
}
