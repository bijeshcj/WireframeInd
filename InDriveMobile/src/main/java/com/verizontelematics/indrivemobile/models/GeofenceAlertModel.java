package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.models.data.GeofenceAlertData;

/**
 * Created by z688522 on 9/11/14.
 */
public class GeofenceAlertModel extends AlertModel {

    public GeofenceAlertModel() {
        super();
    }

    public void setData(String aType, String aName, String aDetail, long aTime, String aMessage) {
        if (mData == null) {
            mData = new GeofenceAlertData(aType, aName, aDetail, aTime, aMessage);
        }
        setChanged();
        notifyObservers(mData);
    }
}
