package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.AlertSettings;

import java.util.ArrayList;

/**
 * Created by z684985 on 9/23/14.
 */
public class AlertSettingsAdapter extends ArrayAdapter<AlertSettings> {
    private static String TAG = AlertSettingsAdapter.class.getSimpleName();
    private int textViewResourceId;
    private ArrayList<AlertSettings> itemList;
    private String strOn = "On";
    private String strOff = "Off";


    public AlertSettingsAdapter(Context context, int textViewResourceId, ArrayList<AlertSettings> itemList) {

        super(context, textViewResourceId, itemList);
        strOn = context.getResources().getString(R.string.on);
        strOff = context.getResources().getString(R.string.off);

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
                holder._alertStatusIV = (ImageView) row.findViewById(R.id.onOffIV);
                holder._alertStatusTV = (TextView) row.findViewById(R.id.onOffTV);
                row.setTag(holder);

            } else {
                holder = (ListHolder) row.getTag();
            }
            AlertSettings list_Obj = itemList.get(position);
            holder._alertDetailTV.setText(list_Obj.getAlertDetail());
            holder._alertStatusTV.setText(list_Obj.isAlertToggle() ? strOn : strOff);
            holder._alertStatusIV.setImageResource(list_Obj.isAlertToggle() ? R.drawable.green_circle : R.drawable.red_circle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return row;
    }


    // static Holder class to increase the performance of the list view
    static class ListHolder {
        TextView _alertDetailTV = null;
        ImageView _alertStatusIV =  null;
        TextView _alertStatusTV = null;
    }
}
