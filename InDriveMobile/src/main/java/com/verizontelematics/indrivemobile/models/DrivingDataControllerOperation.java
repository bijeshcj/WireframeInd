package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.models.data.DrivingDataControllerOperationData;

/**
 * Created by z688522 on 12/9/14.
 */
public class DrivingDataControllerOperation extends Operation {

    public DrivingDataControllerOperation(int id, int state) {
        super(id, state);
        mData = new DrivingDataControllerOperationData();
    }

    @Override
    public String getInformation() {
        if (mData == null)
            return "";
        return ((DrivingDataControllerOperationData)mData).getMessage();
    }

    @Override
    public void setInformation(String message) {
        ((DrivingDataControllerOperationData)mData).setMessage(message);
        setChanged();
        notifyObservers(this);
    }
}
