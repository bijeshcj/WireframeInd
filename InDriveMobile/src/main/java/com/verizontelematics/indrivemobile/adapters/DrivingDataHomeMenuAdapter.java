package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by bijesh on 12/22/2014.
 */
public class DrivingDataHomeMenuAdapter extends BaseAdapter {

    private ArrayList<String> mOptions = new ArrayList<String>();
    private Context mContext;
    public DrivingDataHomeMenuAdapter(Context ctx, String[]  arr) {
        if (arr == null)
            return;
        mContext = ctx;
        Collections.addAll(mOptions, arr);
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.driving_data_home_menu_list_item, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.home_options_list_item_name);
        tv.setText(mOptions.get(position));
        return convertView;
    }
}

