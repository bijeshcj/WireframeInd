package com.verizontelematics.indrivemobile.models;

/**
 * Created by z688522 on 10/23/14.
 */
public class DiagnosticControllerOperation extends Operation {

    public DiagnosticControllerOperation(int id, int state) {
        super(id, state);
        mData = new DiagnosticControllerOperationData();
    }

    @Override
    public String getInformation() {
        if (mData == null)
            return "";
        return ((DiagnosticControllerOperationData)mData).getMessage();
    }

    @Override
    public void setInformation(String message) {
        ((DiagnosticControllerOperationData)mData).setMessage(message);
        setChanged();
        notifyObservers(this);
    }
}
