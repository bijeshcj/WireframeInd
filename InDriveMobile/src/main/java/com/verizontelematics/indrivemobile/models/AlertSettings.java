package com.verizontelematics.indrivemobile.models;

/**
 * Created by z684985 on 9/23/14.
 */
public class AlertSettings {
    private String alertDetail;
    private String alertTime;
    private boolean alertToggle;

    public boolean isAlertToggle() {
        return alertToggle;
    }

    public void setAlertToggle(boolean alertToggle) {
        this.alertToggle = alertToggle;
    }

    public AlertSettings(String alertDetail,String alertTime,boolean alertToggle)
    {

        this.alertDetail=alertDetail;
        this.alertTime= alertTime;
        this.alertToggle = alertToggle;
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
}
