package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.adapters.AlertHistoryAdapter;
import com.verizontelematics.indrivemobile.models.AlertHistory;

import java.util.ArrayList;

/**
 * Created by Priyanga on 8/26/2014.
 */
public class RecallAlertFragment extends Fragment {

    private ImageView onState;
    private ImageView offState;


    public RecallAlertFragment(){
//        Empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).setTitle(getResources().getStringArray(R.array.alerts_array)[6]);

        View rootView = inflater.inflate(R.layout.fragment_recall_alert,container,false);
        RelativeLayout switchLayout = (RelativeLayout) rootView.findViewById(R.id.layoutSwitch);
        onState= (ImageView)rootView.findViewById(R.id.onState);
        offState=(ImageView)rootView.findViewById(R.id.offState);
        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offState.getVisibility() == View.VISIBLE) {
                    //turning the state as ON
                    onState.setVisibility(View.VISIBLE);
                    offState.setVisibility(View.INVISIBLE);
                } else if (offState.getVisibility() == View.INVISIBLE) {
                    //turning the state as OFF
                    onState.setVisibility(View.INVISIBLE);
                    offState.setVisibility(View.VISIBLE);
                }
            }
        });
        ListView recallAlertListView = (ListView) rootView.findViewById(R.id.alertHistoryLV);
        AlertHistoryAdapter adapter = new AlertHistoryAdapter(getActivity(), R.layout.alert_history_lv_item,populateData());
        recallAlertListView.setAdapter(adapter);
        recallAlertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }

        });
        // Hack to not propagate touch events to fragment.
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return rootView;
    }

    private ArrayList<AlertHistory> populateData() {

        ArrayList<AlertHistory> alertArrayList = new ArrayList<AlertHistory>();

        AlertHistory alertHistoryObj = new AlertHistory("Recall.","Today","");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Recall.","Yesterday","");

        alertArrayList.add(alertHistoryObj);

        alertHistoryObj = new AlertHistory("Recall.","8/4","");

        alertArrayList.add(alertHistoryObj);

        return alertArrayList;

    }
}
