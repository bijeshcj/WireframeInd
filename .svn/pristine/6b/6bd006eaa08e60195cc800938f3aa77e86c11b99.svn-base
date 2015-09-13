package com.verizontelematics.indrivemobile.models;

/**
 * Created by z688522 on 2/6/15.
 */
public class RealVehicleEventData implements EventData {

    private Object mUserData = "";
    private String mEventId = "";
    private String mErrorMessage = "";
    public RealVehicleEventData(PollOperation aOpr) {
        // TODO Auto-generated constructor stub
        mUserData = aOpr.getResponse();
        mEventId = aOpr.getOperation();
    }

    public RealVehicleEventData(PollOperation aOpr, String error) {
        // TODO Auto-generated constructor stub
        mUserData = aOpr.getResponse();
        mEventId = aOpr.getOperation();
        mErrorMessage = error;
    }

    @Override
    public Object getUserData() {
        // TODO Auto-generated method stub
        return mUserData;
    }

    public void setUserData(String xmlStr) {
        mUserData = xmlStr;
    }

    @Override
    public boolean isStopPropagation() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getEventId() {
        // TODO Auto-generated method stub
        return mEventId;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String err) {
        mErrorMessage =  err;
    }

}

