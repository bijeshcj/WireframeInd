package com.verizontelematics.indrivemobile.models.data;

import java.util.List;

/**
 * Created by z688522 on 3/12/15.
 */
public class UserDetail {
    UserAccountInfoData AccountInfo;
    List<Vehicle> VehiclesInfo;
    UserPreferenceData NotificationPreferenceInfo;

    public UserAccountInfoData getAccountInfo() {
        return AccountInfo;
    }

    public void setAccountInfo(UserAccountInfoData accountInfo) {
        AccountInfo = accountInfo;
    }

    public List<Vehicle> getVehiclesInfo() {
        return VehiclesInfo;
    }

    public void setVehiclesInfo(List<Vehicle> vehiclesInfo) {
        VehiclesInfo = vehiclesInfo;
    }

    public UserPreferenceData getNotificationPreferenceInfo() {
        return NotificationPreferenceInfo;
    }

    public void setNotificationPreferenceInfo(UserPreferenceData notificationPreferenceInfo) {
        NotificationPreferenceInfo = notificationPreferenceInfo;
    }
}
