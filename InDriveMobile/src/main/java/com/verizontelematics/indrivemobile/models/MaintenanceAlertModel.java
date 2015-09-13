package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.models.data.MaintenanceAlertData;

/**
 * Created by z688522 on 9/11/14.
 */
public class MaintenanceAlertModel extends AlertModel {
    public MaintenanceAlertModel() {
        super();
    }
    public void setData(String aType, String aName, String aDetail, long aTime, String aMessage) {
        if (mData == null) {
            mData = new MaintenanceAlertData(aType, aName, aDetail, aTime, aMessage);
        }else {
            ((MaintenanceAlertData) mData).set(aType, aName, aDetail, aTime, aMessage);
        }
        setChanged();
        notifyObservers(mData);
    }
}
