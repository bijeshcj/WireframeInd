package com.verizontelematics.indrivemobile.models;

import java.util.ArrayList;

/**
 * Created by z688522 on 9/12/14.
 */
public class AlertsModel extends BaseModel {


    public AlertsModel() {
        super();
        mData = new ArrayList<AlertModel>();
    }

    public void add(AlertModel alertModel) {
        ((ArrayList<AlertModel>)mData).add(alertModel);
        setChanged();
        notifyObservers();
    }

    public void remove(AlertModel alertModel) {
        ArrayList<AlertModel> list = (ArrayList<AlertModel>)mData;
        if (list.contains(alertModel)) {
            list.remove(alertModel);
            setChanged();
            notifyObservers();
        }
    }

    public void clear() {
        if (((ArrayList<AlertModel>)mData).isEmpty())
            return;
        ((ArrayList<AlertModel>)mData).clear();
        setChanged();
        notifyObservers();
    }

    public Boolean update(int selectedIndex, MaintenanceAlertModel maintenanceAlertModel) {
        ArrayList<AlertModel> list = (ArrayList<AlertModel>)mData;
        if (selectedIndex == -1 || list == null || selectedIndex >= list.size()) {
                return false;
        }
        list.set(selectedIndex, maintenanceAlertModel);
        setChanged();
        notifyObservers();
        return true;
    }

    public AlertModel get(int index) {
        ArrayList<AlertModel> list = (ArrayList<AlertModel>)mData;
        if (index == -1 || list == null || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }
}
