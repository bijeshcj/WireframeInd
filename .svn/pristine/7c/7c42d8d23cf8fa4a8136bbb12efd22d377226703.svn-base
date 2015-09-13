package com.verizontelematics.indrivemobile.utils.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.utils.BasicUtil;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyanga on 10/6/2014.
 */
public class CustomServiceTypeDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView serviceTypeLV;
    private Context context;
    private CheckBox customServiceTypeCB;
    private EditText customServiceTypeET;
    private RelativeLayout selectServiceTypeRL;
    private String listString = "";
    private CustomDialogInterface observer = null;
    private InDrivePreference appPref;
    private ArrayList serviceTypeArrList;

    public CustomServiceTypeDialog(Context context, CustomDialogInterface observer) {
        super(context);
        this.context = context;
        this.observer = observer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        setContentView(view);
        setTitle(context.getResources().getString(R.string.service_type));

        appPref = InDrivePreference.getInstance();


        //Service Type ListView
        serviceTypeLV = (ListView) view.findViewById(R.id.serviceTypeLV);



        serviceTypeArrList = appPref.getCustomObject(InDrivePreference.SERVICE_TYPE, new TypeToken<List<String>>() {}.getType());
        if (serviceTypeArrList != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice, serviceTypeArrList);
            serviceTypeLV.setOnItemClickListener(this);
            serviceTypeLV.setAdapter(adapter);
        }

        //

        customServiceTypeET = (EditText) view.findViewById(R.id.enterServiceTypeET);

        selectServiceTypeRL = (RelativeLayout) view.findViewById(R.id.selectServiceTypeRL);
        RelativeLayout enterServiceTypeRL = (RelativeLayout) view.findViewById(R.id.enterServiceTypeRL);


        customServiceTypeCB = (CheckBox) view.findViewById(R.id.enterServiceTypeCB);
        customServiceTypeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    customServiceTypeET.setEnabled(true);
                    customServiceTypeET.requestFocus();
                    BasicUtil.setAlphaToViews((float) 0.2, selectServiceTypeRL);
                    serviceTypeLV.setEnabled(false);
                    CustomServiceTypeDialog.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                } else {
                    customServiceTypeET.setText("");
                    customServiceTypeET.setEnabled(false);
                    BasicUtil.setAlphaToViews((float) 1, selectServiceTypeRL);
                    serviceTypeLV.setEnabled(true);
                }

            }
        });

        Button saveBTN = (Button) view.findViewById(R.id.saveBTN);
        Button cancelBTN = (Button) view.findViewById(R.id.cancelBTN);

        saveBTN.setOnClickListener(this);
        cancelBTN.setOnClickListener(this);

        // Listener for the back button press event - Dialog should be closed when the back button is pressed.
        this.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    observer.onDialogCancelled();
                    dismiss();
                }
                return false;
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        listString = ((CheckedTextView) view).getText().toString();
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.saveBTN:
                saveBtnAction();
                break;

            case R.id.cancelBTN:
                Log.v("Cancel Btn", "Cancel btn");
                cancelBtnAction();
                break;

            default:
                break;


        }

    }

    private void cancelBtnAction() {

        observer.onNegativeButtonClick();
        dismiss();
    }

    private void saveBtnAction() {
        String selectedServiceType = "";
        if (customServiceTypeCB.isChecked()) {
            selectedServiceType = customServiceTypeET.getText().toString();
        } else {

            selectedServiceType = listString;

        }
        Log.v("Selected Service Type is ", selectedServiceType);
        observer.onPositiveButtonClick(selectedServiceType);
        dismiss();
        persistAddedServiceType(selectedServiceType);

    }

    private void persistAddedServiceType(String newValue) {

        serviceTypeArrList.add(newValue);
        appPref.setCustomObject(InDrivePreference.SERVICE_TYPE,serviceTypeArrList);


    }

    public interface CustomDialogInterface {
        public void onPositiveButtonClick(String serviceType);

        public void onNegativeButtonClick();

        public void onDialogCancelled();
    }


}
