package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by z689649 on 10/3/14.
 */
public class MaintenanceLogData implements Parcelable {

    private String ServiceName;
    private String Cost;
    private long ServiceDate;
    private String Mileage;
    private String Phone;
//    private Address Address;
    private String ServiceType;
    private String PreferredServiceCenter;
    private String ServiceCenter;
    private String Description;
    private String ServiceID;

    public MaintenanceLogData() {
    }



    public MaintenanceLogData(String serviceType, String serviceName,
                              String description, double cost, double mileage, Calendar dateSelectedCalender, String serviceCenter) {
        ServiceType = serviceType;
        ServiceName = serviceName;
        Description = description;
        Cost = "" + cost;
        Mileage = "" + mileage;
        ServiceDate = dateSelectedCalender.getTimeInMillis();
        ServiceCenter = serviceCenter;
        // find out how to store this.
        // ServiceDate = ""+dateSelectedCalender.get(Calendar.MONTH)+"/"+dateSelectedCalender.get(Calendar.DAY_OF_MONTH)+"/"+dateSelectedCalender.get(Calendar.YEAR);
    }

    public MaintenanceLogData(String serviceType, String serviceName,
                              String description, double cost, String mileage, Calendar dateSelectedCalender, String serviceCenter) {
        ServiceType = serviceType;
        ServiceName = serviceName;
        Description = description;
        Cost = "" + cost;
        Mileage = mileage;
        ServiceDate = dateSelectedCalender.getTimeInMillis();
        ServiceCenter = serviceCenter;
        // find out how to store this.
        // ServiceDate = ""+dateSelectedCalender.get(Calendar.MONTH)+"/"+dateSelectedCalender.get(Calendar.DAY_OF_MONTH)+"/"+dateSelectedCalender.get(Calendar.YEAR);
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public long getServiceDate() {
        return ServiceDate;
    }

    public void setServiceDate(long serviceDate) {
        ServiceDate = serviceDate;
    }

    public String getMileage() {
        return Mileage;
    }

    public void setMileage(String mileage) {
        Mileage = mileage;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

//    public String getAddress() {
//        Gson gson = new GsonBuilder().create();
//        String lJson = gson.toJson(Address);
//        return lJson;
//    }

//    public void setAddress(String address) {
//        Gson gson = new GsonBuilder().create();
//        Address = gson.fromJson(address, Address.getClass());
//    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getPreferredServiceCenter() {
        return PreferredServiceCenter;
    }

    public void setPreferredServiceCenter(String preferredServiceCenter) {
        PreferredServiceCenter = preferredServiceCenter;
    }

    public String getServiceCenter() {
        return ServiceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        ServiceCenter = serviceCenter;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

//    public void setAddress(Address address) {
//        Address = address;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // Address Address;
//        parcel.writeParcelable(Address, i);
        // String ServiceName;
        parcel.writeString(ServiceName);
        // String Cost;
        parcel.writeString(Cost);
        // long ServiceDate;
        parcel.writeLong(ServiceDate);
        // String Mileage;
        parcel.writeString(Mileage);
        // String Phone;
        parcel.writeString(Phone);

        // String ServiceType;
        parcel.writeString(ServiceType);
        // String PreferredServiceCenter;
        parcel.writeString(PreferredServiceCenter);
        // String ServiceCenter;
        parcel.writeString(ServiceCenter);
        // String Description;
        parcel.writeString(Description);
        // String ServiceID;
        parcel.writeString(ServiceID);
    }

    public static final Creator<MaintenanceLogData> CREATOR = new Parcelable.Creator<MaintenanceLogData>() {

        @Override
        public MaintenanceLogData createFromParcel(Parcel parcel) {
            return new MaintenanceLogData(parcel);
        }

        @Override
        public MaintenanceLogData[] newArray(int i) {
            return new MaintenanceLogData[0];
        }
    };

    private MaintenanceLogData(Parcel in) {
        // Address Address;
//        Address = (Address) in.readParcelable(Address.getClass().getClassLoader());

        // String ServiceName;
        ServiceName = in.readString();
        // String Cost;
        Cost = in.readString();
        // long ServiceDate;
        ServiceDate = in.readLong();
        // String Mileage;
        Mileage = in.readString();
        // String Phone;
        Phone = in.readString();

        // String ServiceType;
        ServiceType = in.readString();
        // String PreferredServiceCenter;
        PreferredServiceCenter = in.readString();
        // String ServiceCenter;
        ServiceCenter = in.readString();
        // String Description;
        Description = in.readString();
        // String ServiceID;
        ServiceID = in.readString();
    }

    public void set(MaintenanceLogData logData) {

        ServiceName = logData.ServiceName;
        Cost = logData.Cost;
        ServiceDate = logData.ServiceDate;
        Mileage = logData.Mileage;
        Phone = logData.Phone;
//        Address = logData.Address;
        ServiceType = logData.ServiceType;
        PreferredServiceCenter = logData.PreferredServiceCenter;
        ServiceCenter = logData.ServiceCenter;
        Description = logData.Description;
        ServiceID = logData.ServiceID;

    }
}
