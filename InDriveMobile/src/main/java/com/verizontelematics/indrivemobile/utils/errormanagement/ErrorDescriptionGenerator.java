package com.verizontelematics.indrivemobile.utils.errormanagement;

import android.content.Context;
import android.util.Log;

import com.verizontelematics.indrivemobile.models.Operation;

import org.json.JSONObject;

/**
 * Created by bijesh on 3/27/2015.
 * <p/>
 * <p/>
 * ErrorDescriptionGenerator follows Strategy Pattern
 */
public class ErrorDescriptionGenerator {
    private static final String TAG = ErrorDescriptionGenerator.class.getCanonicalName();

    private ErrorManager errorManager;

    public ErrorDescriptionGenerator(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * Entry point to get the Response description for any given particular ErrorController Module type
     * e.g DiagnosticsErrorController
     *
     * @param response
     * @param operationCodeId
     * @return
     */
    public String executeGetResponseDescription(Context context, String response, int operationCodeId, String exceptionMessage) {
        return errorManager.getResponseDescription(context, getErrorCode(response), getOperation(operationCodeId), exceptionMessage);
    }


    private int getErrorCode(String dataAsString) {
        try {
            JSONObject json = new JSONObject(dataAsString);
            String res = json.getString("ErrorCode");
            if (res != null && res.trim().length() > 0) {
                return Integer.valueOf(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Operation.OperationCode getOperation(int id) {
        switch (id) {
            case 0:
                return Operation.OperationCode.GET_MAINTENANCE_REMINDERS;
            case 1:
                return Operation.OperationCode.GET_MAINTENANCE_LOGS;
            case 2:
                return Operation.OperationCode.ADD_MAINTENANCE_LOGS;
            case 3:
                return Operation.OperationCode.DELETE_MAINTENANCE_LOG;
            case 4:
                return Operation.OperationCode.UPDATE_MAINTENANCE_LOG;
            case 5:
                return Operation.OperationCode.ADD_MAINTENANCE_REMINDER;
            case 6:
                return Operation.OperationCode.DELETE_MAINTENANCE_REMINDER;
            case 7:
                return Operation.OperationCode.UPDATE_MAINTENANCE_REMINDER;
            case 8:
                return Operation.OperationCode.ADD_MAINTENANCE_REMINDERS;
            case 9:
                return Operation.OperationCode.DELETE_MAINTENANCE_REMINDERS;
            case 10:
                return Operation.OperationCode.UPDATE_MAINTENANCE_REMINDERS;
            case 11:
                return Operation.OperationCode.GET_RECALLS;
            case 12:
                return Operation.OperationCode.SET_RECALL_COMPLETED;
            case 13:
                return Operation.OperationCode.GET_VEHICLE_HEALTH;
            case 14:
                return Operation.OperationCode.GET_DRIVING_DATA;
            case 15:
                return Operation.OperationCode.GET_ALERTS;
            case 16:
                return Operation.OperationCode.UPDATE_SPEED_ALERTS;
            case 17:
                return Operation.OperationCode.CREATE_LOCATION_ALERT;
            case 18:
                return Operation.OperationCode.UPDATE_LOCATION_ALERT;
            case 19:
                return Operation.OperationCode.UPDATE_DIAGNOSTIC_ALERTS;
            case 20:
                return Operation.OperationCode.GET_ALERTS_HISTORY;
            case 21:
                return Operation.OperationCode.GET_LOCATION_HISTORY;
            case 22:
                return Operation.OperationCode.CREATE_VALET_ALERT;
            case 23:
                return Operation.OperationCode.UPDATE_VALET_ALERT;
            case 24:
                return Operation.OperationCode.DELETE_LOCATION_ALERT;
            case 25:
                return Operation.OperationCode.FIND_USER;
            case 26:
                return Operation.OperationCode.SEND_AUTH_TOKEN;
            case 27:
                return Operation.OperationCode.GET_LOCATION;
            case 28:
                return Operation.OperationCode.GET_CERT_ID;
            case 29:
                return Operation.OperationCode.AUTHENTICATE;
            case 30:
                return Operation.OperationCode.GET_USER_ACCT_VECH_DEV_INFO;
            case 31:
                return Operation.OperationCode.GET_USER_VEHICLES;
            case 32:
                return Operation.OperationCode.VALIDATE_REGISTRATION;
            case 33:
                return Operation.OperationCode.FORGOT_PASSWORD;
            case 34:
                return Operation.OperationCode.FORGOT_USERNAME;
            case 35:
                return Operation.OperationCode.UPDATE_PASSWORD;
            case 36:
                return Operation.OperationCode.GET_USER_PREFERENCE;
            case 37:
                return Operation.OperationCode.REFRESH;
            case 38:
                return Operation.OperationCode.REGISTER_PUSH;
            case 39:
                return Operation.OperationCode.DEREGISTER_PUSH;
            case 40:
                return Operation.OperationCode.UPDATE_USER_PREFERENCE;
            case 41:
                return Operation.OperationCode.SEND_AUTH_TOKEN_NEW_DEV_REG;
        }
        return null;
    }


}
