package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z688522 on 11/10/14.
 */
public class RecallData implements Parcelable {
    private String RecallID;
    private String ComponentNumber;
    private String ComponentName;
    private String CorrectiveAction;
    private String DefectDescription;
    private String Manufacturer;
    private long ManufacturingBeginDate;
    private long ManufacturingEndDate;
    private String PotentialAffectedUnits;
    private String Summary;
    private long DateIssued;
    private long DateCompleted;

    public String getRecallID() {
        return RecallID;
    }

    public void setRecallID(String recallID) {
        RecallID = recallID;
    }

    public String getComponentName() {
        return ComponentName;
    }

    public void setComponentName(String componentName) {
        ComponentName = componentName;
    }

    public String getCorrectiveAction() {
        return CorrectiveAction;
    }

    public void setCorrectiveAction(String correctiveAction) {
        CorrectiveAction = correctiveAction;
    }

    public String getDefectDescription() {
        return DefectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        DefectDescription = defectDescription;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public long getManufacturingBeginDate() {
        return ManufacturingBeginDate;
    }

    public void setManufacturingBeginDate(long  manufacturingBeginDate) {
        ManufacturingBeginDate = manufacturingBeginDate;
    }

    public long getManufacturingEndDate() {
        return ManufacturingEndDate;
    }

    public void setManufacturingEndDate(long manufacturingEndDate) {
        ManufacturingEndDate = manufacturingEndDate;
    }

    public String getPotentialAffectedUnits() {
        return PotentialAffectedUnits;
    }

    public void setPotentialAffectedUnits(String potentialAffectedUnits) {
        PotentialAffectedUnits = potentialAffectedUnits;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public long getDateIssued() {
        return DateIssued;
    }

    public void setDateIssued(long  dateIssued) {
        DateIssued = dateIssued;
    }

    public long getDateCompleted() {
        return DateCompleted;
    }

    public void setDateCompleted(long dateCompleted) {
        DateCompleted = dateCompleted;
    }

    public String getComponentNumber() {
        return ComponentNumber;
    }

    public void setComponentNumber(String componentNumber) {
        ComponentNumber = componentNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // String RecallID;
        parcel.writeString(RecallID);
        // String ComponentNumber;
        parcel.writeString(ComponentNumber);
        // String ComponentName;
        parcel.writeString(ComponentName);
        // String CorrectiveAction;
        parcel.writeString(CorrectiveAction);
        // String DefectDescription;
        parcel.writeString(DefectDescription);
        // String Manufacturer;
        parcel.writeString(Manufacturer);
        // long ManufacturingBeginDate;
        parcel.writeLong(ManufacturingBeginDate);
        // long ManufacturingEndDate;
        parcel.writeLong(ManufacturingEndDate);
        // String PotentialAffectedUnits;
        parcel.writeString(PotentialAffectedUnits);
        // String Summary;
        parcel.writeString(Summary);
        // long DateIssued;
        parcel.writeLong(DateIssued);
        // long DateCompleted;
        parcel.writeLong(DateCompleted);
    }

    public static final Creator<RecallData> CREATOR = new Parcelable.Creator<RecallData>() {

        @Override
        public RecallData createFromParcel(Parcel parcel) {
            return new RecallData(parcel);
        }

        @Override
        public RecallData[] newArray(int i) {
            return new RecallData[0];
        }
    };

    private RecallData(Parcel in) {
        // String RecallID;
        RecallID = in.readString();
        // String ComponentNumber;
        ComponentNumber = in.readString();
        // String ComponentName;
        ComponentName = in.readString();
        // String CorrectiveAction;
        CorrectiveAction = in.readString();
        // String DefectDescription;
        DefectDescription = in.readString();
        // String Manufacturer;
        Manufacturer = in.readString();
        // long ManufacturingBeginDate;
        ManufacturingBeginDate = in.readLong();
        // long ManufacturingEndDate;
        ManufacturingEndDate = in.readLong();
        // String PotentialAffectedUnits;
        PotentialAffectedUnits = in.readString();
        // String Summary;
        Summary = in.readString();
        // long DateIssued;
        DateIssued = in.readLong();
        // long DateCompleted;
        DateCompleted = in.readLong();
    }

    public RecallData() {

    }
}
