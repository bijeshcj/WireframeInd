package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by z688522 on 2/2/15.
 */
public class PolygonType implements Parcelable {
    public ArrayList<Vertex> getVertices() {
        return Vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        Vertices = vertices;
    }

    private ArrayList<Vertex> Vertices = new ArrayList<Vertex>();

    public PolygonType(Parcel in) {
        in.readTypedList(Vertices, Vertex.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(Vertices);
    }

    public static Creator<PolygonType> CREATOR = new Creator<PolygonType>() {
        @Override
        public PolygonType createFromParcel(Parcel parcel) {
            return new PolygonType(parcel);
        }

        @Override
        public PolygonType[] newArray(int size) {
            return new PolygonType[size];
        }
    };
}
