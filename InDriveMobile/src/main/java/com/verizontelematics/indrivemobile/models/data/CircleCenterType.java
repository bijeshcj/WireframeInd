package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by z688522 on 2/2/15.
 */
public class CircleCenterType implements Parcelable {

    private double Latitude;
    private double Longitude;


    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public CircleCenterType() {
        Latitude = 0;
        Longitude = 0;
    }

    public CircleCenterType(double latitude, double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public CircleCenterType(LatLng latLng) {
        Latitude = latLng.latitude;
        Longitude = latLng.longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(Latitude, Longitude);
    }

    public void setLatLng(LatLng latLng) {
        Latitude = latLng.latitude;
        Longitude = latLng.longitude;
    }

    public CircleCenterType(Parcel in) {

        Latitude = in.readDouble();
        Longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(Latitude);
        parcel.writeDouble(Longitude);
    }

    public static Creator<CircleCenterType> CREATOR = new Creator<CircleCenterType>() {
        @Override
        public CircleCenterType createFromParcel(Parcel parcel) {
            return new CircleCenterType(parcel);
        }

        @Override
        public CircleCenterType[] newArray(int i) {
            return new CircleCenterType[i];
        }
    };
}
