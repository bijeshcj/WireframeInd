package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z689649 on 10/29/14.
 */
public class Address implements  Parcelable{

    private String State;
    private String ZipCode;
    private String Address2;
    private String Country;
    private String Address1;
    private String City;

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // String State;
        parcel.writeString(State);
        // String ZipCode;
        parcel.writeString(ZipCode);
        // String Address2;
        parcel.writeString(Address2);
        // String Country;
        parcel.writeString(Country);
        // String Address1;
        parcel.writeString(Address1);
        // String City;
        parcel.writeString(City);
    }

    public static final Creator<Address> CREATOR = new Parcelable.Creator<Address>() {

        @Override
        public Address createFromParcel(Parcel parcel) {
            return new Address(parcel);
        }

        @Override
        public Address[] newArray(int i) {
            return new Address[0];
        }
    };

    private Address(Parcel in) {
        // String State;
        State = in.readString();
        // String ZipCode;
        ZipCode = in.readString();
        // String Address2;
        Address2 = in.readString();
        // String Country;
        Country = in.readString();
        // String Address1;
        Address1 = in.readString();
        // String City;
        City = in.readString();
    }
}
