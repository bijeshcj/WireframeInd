package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.data.LocationAlert;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Priyanga on 8/26/2014.
 */
public class BoundaryAlertAdapter extends BaseAdapter {
    private static String TAG = BoundaryAlertAdapter.class.getSimpleName();
    private final Context mContext;
    private int textViewResourceId;
    private ArrayList<LocationAlert> itemList;

    public BoundaryAlertAdapter(Context context, int textViewResourceId, ArrayList<LocationAlert> itemList) {
        mContext = context;
        Collections.sort(itemList);
        this.itemList = itemList;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        try {
            ListHolder holder = null;
            if (row == null) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(textViewResourceId, parent, false);
                holder = new ListHolder();
                holder.boundaryDetailTV = (TextView) row.findViewById(R.id.boundaryDetailTV);
                row.setTag(holder);

            } else {
                holder = (ListHolder) row.getTag();
            }
            LocationAlert list_Obj = itemList.get(position);
            holder.boundaryDetailTV.setText(list_Obj.getAlertName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return row;
    }

//    public boolean updateView(View view) {
//        if (view == null) {
//            return false;
//        }
//        ListHolder holder = (ListHolder)view.getTag();
//        if (holder == null)
//            return false;
//        boolean switchOn = (holder.onStateIV.getVisibility() == View.VISIBLE);
//        switchOn = !switchOn;
//        holder.onStateIV.setVisibility(switchOn ? View.VISIBLE : View.GONE);
//        holder.offStateIV.setVisibility(switchOn ? View.GONE : View.VISIBLE);
//        return switchOn;
//    }


    // static Holder class to increase the performance of the list view
    static class ListHolder {

        TextView boundaryDetailTV;
    }

    public void updateData(ArrayList<LocationAlert> locationAlerts) {
        this.itemList = locationAlerts;
        notifyDataSetChanged();
    }
}
