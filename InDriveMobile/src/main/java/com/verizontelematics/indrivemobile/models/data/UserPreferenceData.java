package com.verizontelematics.indrivemobile.models.data;

/**
 * Created by z688522 on 3/6/15.
 */
public class UserPreferenceData extends BaseData {


    private String AccountID;
    private String PushNotification;
    private String EmailNotification;
    private String SmsNotification;

    public UserPreferenceData() {
        super();
    }


    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getPushNotification() {
        return PushNotification;
    }

    public void setPushNotification(String pushNotification) {
        PushNotification = pushNotification;
    }

    public String getEmailNotification() {
        return EmailNotification;
    }

    public void setEmailNotification(String emailNotification) {
        EmailNotification = emailNotification;
    }

    public String getSmsNotification() {
        return SmsNotification;
    }

    public void setSmsNotification(String smsNotification) {
        SmsNotification = smsNotification;
    }
}
