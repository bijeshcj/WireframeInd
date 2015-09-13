package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z688522 on 2/2/15.
 */
public class CircleType implements Parcelable {
    public double getRadius() {
        return Radius;
    }

    public void setRadius(double radius) {
        Radius = radius;
    }

    public CircleCenterType getCircleCenter() {
        return CircleCenter;
    }

    public void setCircleCenter(CircleCenterType circleCenter) {
        CircleCenter = circleCenter;
    }


    private double Radius;
    private CircleCenterType CircleCenter;

    public CircleType(Parcel in) {
        Radius = in.readDouble();
        CircleCenter = in.readParcelable(CircleType.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CircleType(double radius, CircleCenterType circleCenter) {
        Radius = radius;
        CircleCenter = circleCenter;
    }

    @Override

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(Radius);
        parcel.writeParcelable(CircleCenter, 0);
    }

    public static Creator<CircleType> CREATOR = new Creator<CircleType>() {
        @Override
        public CircleType createFromParcel(Parcel parcel) {
            return new CircleType(parcel);
        }

        @Override
        public CircleType[] newArray(int i) {
            return new CircleType[i];
        }
    };
}
