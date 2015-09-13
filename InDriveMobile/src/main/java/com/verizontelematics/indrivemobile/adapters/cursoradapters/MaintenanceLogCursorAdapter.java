package com.verizontelematics.indrivemobile.adapters.cursoradapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceLogTable;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;

/**
 * Created by bijesh on 10/7/2014.
 */
public class MaintenanceLogCursorAdapter extends CursorAdapter {


    private Context mContext;

    public MaintenanceLogCursorAdapter(Context context, Cursor c, boolean autoReQuery) {
        super(context, c, autoReQuery);
        mContext = context;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.maintenance_log_list_item,null);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView alertDetailTV = (TextView) view.findViewById(R.id.alertDetailTV);
        TextView alertTimeTV = (TextView) view.findViewById(R.id.alertTimeTV);
        String str = cursor.getString(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_ALERT_NAME));
        alertDetailTV.setText(str);

        alertTimeTV.setText(DateDataFormat.convertMillisAsDateString(mContext, cursor.getLong(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_DATE)), true));
        view.setTag(cursor.getInt(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MAINTENANCE_Id)));
      //  view.setTag(new Integer(cursor.getInt(cursor.getColumnIndex(MaintenanceLogTable.COLUMN_MAINTENANCE_Id))));
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }


}
