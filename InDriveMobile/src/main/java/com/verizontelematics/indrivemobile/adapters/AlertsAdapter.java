package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.widget.BaseAdapter;

import com.verizontelematics.indrivemobile.models.AlertModel;
import com.verizontelematics.indrivemobile.models.AlertsModel;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by z688522 on 9/12/14.
 */
public abstract class AlertsAdapter extends BaseAdapter implements Observer {
    protected ArrayList<AlertModel> mAlerts;
    protected Context mContext;

    public AlertsAdapter(Context ctx) {
        mContext = ctx;
    }

    @Override
    public int getCount() {
        if (mAlerts == null)
            return 0;
        return mAlerts.size();
    }

    @Override
    public Object getItem(int position) {
        if (mAlerts == null || position >= mAlerts.size())
            return null;
        return mAlerts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void update(Observable observable, Object data) {

        if (AlertsModel.class.isInstance(observable)) {
            mAlerts = (ArrayList<AlertModel>) ((AlertsModel) observable).getData();
            notifyDataSetChanged();
        }
    }
}
