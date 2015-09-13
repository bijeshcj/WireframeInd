package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.AlertHistory;

import java.util.ArrayList;

/**
 * Created by Z681639 on 11/12/2014.
 */
public class RecallInfoAdapter extends ArrayAdapter<AlertHistory> {
    private static String TAG = AlertHistoryAdapter.class.getSimpleName();
    private int textViewResourceId;
    private ArrayList<AlertHistory> itemList;

    public RecallInfoAdapter(Context context, int textViewResourceId, ArrayList<AlertHistory> itemList) {

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
                holder._recallDetailTV = (TextView) row.findViewById(R.id.recallDetailTV);
               //holder._recallDescTV = (TextView) row.findViewById(R.id.recallDetailDescTV);
                row.setTag(holder);

            } else {
                holder = (ListHolder) row.getTag();
            }
            AlertHistory list_Obj = itemList.get(position);
            holder._recallDetailTV.setText(list_Obj.getAlertDetail());
            holder._recallDetailTV.setText(Html.fromHtml(list_Obj.getAlertDetail()));

            //holder._recallDescTV.setText(list_Obj.getAlertTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return row;
    }


    // static Holder class to increase the performance of the list view
    static class ListHolder {
        TextView _recallDetailTV = null;
        //TextView _recallDescTV = null;

    }
}