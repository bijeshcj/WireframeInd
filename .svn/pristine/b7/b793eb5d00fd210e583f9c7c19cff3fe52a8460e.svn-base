package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.AlertHistory;

import java.util.ArrayList;

/**
 * Created by Priyanga on 8/26/2014.
 */
public class AlertDetailHistoryAdapter extends ArrayAdapter<AlertHistory> {
    private static String TAG = AlertDetailHistoryAdapter.class.getSimpleName();
    private int textViewResourceId;
    private ArrayList<AlertHistory> itemList;

    public AlertDetailHistoryAdapter(Context context, int textViewResourceId, ArrayList<AlertHistory> itemList) {

        super(context, textViewResourceId, itemList);
        this.itemList = itemList;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public int getCount() {
        return this.itemList.size();
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
//                holder._alertDetailMsgTV = (TextView) row.findViewById(R.id.alertDetailMessageTV);
                row.setTag(holder);

            } else {
                holder = (ListHolder) row.getTag();
            }
            AlertHistory list_Obj = itemList.get(position);
            holder._alertDetailTV.setText(list_Obj.getAlertDetail());
            holder._alertTimeTV.setText(list_Obj.getAlertTime());
//            holder._alertDetailMsgTV.setText(list_Obj.getAlertMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return row;
    }


    // static Holder class to increase the performance of the list view
    static class ListHolder {
        TextView _alertDetailTV = null;
        TextView _alertTimeTV = null;
//        TextView _alertDetailMsgTV=null;
    }
}
