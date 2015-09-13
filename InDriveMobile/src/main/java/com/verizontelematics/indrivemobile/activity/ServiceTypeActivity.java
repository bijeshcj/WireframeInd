package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeActivity extends Activity {
    private ArrayList<Integer> lstDisableServiceTypes = new ArrayList<Integer>();
    private List<String> mServiceTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);

        setupHeaderView();

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            if ((arguments.containsKey("Service_Types")) && (arguments.getStringArrayList("Service_Types") != null)) {
                mServiceTypes = arguments.getStringArrayList("Service_Types");
            }

            if ((arguments.containsKey("Disabled_Service_Types")) && (arguments.getIntegerArrayList("Disabled_Service_Types") != null)) {
                lstDisableServiceTypes = arguments.getIntegerArrayList("Disabled_Service_Types");
            }
        }
        ListView serviceTypeLV = (ListView) this.findViewById(R.id.serviceTypeLV);
        CustomOptionListAdapter adapter = new CustomOptionListAdapter(this, R.layout.custom_options_list, mServiceTypes);
        serviceTypeLV.setAdapter(adapter);
        serviceTypeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isDisabledOptionSelectedByUser(position)){
                    return;
                }

                String selectedServiceType = mServiceTypes.get(position);
                Intent serviceTypeIntent = new Intent();
                serviceTypeIntent.putExtra("Position", position);
                serviceTypeIntent.putExtra("SelectedServiceType", selectedServiceType);
                setResult(RESULT_OK, serviceTypeIntent);
                finish();

            }
        });
    }

    private boolean isDisabledOptionSelectedByUser(int position){
        for(Integer disabledOption:lstDisableServiceTypes){
            if(disabledOption == position)
                return true;
        }
        return false;
    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
        headerRL.setBackgroundResource(R.drawable.diagnostic_header_background);
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.service_selection));
        headerBtn.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomOptionListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<String> lstServiceTypes;

        public CustomOptionListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.lstServiceTypes = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custom_options_list, parent, false);
            LinearLayout entireRow = (LinearLayout)rowView.findViewById(R.id.serviceTypeRow);
            TextView textView = (TextView) rowView.findViewById(R.id.txtViewPredefinedServiceType);
            textView.setText(lstServiceTypes.get(position));
            if (hasExistsServiceType(position)) {
                textView.setEnabled(false);
                rowView.setClickable(true);
                entireRow.setBackgroundColor(getResources().getColor(R.color.gray_separator));
                textView.setBackgroundColor(getResources().getColor(R.color.gray_separator));
            }
            else
            {
                textView.setEnabled(true);
                rowView.setClickable(false);
            }
            return rowView;
        }
    }

    private boolean hasExistsServiceType(int pos) {
        boolean retVal = false;
        if (lstDisableServiceTypes != null) {
            retVal = lstDisableServiceTypes.contains(pos);
        }

        return retVal;
    }
}
