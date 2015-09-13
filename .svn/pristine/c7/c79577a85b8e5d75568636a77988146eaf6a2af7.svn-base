package com.verizontelematics.indrivemobile.utils.errormanagement;

import android.content.Context;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by bijesh on 3/27/2015.
 */
public class DiagnosticsErrorController implements ErrorManager {

    private static final String TAG = DiagnosticsErrorController.class.getCanonicalName();


    @Override
    public String getResponseDescription(Context context,int responseCode, Operation.OperationCode operationCode,String exceptionMessage) {
        if(responseCode == SUCCESS_RESPONSE_CODE){
              return context.getResources().getString(R.string.success_response);
        }else if(responseCode == FAILURE_RESPONSE_CODE){
              return context.getResources().getString(R.string.failure_response)+" : "+exceptionMessage;
        }
        switch (operationCode){
//            VehicleHealth module
            case GET_VEHICLE_HEALTH:

                break;

//            Maintenance Log module
            case GET_MAINTENANCE_LOGS:

                break;
            case ADD_MAINTENANCE_LOGS:

                if(responseCode == 1402){
                    return context.getResources().getString(R.string.vehicle_does_not_exists);
                }
                break;

            case DELETE_MAINTENANCE_LOG:

                break;
            case UPDATE_MAINTENANCE_LOG:

                break;

//            Maintenance Remainder module
            case ADD_MAINTENANCE_REMINDERS:

                break;
            case DELETE_MAINTENANCE_REMINDERS:

                break;
            case UPDATE_MAINTENANCE_REMINDERS:

                break;
//            Recall Module
            case GET_RECALLS:

                break;
            case SET_RECALL_COMPLETED:

                break;
        }
        return null;
    }



}
