package com.verizontelematics.indrivemobile.models;


/**
 * Created by bijesh on 12/31/2014.
 */
public class AlertsControllerOperation extends Operation {

    public AlertsControllerOperation(int id,int state){
        super(id,state);
        mData = new AlertsControllerOperationData();
    }

    @Override
    public String getInformation() {
        if (mData == null)
            return "";
        return ((AlertsControllerOperationData)mData).getMessage();
    }

    @Override
    public void setInformation(String message) {
        ((AlertsControllerOperationData)mData).setMessage(message);
        setChanged();
        notifyObservers(this);
    }

}
