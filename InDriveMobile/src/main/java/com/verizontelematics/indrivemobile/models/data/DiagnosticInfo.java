package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Priyanga on 3/23/2015.
 */
public class DiagnosticInfo extends BaseData implements Parcelable {

    private String Code;
    private String Description;

    public DiagnosticInfo() {
        super();
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // String Code;
        parcel.writeString(Code);
        // String Description;
        parcel.writeString(Description);
    }

    public static final Creator<DiagnosticInfo> CREATOR = new Parcelable.Creator<DiagnosticInfo>() {

        @Override
        public DiagnosticInfo createFromParcel(Parcel parcel) {
            return new DiagnosticInfo(parcel);
        }

        @Override
        public DiagnosticInfo[] newArray(int i) {
            return new DiagnosticInfo[0];
        }
    };

    private DiagnosticInfo(Parcel in) {
        // String Code;
        Code = in.readString();
        // String Description;
        Description = in.readString();

    }
}
