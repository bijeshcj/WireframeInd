package com.verizontelematics.indrivemobile.models.data;

/**
 * Created by z688522 on 9/11/14.
 */
public class AlertData extends BaseData {

    private String mType;
    private String mName;
    private String mDetail;
    private long mTime;
    private String mMessage;
    private int mileage_interval;
    private String userSelection;

    public AlertData(){

    }


    public AlertData(String aType, String aName, String aDetail, long aTime, String aMessage){
        mType = aType;
        mName = aName;
        mDetail = aDetail;
        mTime = aTime;
        mMessage = aMessage;
    }


    public AlertData(String aType, String aName, String aDetail, long aTime, String aMessage,int mileage_interval,String userSelection) {
        mType = aType;
        mName = aName;
        mDetail = aDetail;
        mTime = aTime;
        mMessage = aMessage;
        this.mileage_interval = mileage_interval;
        this.userSelection = userSelection;

    }

    public int getMileage_interval() {
        return mileage_interval;
    }

    public void setMileage_interval(int mileage_interval) {
        this.mileage_interval = mileage_interval;
    }

    public String getUserSelection() {
        return userSelection;
    }

    public void setUserSelection(String userSelection) {
        this.userSelection = userSelection;
    }

    public void setType(String aAlertType) {
        mType = aAlertType;
    }

    public void setName(String aName) {
        mName = aName;
    }

    public void setDetail(String aDetail) {
        mDetail = aDetail;
    }

    public String getType() { return mType; }
    public String getName() { return mName; }
    public String getDetail() { return mDetail; }
    public long getTime() { return mTime; }
    public String getMessage() { return mMessage; }

    public void set(String aType, String aName, String aDetail, long aTime, String aMessage) {
        mType = aType;
        mName = aName;
        mDetail = aDetail;
        mTime = aTime;
        mMessage = aMessage;
    }

    @Override
    public String toString() {
        return "Remainder serviceType: "+this.mType;
    }
}
