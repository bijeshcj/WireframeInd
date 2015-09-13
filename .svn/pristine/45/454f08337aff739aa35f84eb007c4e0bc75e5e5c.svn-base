package com.verizontelematics.indrivemobile.models;

import java.util.Observable;

/**
 * Created by z688522 on 9/11/14.
 */
public class BaseModel extends Observable {
    Object mData;

    public Object getData() { return mData; }

    public void clear() {
        mData = null;
        // do parsing
        setChanged();
        notifyObservers(mData);
    }
    public void setData(Object obj) {
        mData = obj;
        setChanged();
        notifyObservers(mData);
    }

    public void setData(String dataAsString) {
        // do parsing
        setChanged();
        notifyObservers(mData);
    }
}
