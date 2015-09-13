package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.models.data.AlertData;

/**
 * Created by z688522 on 9/11/14.
 */
public class AlertModel extends BaseModel {
    private Boolean mActive = true;

    public void setData(String xml) {

    }

    public void setData(String aType, String aName, String aDetail, long aTime, String aMessage,int mileage,String userSelection) {
        if (mData == null) {
            mData = new AlertData(aType, aName, aDetail, aTime, aMessage,mileage,userSelection);
        }
        setChanged();
        notifyObservers(mData);
    }

    public void setActive(boolean active) {
        if (mActive == active)
            return;
        mActive = active;
        setChanged();
        notifyObservers(mActive);
    }

    public Boolean isActive() {
        return mActive;
    }
}
