package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z688522 on 2/4/15.
 */
public class ValetAlert implements Parcelable {

    private String AlertName;
    private int AlertId;
    private String Status;
    private String AlertTrigger;
    private CircleType Circle;

    public String getAlertName() {
        return AlertName;
    }

    public void setAlertName(String alertName) {
        AlertName = alertName;
    }

    public int getAlertId() {
        return AlertId;
    }

    public void setAlertId(int alertId) {
        AlertId = alertId;
    }

    public CircleType getCircle() {
        return Circle;
    }

    public void setCircle(CircleType circle) {
        Circle = circle;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAlertTrigger() {
        return AlertTrigger;
    }

    public void setAlertTrigger(String alertTrigger) {
        AlertTrigger = alertTrigger;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(AlertName);
        parcel.writeInt(AlertId);
        parcel.writeString(Status);
        parcel.writeString(AlertTrigger);
        parcel.writeParcelable(Circle, i);
    }

    public ValetAlert() {
        AlertName = "ValetAlert";
        AlertId = 0;
        Status = "InActive";

    }

    public ValetAlert(Parcel in) {
        AlertName = in.readString();
        AlertId =  in.readInt();
        Status = in.readString();
        AlertTrigger = in.readString();
        Circle = in.readParcelable(CircleType.class.getClassLoader());
    }

    public static Creator<ValetAlert> CREATOR = new Creator<ValetAlert>() {
        @Override
        public ValetAlert createFromParcel(Parcel parcel) {
            return new ValetAlert(parcel);
        }

        @Override
        public ValetAlert[] newArray(int i) {
            return new ValetAlert[i];
        }
    };
}
