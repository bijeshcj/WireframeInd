package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.AccountTable;
import com.verizontelematics.indrivemobile.models.Accounts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z689649 on 9/17/14.
 * AccountSelector activity here.
 */
public class AccountSelectorActivity extends Activity {

    private List<Accounts> accounts = new ArrayList<Accounts>();
    private static final int MAX_DUMMY_ITEMS = 5;

    private ListAdapter adapter = new ListAdapter() {
        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            // do nothing for now.
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            // do nothing for now.
        }

        @Override
        public int getCount() {
            return accounts.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RelativeLayout lLayout = (RelativeLayout) convertView;
            if(convertView == null){
                LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                lLayout = (RelativeLayout) inflator.inflate(R.layout.account_selector_list_item,null);
            }

            TextView lTextView =  (TextView) lLayout.findViewById(R.id.accountNumber);
            Accounts lAccount  = accounts.get(position);
            // we need to show only 4 chars and all others will be shown as "*"
            String lAccountIdString = obfuscateString(lAccount.getAccountIdServer(), 4);
            lTextView.setText(lAccountIdString);
            lTextView =  (TextView) lLayout.findViewById(R.id.vehicle_details_line_1);
            lTextView.setText(lAccount.getVehicleDesc());

            return lLayout;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_selector_activity);
        ListView lListView = (ListView) findViewById(R.id.listView);
        populateListItems(MAX_DUMMY_ITEMS);
        lListView.setAdapter(adapter);
        // check on how to process the items here.
        lListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent lIntent = getIntent();
                lIntent.putExtra(LoginActivity.ACCOUNT_ID,accounts.get(position).getAccountIdServer());
                setResult(Activity.RESULT_OK, lIntent);
                // we clicked on some item here.
                finish();
            }
        });
        findViewById(R.id.button_Login).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent lIntent = getIntent();
                setResult(Activity.RESULT_CANCELED, lIntent);
                finish();
            }
        });
    }

    private String obfuscateString(String aString, int aNumberOfCharsToShow){
        int lTotalLength = aString.length();
        int lNumberOfStars = lTotalLength  - aNumberOfCharsToShow;
        String lSubString = null;
        if(lTotalLength > aNumberOfCharsToShow) {
            lSubString = aString.substring(lTotalLength - aNumberOfCharsToShow);
        }
        String repeatedStart = new String(new char[lNumberOfStars]).replace("\0","*");
        return  repeatedStart + lSubString;
    }

    // Populate list items here.
    private void populateListItems(int size){

        // get the list from storageTransaction.
        accounts.clear();
        StorageTransaction lTransaction = new StorageTransaction(this);

        Cursor C = lTransaction.getAllData(AccountTable.TABLE_NAME_ACCOUNT,null,false);

        if(C != null && C.moveToFirst()){
            do{
                Accounts lAccount = new Accounts();
                lAccount.setAccountIdServer(C.getString(C.getColumnIndex(AccountTable.COLUMN_ACCOUNT_ID_SERVER)));
                lAccount.setVehicleDesc(C.getString(C.getColumnIndex(AccountTable.COLUMN_VEHICLE_DES_)));
                accounts.add(lAccount);
            }while(C.moveToNext());
        }else {
            for (int i = 0; i < size; i++) {
                accounts.add(populateDummyAccounts());
            }
        }

        if(C != null){
            C.close();
        }
    }

    // Populate dummy accounts here.
    private Accounts populateDummyAccounts(){

        Accounts lAccount = new Accounts();
        lAccount.setAccountIdServer("" + ((int) (10000000 * Math.random())) / 10);
        lAccount.setVehicleDesc(this.getResources().getString(R.string.vehicle_desc));
        StorageTransaction lTransaction = new StorageTransaction(this);
        lTransaction.insertAccount(lAccount);
        return lAccount;
    }

}
