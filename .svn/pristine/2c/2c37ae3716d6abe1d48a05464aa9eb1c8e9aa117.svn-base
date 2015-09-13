package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.models.data.RecallAlertData;

/**
 * Created by z688522 on 10/7/14.
 */
public class RecallAlertModel extends AlertModel {
    public RecallAlertModel() {
        super();
    }
    public void setData(String aType, String aName, String aDetail, long aTime, String aMessage) {
        if (mData == null) {
            mData = new RecallAlertData(aType, aName, aDetail, aTime, aMessage);
        }else {
            ((RecallAlertData) mData).set(aType, aName, aDetail, aTime, aMessage);
        }
        setChanged();
        notifyObservers(mData);
    }
}
