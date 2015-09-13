package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z688522 on 2/2/15.
 */
public class ShapeType implements Parcelable {
    public CircleType getCircle() {
        return Circle;
    }

    public void setCircle(CircleType circle) {
        Circle = circle;
    }

    public PolygonType getPolygon() {
        return Polygon;
    }

    public void setPolygon(PolygonType polygon) {
        Polygon = polygon;
    }

    private CircleType Circle;

    private PolygonType Polygon;

    public ShapeType(CircleType circle, PolygonType polygon) {
        Circle = circle;
        Polygon = polygon;
    }

    public ShapeType(Parcel in) {
        if (in.readInt() == 1) {
            Circle = in.readParcelable(CircleType.class.getClassLoader());
        }
        else
            Polygon = in.readParcelable(PolygonType.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (Circle != null) {
            parcel.writeInt(1);
            parcel.writeParcelable(Circle, i);
            return;
        }
        if (Polygon != null) {
            parcel.writeInt(2);
            parcel.writeParcelable(Polygon, i);
        }
    }

    public static Creator<ShapeType> CREATOR = new Creator<ShapeType>() {
        @Override
        public ShapeType createFromParcel(Parcel parcel) {
            return new ShapeType(parcel);
        }

        @Override
        public ShapeType[] newArray(int i) {
            return new ShapeType[i];
        }
    };
}
