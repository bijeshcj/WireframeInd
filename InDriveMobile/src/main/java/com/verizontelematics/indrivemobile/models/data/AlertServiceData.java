package com.verizontelematics.indrivemobile.models.data;


public class AlertServiceData extends BaseData {

    private String serviceName;
    private int isPredefined;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getIsPredefined() {
        return isPredefined;
    }

    public void setIsPredefined(int isPredefined) {
        this.isPredefined = isPredefined;
    }

}