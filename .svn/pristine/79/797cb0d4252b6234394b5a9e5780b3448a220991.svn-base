package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.IndriveHomeAdapterModel;

import java.util.ArrayList;

/**
 * Created by bijesh on 11/18/2014.
 */
public class IndriveHomeAdapter  extends BaseAdapter{

//    String[] Title, Detail;
    private int[] mImages;
    private Context mContext;
    private ArrayList<IndriveHomeAdapterModel> mListHomeModels;

    public IndriveHomeAdapter(Context context,ArrayList<IndriveHomeAdapterModel> models){
        this.mContext = context;
        mListHomeModels = models;
        mImages = new int[]{R.drawable.alert_tab_icon_normal,R.drawable.alert_tab_icon_pressed,
        R.drawable.alert_tab_icon_normal,R.drawable.alert_tab_icon_pressed,R.drawable.alert_tab_icon_normal};
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mListHomeModels.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row;
        row = inflater.inflate(R.layout.list_item_indrive_home, parent, false);
        TextView title, detail;
        ImageView i1;
        title = (TextView) row.findViewById(R.id.title);
        detail = (TextView) row.findViewById(R.id.detail);
        i1=(ImageView)row.findViewById(R.id.img);
        title.setText(mListHomeModels.get(position).getHeader());
        detail.setText(mListHomeModels.get(position).getDescription());
        i1.setImageResource(mImages[position]);

        return (row);
    }
}


