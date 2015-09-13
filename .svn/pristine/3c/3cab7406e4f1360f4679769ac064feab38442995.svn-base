package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.models.DrivingDataControllerOperation;
import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by z688522 on 12/9/14.
 */
public class DrivingDataControllerOperationFactory implements OperationFactory {
    private static DrivingDataControllerOperationFactory mInstance = null;

    private DrivingDataControllerOperationFactory() {

    }

    public static DrivingDataControllerOperationFactory instance() {
        if (mInstance == null) {
            mInstance = new DrivingDataControllerOperationFactory();
        }
        return mInstance;
    }

    @Override
    public Operation createOperation(int id, int state) {
        return new DrivingDataControllerOperation(id, state);
    }
}
