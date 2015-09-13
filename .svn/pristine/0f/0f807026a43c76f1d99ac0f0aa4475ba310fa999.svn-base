package com.verizontelematics.indrivemobile.adapters.cursoradapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.database.tables.MaintenanceReminderTable;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;


/**
 * Created by bijesh on 10/8/2014.
 */
public class MaintenanceReminderCursorAdapter extends CursorAdapter {


    private final Context mContext;

    public MaintenanceReminderCursorAdapter(Context context, Cursor c, boolean autoReQuery) {
        super(context, c, autoReQuery);
        mContext = context;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.maintence_alert_list_item,null);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView txtVwServiceType = (TextView) view.findViewById(R.id.alert_name);
        TextView txtDateSelected =(TextView)view.findViewById(R.id.alert_date);

        String serviceType = cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_SERVICE_TYPE));
        String reminderName = cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_NAME));
        String serviceName = cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_SERVICE_NAME));
        // display service name by default
        txtVwServiceType.setText(serviceName);

        // display reminder name when  serviceType is custom
        if (serviceType.equalsIgnoreCase("Custom")) {
            txtVwServiceType.setText(reminderName);
        }

        // get reminder data
        long dateInMilli = cursor.getLong(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_DATE));
        // get millage interval
        String dateOrMileage =  (dateInMilli > 0) ? DateDataFormat.convertMillisAsDateString(mContext, dateInMilli, true) : cursor.getString(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_REMINDER_CONFIG_MILES))+" m";

        txtDateSelected.setText(dateOrMileage);

        view.setTag(cursor.getInt(cursor.getColumnIndex(MaintenanceReminderTable.COLUMN_MAINTENANCE_Id)));
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }

}
