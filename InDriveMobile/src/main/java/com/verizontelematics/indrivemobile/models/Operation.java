package com.verizontelematics.indrivemobile.models;

import java.util.Observable;

/**
 * Created by z688522 on 10/22/14.
 */
public abstract class Operation extends Observable {

    // Operation Ids
    public static enum OperationCode {
        GET_MAINTENANCE_REMINDERS,
        GET_MAINTENANCE_LOGS,
        ADD_MAINTENANCE_LOGS,
        DELETE_MAINTENANCE_LOG,
        UPDATE_MAINTENANCE_LOG,
        ADD_MAINTENANCE_REMINDER,
        DELETE_MAINTENANCE_REMINDER,
        UPDATE_MAINTENANCE_REMINDER,
        ADD_MAINTENANCE_REMINDERS,
        DELETE_MAINTENANCE_REMINDERS,
        UPDATE_MAINTENANCE_REMINDERS,
        GET_RECALLS,
        SET_RECALL_COMPLETED,
        GET_VEHICLE_HEALTH,
        GET_DRIVING_DATA,
        GET_ALERTS,
        UPDATE_SPEED_ALERTS,
        CREATE_LOCATION_ALERT,
        UPDATE_LOCATION_ALERT,
        UPDATE_DIAGNOSTIC_ALERTS,
        GET_ALERTS_HISTORY,
        GET_LOCATION_HISTORY,
        CREATE_VALET_ALERT,
        UPDATE_VALET_ALERT,
        DELETE_LOCATION_ALERT,
        FIND_USER, SEND_AUTH_TOKEN, GET_LOCATION, GET_CERT_ID, AUTHENTICATE, GET_USER_ACCT_VECH_DEV_INFO, GET_USER_VEHICLES, VALIDATE_REGISTRATION, FORGOT_PASSWORD, FORGOT_USERNAME, UPDATE_PASSWORD, GET_USER_PREFERENCE, REFRESH, REGISTER_PUSH, DEREGISTER_PUSH, UPDATE_USER_PREFERENCE,
        SEND_AUTH_TOKEN_NEW_DEV_REG
    }

    // Operation States
    public static final int ERROR = 0;
    public static final int CREATED = 1;
    private static final int SUBMITTED = 2;
    public static final int PENDING = 3;
    public static final int FINISHED = 4;
    public static final int CANCELLED = 5;

    int id;
    private int state;
    protected Object mData;
//    protected View mView = null;


    protected Operation() {
        id = -1;
        state = -1;
    }

    protected Operation(int oprId, int oprState) {
        id = oprId;
        state = oprState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        setChanged();
        notifyObservers(this);
    }


    public Object getData() {
        return mData;

    }

    public boolean isInProgress() {
        return (this.getState() == Operation.CREATED || this.getState() == Operation.PENDING || this.getState() == Operation.SUBMITTED);
    }

    public boolean isFinished() {
        return (this.getState() == Operation.FINISHED || this.getState() == Operation.ERROR);
    }

    public void setData(Object aData) {
        this.mData = aData;
        setChanged();
        notifyObservers(this);
    }

//    public View getView() {
//        return mView;
//    }
//
//    public void setView(View aView) {
//        mView = aView;
//    }

    abstract public String getInformation();

    abstract public void setInformation(String message);

    //Method to kill the polling timers
    public void clean() {
        setState(CANCELLED);
    }

    public void cancel() {
        setState(CANCELLED);
    }

    public void onDestroy() {

    }

}
