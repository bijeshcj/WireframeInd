package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.models.DiagnosticControllerOperation;
import com.verizontelematics.indrivemobile.models.Operation;

/**
 * Created by z688522 on 10/23/14.
 */
public class DiagnosticControllerOperationFactory implements OperationFactory {

    private static DiagnosticControllerOperationFactory mInstance = null;

    private DiagnosticControllerOperationFactory() {

    }

    public static DiagnosticControllerOperationFactory instance() {
        if (mInstance == null) {
            mInstance = new DiagnosticControllerOperationFactory();
        }
        return mInstance;
    }
    @Override
    public Operation createOperation(int id, int state) {
        return new DiagnosticControllerOperation(id, state);
    }
}
