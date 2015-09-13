package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z688522 on 10/24/14.
 */
public class MaintenanceReminderData implements Parcelable  {


    private String ReminderDescription;
    private boolean NotificationFlagSms;
    private String ServiceType;
    private long ReminderDate;
    private Double ReminderConfigMiles;
    private long LastNotificationDate;
    private String SmsPhoneNumber;
    private String Email;
    private String ReminderName;
    private String ServiceName;
    private String ReminderID;
    private double LastOdometer;
    private boolean NotificationFlagEmail;
    private Long ReminderConfigMonths;

    public MaintenanceReminderData() {

    }

    public MaintenanceReminderData(String reminderDescription, boolean notificationFlagSms, String serviceType,
                                   long reminderDate, Double reminderConfigMiles, long lastNotificationDate, String smsPhoneNumber,
                                   String email, String reminderName, String serviceName, String reminderID, double lastOdometer,
                                   boolean notificationFlagEmail, long reminderConfigMonths) {
        ReminderDescription = reminderDescription;
        NotificationFlagSms = notificationFlagSms;
        ServiceType = serviceType;
        ReminderDate = reminderDate;
        ReminderConfigMiles = reminderConfigMiles;
        LastNotificationDate = lastNotificationDate;
        SmsPhoneNumber = smsPhoneNumber;
        Email = email;
        ReminderName = reminderName;
        ServiceName = serviceName;
        ReminderID = reminderID;
        LastOdometer = lastOdometer;
        NotificationFlagEmail = notificationFlagEmail;
        ReminderConfigMonths = reminderConfigMonths;
    }

    public String getReminderDescription() {
        return ReminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.ReminderDescription = reminderDescription;
    }

    public boolean isNotificationFlagSms() {
        return NotificationFlagSms;
    }

    public void setNotificationFlagSms(boolean notificationFlagSms) {
        NotificationFlagSms = notificationFlagSms;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public long getReminderDate() {
        return ReminderDate;
    }

    public void setReminderDate(long reminderDate) {
        ReminderDate = reminderDate;
    }

    public double getReminderConfigMiles() {
        if(ReminderConfigMiles != null) {
            double conversionVariable = ReminderConfigMiles;
            return conversionVariable;
        }else{
            return 0.0;
        }
    }

    public void setReminderConfigMiles(Double reminderConfigMiles) {
        ReminderConfigMiles = reminderConfigMiles;
    }

    public long getLastNotificationDate() {
        return LastNotificationDate;
    }

    public void setLastNotificationDate(long lastNotificationDate) {
        LastNotificationDate = lastNotificationDate;
    }

    public String getSmsPhoneNumber() {
        return SmsPhoneNumber;
    }

    public void setSmsPhoneNumber(String smsPhoneNumber) {
        SmsPhoneNumber = smsPhoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getReminderName() {
        return ReminderName;
    }

    public void setReminderName(String reminderName) {
        ReminderName = reminderName;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getReminderID() {
        return ReminderID;
    }

    public void setReminderID(String reminderID) {
        ReminderID = reminderID;
    }

    public double getLastOdometer() {
        return LastOdometer;
    }

    public void setLastOdometer(double lastOdometer) {
        LastOdometer = lastOdometer;
    }

    public boolean isNotificationFlagEmail() {
        return NotificationFlagEmail;
    }

    public void setNotificationFlagEmail(boolean notificationFlagEmail) {
        NotificationFlagEmail = notificationFlagEmail;
    }

    public long getReminderConfigMonths() {
        if(ReminderConfigMonths == null)
            return 0;
        return ReminderConfigMonths;
    }

    public void setReminderConfigMonths(Long reminderConfigMonths) {
        ReminderConfigMonths = reminderConfigMonths;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ReminderDescription);
        boolean flags[] = new boolean[1];
        flags[0] = NotificationFlagSms;
        parcel.writeBooleanArray(flags);
        parcel.writeString(ServiceType);

        parcel.writeLong(ReminderDate);
        if(ReminderConfigMiles != null)
          parcel.writeDouble(ReminderConfigMiles);
        else
          parcel.writeDouble(0);
        parcel.writeLong(LastNotificationDate);
        parcel.writeString(SmsPhoneNumber);
        parcel.writeString(Email);
        parcel.writeString(ReminderName);
        parcel.writeString(ServiceName);
        parcel.writeString(ReminderID);
        parcel.writeDouble(LastOdometer);
        flags[0] = NotificationFlagEmail;
        parcel.writeBooleanArray(flags);
        if(ReminderConfigMonths != null)
          parcel.writeLong(ReminderConfigMonths);
        else
          parcel.writeLong(0);
    }

    public static final Creator<MaintenanceReminderData> CREATOR = new Parcelable.Creator<MaintenanceReminderData>() {

        @Override
        public MaintenanceReminderData createFromParcel(Parcel parcel) {
            return new MaintenanceReminderData(parcel);
        }

        @Override
        public MaintenanceReminderData[] newArray(int i) {
            return new MaintenanceReminderData[0];
        }
    };

    private MaintenanceReminderData(Parcel in) {

        ReminderDescription = in.readString();
        boolean flags[] = new boolean[1];
        in.readBooleanArray(flags);
        NotificationFlagSms = flags[0];
        ServiceType = in.readString();
        ReminderDate = in.readLong();
        ReminderConfigMiles = in.readDouble();
        LastNotificationDate = in.readLong();
        SmsPhoneNumber = in.readString();
        Email = in.readString();
        ReminderName = in.readString();
        ServiceName = in.readString();
        ReminderID = in.readString();
        LastOdometer = in.readDouble();
        in.readBooleanArray(flags);
        NotificationFlagEmail = flags[0];
        ReminderConfigMonths = in.readLong();
    }
    public void set(MaintenanceReminderData reminderData) {

        ReminderDescription = reminderData.ReminderDescription;
        NotificationFlagSms = reminderData.NotificationFlagSms;
        ServiceType = reminderData.ServiceType;
        ReminderDate = reminderData.ReminderDate;
        ReminderConfigMiles = reminderData.ReminderConfigMiles;
        LastNotificationDate = reminderData.LastNotificationDate;
        SmsPhoneNumber = reminderData.SmsPhoneNumber;
        Email = reminderData.Email;
        ReminderName = reminderData.ReminderName;
        ServiceName = reminderData.ServiceName;
        ReminderID = reminderData.ReminderID;
        LastOdometer = reminderData.LastOdometer;
        NotificationFlagEmail = reminderData.NotificationFlagEmail;
        ReminderConfigMonths = reminderData.ReminderConfigMonths;
    }
}
