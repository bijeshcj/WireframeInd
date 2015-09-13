package com.verizontelematics.indrivemobile.models;


import com.verizontelematics.indrivemobile.models.data.LocationDataControllerOperationData;

/**
 * Created by bijesh on 1/30/2015.
 */
public class LocationDataControllerOperation extends Operation {

    private PollOperation mPollOperation;

    public LocationDataControllerOperation(int id, int state) {
        super(id, state);
        mData = new LocationDataControllerOperationData();
    }



    @Override
    public String getInformation() {
        if (LocationDataControllerOperationData.class.isInstance(mData)) {
           return  ((LocationDataControllerOperationData)mData).getMessage();
        }
        return "";
    }

    @Override
    public void setInformation(String message) {
        if (LocationDataControllerOperationData.class.isInstance(mData)) {
            ((LocationDataControllerOperationData)mData).setMessage(message);
        }
    }

    public void setPollOperation(PollOperation pollOperation) {
        mPollOperation = pollOperation;
    }

    @Override
    public void clean() {
        super.clean();
        if (mPollOperation != null) {
            mPollOperation.clean();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mPollOperation != null) {
            mPollOperation.cancel();
        }
    }
}
