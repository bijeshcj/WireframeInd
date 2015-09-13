package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.database.tables.RecallTable;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;

/**
 * Created by z688522 on 10/7/14.
 */
public class RecallAlertsAdapter extends CursorAdapter {
    private Context mContext;

    public RecallAlertsAdapter(Context context, Cursor c, boolean autoReQuery) {
        super(context, c, autoReQuery);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.recall_alert_list_item,null);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtAlertName = (TextView) view.findViewById(R.id.txt_recall_alert_title);
        TextView txtDateTime = (TextView) view.findViewById(R.id.txt_recall_alert_date);
        String str = cursor.getString(cursor.getColumnIndex(RecallTable.COLUMN_ComponentName));
        txtAlertName.setText(str);
        txtDateTime.setText(DateDataFormat.convertMillisAsDateString(mContext, cursor.getLong(cursor.getColumnIndex(RecallTable.COLUMN_DateIssued)), true));
        view.setTag(cursor.getInt(cursor.getColumnIndex(RecallTable.COLUMN_RECALL_LOCAL_REC_ID)));

     //   view.setTag(new Integer(cursor.getInt(cursor.getColumnIndex(RecallTable.COLUMN_RECALL_LOCAL_REC_ID))));
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }
}
