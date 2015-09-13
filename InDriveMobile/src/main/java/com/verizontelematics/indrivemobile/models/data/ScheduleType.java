package com.verizontelematics.indrivemobile.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by z688522 on 2/2/15.
 */
public class ScheduleType implements Parcelable {
    private String WeekDays;
    private int MinutesFromMidnight;
    private int Duration;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(WeekDays);
        parcel.writeInt(MinutesFromMidnight);
        parcel.writeInt(Duration);
    }

    public ScheduleType() {
    }

    public ScheduleType(String weekDays, int minutesFromMidNight, int duration) {
        WeekDays = weekDays;
        MinutesFromMidnight = minutesFromMidNight;
        Duration = duration;
    }

    public String getWeekDays() {

        return WeekDays;

    }

    public void setWeekDays(String weekDays) {
        WeekDays = weekDays;
    }

    public int getMinutesFromMidNight() {
        return MinutesFromMidnight;
    }

    public void setMinutesFromMidNight(int minutesFromMidNight) {
        MinutesFromMidnight = minutesFromMidNight;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public ScheduleType(Parcel in) {
        WeekDays = in.readString();
        MinutesFromMidnight = in.readInt();
        Duration = in.readInt();
    }

    public static Creator<ScheduleType> CREATOR = new Creator<ScheduleType>() {

        @Override
        public ScheduleType createFromParcel(Parcel parcel) {
            return new ScheduleType(parcel);
        }

        @Override
        public ScheduleType[] newArray(int i) {
            return new ScheduleType[i];
        }
    };
}
