package com.verizontelematics.indrivemobile.utils.ui;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by z688522 on 12/10/14.
 */
public class CalenderBuilder {
    private static final String TAG = "CalenderBuilder";
    private static final long DAY = 1000 * 60 * 60 * 24;
    private long mStartTime;
    private long mEndTime;
    private int mOption = Option.THIS_WEEK.ordinal();
    private long mWeekCount = -1;
    private long mFirstWeekStartTime;
    private long mLastWeekEndTime;



    private int mWeekIndex = 0;
    private String mDateLabelFormat;

    public static enum  Option {
        THIS_WEEK,
        LAST_WEEK,
        THIS_MONTH,
        LAST_MONTH,
        CUSTOM
    }

    public CalenderBuilder(int option) {
        mOption = option;
    }

    public CalenderBuilder(long startTime, long endTime) {
        mOption = Option.CUSTOM.ordinal();
    }

    public CalenderBuilder(int option, String dateLabelFormat) {
        mOption = option;
        mDateLabelFormat = dateLabelFormat;
    }

    public void build() {
        if (mOption == Option.THIS_WEEK.ordinal()
                || mOption == Option.LAST_WEEK.ordinal()) {
            mWeekCount = 1;
            mFirstWeekStartTime = getStartTimeOfWeek(Calendar.getInstance().getTimeInMillis());
            if (mOption == Option.THIS_WEEK.ordinal()) {
                mLastWeekEndTime = getEndTimeTimeOfWeek(Calendar.getInstance().getTimeInMillis());
            }
            else {
                mLastWeekEndTime = mFirstWeekStartTime - DAY;
                mFirstWeekStartTime = mLastWeekEndTime - 6 * DAY;
            }
        }
        else if (mOption == Option.THIS_MONTH.ordinal()
                || mOption == Option.LAST_MONTH.ordinal()) {
            mWeekCount = 4;
            mFirstWeekStartTime = getStartTimeOfWeek(getStartTimeOfMonth(Calendar.getInstance().getTimeInMillis()));
            if (mOption == Option.THIS_MONTH.ordinal()) {
                mLastWeekEndTime = getEndTimeTimeOfWeek(getEndTimeOfMonth(Calendar.getInstance().getTimeInMillis()));
            }
            else if (mOption == Option.LAST_MONTH.ordinal()) {
                long lastDayOfLastMonth = getStartTimeOfMonth(Calendar.getInstance().getTimeInMillis()) - (1000 * 60 * 60 * 24);
                mLastWeekEndTime = getEndTimeTimeOfWeek(lastDayOfLastMonth);
                mFirstWeekStartTime = getStartTimeOfWeek(getStartTimeOfMonth(lastDayOfLastMonth));
            }
        }
        else if (mOption == Option.CUSTOM.ordinal()) {
            mFirstWeekStartTime = getStartTimeOfWeek(mStartTime);
            mLastWeekEndTime = getEndTimeTimeOfWeek(mEndTime);
        }

        mWeekCount = (mLastWeekEndTime - mFirstWeekStartTime) / (DAY * 7) + 1;

        mWeekIndex = 0;
    }

    public long getStartTimeOfMonth(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
       // calendar.set(calendar.get(calendar.YEAR),calendar.get(calendar.MONTH),calendar.getMinimum(Calendar.DAY_OF_MONTH),0,0,0);


        return calendar.getTimeInMillis();
    }

    public long getEndTimeOfMonth(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis();
    }

    public long getWeekCount() {
        if (mWeekCount < 0) {
            build();
        }
        return mWeekCount;
    }

    public Week getWeek(int index) {
        if (mWeekCount < 0) {
            build();
        }
        if (index > mWeekCount) {
            Log.d(TAG, "out index : "+index+" and week count is "+mWeekCount);
            return null;
        }

        mWeekIndex = index;
        long startTimeOfWeek = mFirstWeekStartTime + mWeekIndex * 7 * DAY;
        long endTimeOfWeek = startTimeOfWeek + 6 * DAY;
        if (mDateLabelFormat != null && !mDateLabelFormat.isEmpty())
            return (new Week(startTimeOfWeek, endTimeOfWeek, mDateLabelFormat));
        else
            return (new Week(startTimeOfWeek, endTimeOfWeek));
    }

    public Week getWeek() {
        if (mWeekCount < 0) {
            build();
        }
        if (mWeekIndex > mWeekCount) {
            Log.d(TAG, "out index : "+mWeekIndex+" and week count is "+mWeekCount);
            return null;
        }
        long startTimeOfWeek = mFirstWeekStartTime + mWeekIndex * 7 * DAY;
        long endTimeOfWeek = startTimeOfWeek + 6 * DAY;
        if (mDateLabelFormat != null && !mDateLabelFormat.isEmpty())
            return (new Week(startTimeOfWeek, endTimeOfWeek, mDateLabelFormat));
        else
            return (new Week(startTimeOfWeek, endTimeOfWeek));
    }

    public void setOption(int option) {
        mOption = option;
        build();
    }

    public void setOption(int option, long startTime, long endTime) {
        mOption = option;
        mStartTime = startTime;
        mEndTime = endTime;
        build();
    }

    public void setDateLabelFormat(String dateLabelFormat) {
        mDateLabelFormat = dateLabelFormat;
        build();
    }

    public int getOption() {
        return mOption;
    }

    public int getCurrentWeekIndex() {
        return mWeekIndex;
    }

    public long getStartTimeOfWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTimeInMillis();
    }

    public long getEndTimeTimeOfWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        return calendar.getTimeInMillis();
    }

    public Week getPreviousWeek() {
        if (mWeekCount < 0) {
            build();
        }
        if (mWeekIndex > 0) {
            mWeekIndex--;
        }
        return getWeek();
    }

    public Week getNextWeek() {
        if (mWeekCount < 0) {
            build();
        }
        mWeekIndex++;
        if (mWeekIndex >= mWeekCount) {
            mWeekIndex--;
        }
        return getWeek();
    }

    public int getWeekIndex() {
        if (mWeekCount < 0) {
            build();
        }
        return mWeekIndex;
    }

    public void setWeekIndex(int mWeekIndex) {
        this.mWeekIndex = mWeekIndex;
    }

    public long getMonthBeginDate() {
        if (mOption == Option.THIS_MONTH.ordinal()) {
            return (getStartTimeOfMonth(Calendar.getInstance().getTimeInMillis()));
        }
        else if (mOption == Option.LAST_MONTH.ordinal()) {
            long lastDayOfLastMonth = getStartTimeOfMonth(Calendar.getInstance().getTimeInMillis()) - DAY;
            return getStartTimeOfMonth(lastDayOfLastMonth);
        }
        Log.d(TAG, "Invalid option for getMonthBeginDate "+mOption);
        return -1;
    }

    @Deprecated
    public long getMonthEndDate() {
        if (mOption == Option.THIS_MONTH.ordinal()) {
            return (getEndTimeOfMonth(Calendar.getInstance().getTimeInMillis()));
        }
        else if (mOption == Option.LAST_MONTH.ordinal()) {

            long lastDayOfLastMonth = getStartTimeOfMonth(Calendar.getInstance().getTimeInMillis()) - DAY;
//            return (getEndTimeOfMonth(lastDayOfLastMonth));
            return lastDayOfLastMonth;
        }
        Log.d(TAG, "Invalid option for getMonthBeginDate "+mOption);
        return -1;
    }

    public long getMonthEndDate(long startDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startDate));

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();
        return lastDayOfMonth.getTime();
    }

}
