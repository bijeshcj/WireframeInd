package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.models.AlertsControllerOperation;
import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by bijesh on 12/31/2014.
 */
public class AlertsControllerOperationFactory implements OperationFactory {

    private static AlertsControllerOperationFactory mInstance;

    private AlertsControllerOperationFactory(){

    }

    public static AlertsControllerOperationFactory instance(){
        if(mInstance == null){
            mInstance =  new AlertsControllerOperationFactory();
        }
        return mInstance;
    }


    @Override
    public Operation createOperation(int id, int state) {
        return new AlertsControllerOperation(id, state);
    }
}
