package com.verizontelematics.indrivemobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.models.data.AlertData;

/**
 * Created by z688522 on 9/12/14.
 */
public class MaintenanceAlertsAdapter extends AlertsAdapter {

    public MaintenanceAlertsAdapter(Context ctx) {
        super(ctx);
        //AppController.instance().getMaintenanceAlertsModel().addObserver(this);
        // mAlerts = (ArrayList<AlertModel>) (AppController.instance().getMaintenanceAlertsModel()).getData();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // boundary condition
        if (mAlerts.size() <= position) {
            return convertView;
        }
        // create MaintenanceAlertListItem
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.maintence_alert_list_item, parent, false);
        }

        // AlertName
        TextView txtAlertName = (TextView) convertView.findViewById(R.id.alert_name);
        final AlertData alertData = (AlertData)mAlerts.get(position).getData();
        txtAlertName.setText(alertData.getType());
        // Check State
       /* ImageButton btnAlertActiveState = (ImageButton) convertView.findViewById(R.id.alert_active_state);
        btnAlertActiveState.setSelected(mAlerts.get(position).isActive());
        btnAlertActiveState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton btn = (ImageButton) v;
                btn.setSelected(!btn.isSelected());
                mAlerts.get(position).setActive(btn.isSelected());
            }
        }); */
        return convertView;
    }
}
