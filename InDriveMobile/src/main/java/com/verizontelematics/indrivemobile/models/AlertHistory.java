package com.verizontelematics.indrivemobile.models;

/**
 * Created by Priyanga on 8/26/2014.
 */
public class AlertHistory extends BaseModel {


    private String alertDetail;
    private String alertTime;
    private String alertMessage;

    public AlertHistory(String alertDetail, String alertTime, String alertMessage) {
        this.alertDetail = alertDetail;
        this.alertTime = alertTime;
        this.alertMessage = alertMessage;
    }

    public String getAlertDetail() {
        return alertDetail;
    }

    public void setAlertDetail(String alertDetail) {
        this.alertDetail = alertDetail;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }
}
