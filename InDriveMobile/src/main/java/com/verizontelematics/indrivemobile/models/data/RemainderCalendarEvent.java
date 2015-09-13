package com.verizontelematics.indrivemobile.models.data;

/**
 * Created by bijesh on 11/13/2014.
 */
public class RemainderCalendarEvent {

    private String title;
    private long startDate;

    public RemainderCalendarEvent(String title,long startDate){
        this.title = title;

        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Calendar Event name: "+this.title+" start time: "+ startDate;
    }
}
