package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z688522 on 2/2/15.
 */
public class PositionType implements Parcelable{
    public double Latitude;
    public double Longitude;

    public PositionType(Parcel in) {
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

    public static Creator<PositionType> CREATOR = new Creator<PositionType>() {
        @Override
        public PositionType createFromParcel(Parcel parcel) {
            return new PositionType(parcel);
        }

        @Override
        public PositionType[] newArray(int size) {
            return new PositionType[size];
        }
    };
}
