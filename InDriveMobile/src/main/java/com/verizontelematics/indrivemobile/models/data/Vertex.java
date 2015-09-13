package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by z688522 on 2/2/15.
 */
public class Vertex implements Parcelable {
    public PositionType getPosition() {
        return Position;
    }

    public void setPosition(PositionType position) {
        Position = position;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public LatLng getLatLng() {
        if (getPosition() == null)
            return null;
        return new LatLng(getPosition().Latitude, getPosition().Longitude);
    }

    private int Order;
    private PositionType Position;

    public Vertex(Parcel in) {
        Order = in.readInt();
        Position = in.readParcelable(PositionType.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Order);
        parcel.writeParcelable(Position, i);
    }

    public static Creator<Vertex> CREATOR = new Creator<Vertex>() {
        @Override
        public Vertex createFromParcel(Parcel parcel) {
            return new Vertex(parcel);
        }

        @Override
        public Vertex[] newArray(int size) {
            return new Vertex[size];
        }
    };
}
