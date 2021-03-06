package com.verizontelematics.indrivemobile.utils.errormanagement;

import android.content.Context;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by bijesh on 6/4/2015.
 */
public class AuthenticateErrorController implements ErrorManager {



    @Override
    public String getResponseDescription(Context context, int responseCode,Operation.OperationCode operationCode, String exceptionMessage) {

        if(responseCode == SUCCESS_RESPONSE_CODE){
            return context.getResources().getString(R.string.success_response);
        }else if(responseCode == FAILURE_RESPONSE_CODE){
            return context.getResources().getString(R.string.failure_response)+" : "+exceptionMessage;
        }
        switch (operationCode) {
            case AUTHENTICATE:
                 if(responseCode == INVALID_USER_NAME_PASSWORD){
                     return "Invalid Username/password";
                 }
                break;
        }

//        handling common failure
        switch (responseCode){
            case GENERAL_FAILURE_1:
            case GENERAL_FAILURE_2:
            case GENERAL_FAILURE_3:
            case GENERAL_FAILURE_4:
            case BAD_REQUEST:
            case INVALID_TIMESTAMP:
                return "Server Error";
            case SERVICE_NOT_FOUND:
            case SERVICE_OPERATION:
            case REQUESTOR_IP:
            case RATE_LIMIT:
            case QUOTA_EXCEED:
            case MESSAGE_REPLAY:
            case INVALID_CSR:
            case REG_EXISTS:
            case INVALID_REG:
            case USER_NOT_AUTH_1:
            case USER_NOT_AUTH_2:
            case USER_NOT_AUTH_3:
                return "Authorization denied";

            case MISSING_USER_NAME:
                return "Missing User Name";
            case MISSING_PASSWORD:
                return "Missing Password";
            case MISSING_CERTIFICATE:
                return "Missing Certificate";
            case MISSING_API_KEY:
                return "Missing Api Key";
            case MISSING_TOKEN:
                return "Missing Token";
            case INVALID_CERTIFICATE:
                return "Invalid Certificate";
            case INVALID_API_KEY:
                return "Invalid Api Key";
            case INVALID_TOKEN:
                return "Invalid Token";
            case INVALID_PASSWORD:
                return "Invalid Password";

        }

            return null;
    }


}
