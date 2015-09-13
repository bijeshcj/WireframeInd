package com.verizontelematics.indrivemobile.controllers;


import com.verizontelematics.indrivemobile.models.LocationDataControllerOperation;
import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by bijesh on 1/30/2015.
 */
public class LocationControllerOperationFactory implements OperationFactory {

    private static LocationControllerOperationFactory mInstance = null;

    private LocationControllerOperationFactory() {

    }

    public static LocationControllerOperationFactory instance() {
        if (mInstance == null) {
            mInstance = new LocationControllerOperationFactory();
        }
        return mInstance;
    }

    @Override
    public Operation createOperation(int oprId, int oprState) {
        return new LocationDataControllerOperation(oprId, oprState);
    }
}
