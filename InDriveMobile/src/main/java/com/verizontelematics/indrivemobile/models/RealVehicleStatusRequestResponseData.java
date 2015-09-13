package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.models.data.BaseData;

/**
 * Created by z688522 on 2/6/15.
 */
public class RealVehicleStatusRequestResponseData extends BaseData {

    public String mResponseCode;
    public String mResponseDescription;
    public String mResponseStatus;
    public String mResponseStatusCode;
    public String mSourceName;
    public String mTargetName;
    public long mTimeStamp;
    public long mVehicleStatusId;

    public RealVehicleStatusRequestResponseData()
    {
        super();
    }
}
