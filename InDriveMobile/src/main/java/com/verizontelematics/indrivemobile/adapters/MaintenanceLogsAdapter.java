package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.data.MaintenanceLogData;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;

import java.util.ArrayList;

/**
 * Created by bijesh on 10/6/2014.
 */
public class MaintenanceLogsAdapter extends ArrayAdapter<MaintenanceLogData> {
    private static String TAG = AlertHistoryAdapter.class.getSimpleName();
    private final Context mContext;
    private int textViewResourceId;
    //    private ArrayList<AlertHistory> itemList;
    private ArrayList<MaintenanceLogData> lstMaintenance = new ArrayList<MaintenanceLogData>();

//    public AlertHistoryAdapter(Context context, int textViewResourceId, ArrayList<AlertHistory> itemList) {
//
//        super(context, textViewResourceId, itemList);
//        this.itemList = itemList;
//        this.textViewResourceId = textViewResourceId;
//    }

    public MaintenanceLogsAdapter(Context context, int textViewResourceId, ArrayList<MaintenanceLogData> lstMaintenance) {

        super(context, textViewResourceId, lstMaintenance);
        this.lstMaintenance = lstMaintenance;
        this.textViewResourceId = textViewResourceId;
        mContext = context;
    }

    @Override
    public int getCount() {
        if(lstMaintenance != null)
         return this.lstMaintenance.size();
        else
            return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        try {
            ListHolder holder = null;
            if (row == null) {

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(textViewResourceId, parent, false);
                holder = new ListHolder();
                holder._alertDetailTV = (TextView) row.findViewById(R.id.alertDetailTV);
                holder._alertTimeTV = (TextView) row.findViewById(R.id.alertTimeTV);
                row.setTag(holder);

            } else {
                holder = (ListHolder) row.getTag();
            }
            MaintenanceLogData list_Obj = lstMaintenance.get(position);
            holder._alertDetailTV.setText(list_Obj.getServiceName());
            holder._alertTimeTV.setText(DateDataFormat.convertMillisAsDateString(mContext, list_Obj.getServiceDate(), true));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return row;
    }


    // static Holder class to increase the performance of the list view
    static class ListHolder {
        TextView _alertDetailTV = null;
        TextView _alertTimeTV = null;
    }
}
