package com.verizontelematics.indrivemobile.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.controllers.DiagnosticController;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.models.data.DiagnosticInfo;
import com.verizontelematics.indrivemobile.models.data.HealthStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z688522 on 2/19/15.
 */
public class DTCListAdapter extends BaseAdapter {

    private final Activity mContext;

    List<HealthStatus> healthStatusList;


    public DTCListAdapter(Activity ctx) {
        mContext = ctx;

//        StorageTransaction dBTransaction = new StorageTransaction(mContext);
//        healthStatusList = dBTransaction.getHealthStatusRecords();
        List<HealthStatus> aHealthStatusList = (List<HealthStatus>) DiagnosticController.instance().getVehicleHealthModel().getData();
        healthStatusList = new ArrayList<HealthStatus>();
        for (HealthStatus healthStatus : aHealthStatusList) {
            if (!healthStatus.getServiceAreaStatus().equalsIgnoreCase("NoIssuesDetected")) {
                healthStatusList.add(healthStatus);
            }
        }
        Log.v("list size is ",""+healthStatusList.size());

    }
    @Override
    public int getCount() {
        return healthStatusList.size();
    }

    @Override
    public Object getItem(int i) {
        return healthStatusList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout rl = (RelativeLayout) view;

        if (rl == null) {
            LayoutInflater li = mContext.getLayoutInflater();
            rl = (RelativeLayout) li.inflate(R.layout.dtc_list_item, null);
        }
        populateView(i, rl);
        return rl;
    }

    private void populateView(int pos, RelativeLayout rl) {
        // Service Area
        HealthStatus healthStatusObj = healthStatusList.get(pos);
        TextView tvServiceArea = (TextView) rl.findViewById(R.id.lbl_service_area);
        tvServiceArea.setText(healthStatusObj.getServiceArea());

        String serviceStatus = healthStatusObj.getServiceAreaStatus();
        TextView serviceStatusTV = (TextView) rl.findViewById(R.id.lbl_service_status);
        serviceStatusTV.setText(getServiceText(serviceStatus));

        ImageView ivDTCStatus = (ImageView) rl.findViewById(R.id.dtc_status);

        if (serviceStatus.equalsIgnoreCase("ServiceRequired")) {
            ivDTCStatus.setImageResource(R.drawable.warning_red);
        }
        else if (serviceStatus.equalsIgnoreCase("MonitorClosely")) {
            ivDTCStatus.setImageResource(R.drawable.warning_yellow);
        }

        // Dtc List description
        ArrayList<DiagnosticInfo> dtcList  = healthStatusObj.getDiagnosticInfo();
        if(dtcList!=null) {
            LinearLayout ll = (LinearLayout) rl.findViewById(R.id.dtc_desc_list);
            for (int i = 0; i < dtcList.size(); i++) {
                RelativeLayout dtcDesc = (RelativeLayout) createView();
                populateDescView(dtcDesc, dtcList.get(i));
                ll.addView(dtcDesc);
            }
        }

    }


    private String getServiceText(String text){
        if(text.equalsIgnoreCase("ServiceRequired"))
            return "Service Required";
        else
            return text;
    }

    private void populateDescView(RelativeLayout dtcDescView, DiagnosticInfo dtcData) {
        // DTC Code
        TextView tvDTCCode = (TextView) dtcDescView.findViewById(R.id.lbl_dtc_code);
        String dtc = (dtcData.getCode() == null || dtcData.getCode().equalsIgnoreCase("null")) ? "" : dtcData.getCode();
        tvDTCCode.setText(mContext.getString(R.string.dtc_code_label)+" "+ dtc);

        // DTC Desc
        TextView tvDTCDesc = (TextView) dtcDescView.findViewById(R.id.lbl_dtc_description);
        tvDTCDesc.setText(dtcData.getDescription());

//        listener for info icon

        ImageView infoIV = (ImageView)dtcDescView.findViewById(R.id.infoIV);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToBeDecidedDialog dialog = new ToBeDecidedDialog(mContext, mContext.getResources().getString(R.string.dtc_info_text));
                dialog.setTitle("DTC Code");
                dialog.show();

            }
        });

    }

    private View createView() {
        LayoutInflater li = mContext.getLayoutInflater();
        return li.inflate(R.layout.dtc_desc_list, null);
    }

    // Stub started
   /* private void createDTCData() {
        DTCData battery = new DTCData();
        battery.set("ServiceArea", "Battery");
        ArrayList<DTCData> batteryDTCList = new ArrayList<DTCData>();
        DTCData ba1 = new DTCData();
        ba1.set("Code", "B0122-2A");
        ba1.set("Status", "ServiceRequired");
        ba1.set("Description", "Blurb on the function of the battery and what can happen when there is a malfunction identified.");
        batteryDTCList.add(ba1);
        DTCData ba2 = new DTCData();
        ba2.set("Code", "B8133-2A");
        ba2.set("Status", "MonitorClosely");
        ba2.set("Description", "Blurb on the function of the battery and what can happen when there is a malfunction identified.");
        batteryDTCList.add(ba2);
        battery.set("DTCList", batteryDTCList);
        mList.add(battery);
        DTCData alternator = new DTCData();
        alternator.set("ServiceArea", "Alternator");
        ArrayList<DTCData> alternatorDTCList = new ArrayList<DTCData>();
        DTCData al1 = new DTCData();
        al1.set("Code", "ALT43-8C");
        al1.set("Status", "MonitorClosely");
        al1.set("Description", "Blurb on the function of the battery and what can happen when there is a malfunction identified.");
        alternatorDTCList.add(al1);
        DTCData al2 = new DTCData();
        al2.set("Code", "ALT56-8C");
        al2.set("Status", "ServiceRequired");
        al2.set("Description", "Blurb on the function of the battery and what can happen when there is a malfunction identified.");
        alternatorDTCList.add(al2);
        alternator.set("DTCList", alternatorDTCList);
        mList.add(alternator);
    }*/
    // Stub ended.



}
