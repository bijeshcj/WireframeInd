package com.verizontelematics.indrivemobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.adapters.ProfileAdapter;
import com.verizontelematics.indrivemobile.controllers.AuthenticateController;
import com.verizontelematics.indrivemobile.models.data.UserDetail;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;

import java.util.ArrayList;


/**
 * Created by bijesh on 1/29/2015.
 */
public class ProfileActivity extends FragmentActivity {

    private static String TAG = ProfileActivity.class.getCanonicalName();
    private ListView mListViewProfileOptions;

    private static final int CHANGE_PASSWORD = 0;
    private static final int PRIVACY_POLICY = 1;
    private static final int TERMS_OF_USE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initComponents();
        setupHeaderView();
        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_pro));

    }

    private void setupHeaderView() {

        RelativeLayout headerRL = (RelativeLayout) this.findViewById(R.id.headerRL);
//        headerRL.setBackgroundColor(this.getResources().getColor(R.color.diagnostics_code));
//        headerRL.setBackgroundResource(R.drawable.alerts_header_background);
        headerRL.setBackgroundColor(this.getResources().getColor(R.color.gray));
        TextView headerTitleTV = (TextView) headerRL.findViewById(R.id.title_header_TV);
        Button headerBtn = (Button) headerRL.findViewById(R.id.action_btn);
        headerTitleTV.setText(this.getResources().getString(R.string.profile_header));
        headerBtn.setVisibility(View.GONE);


    }

    private void initComponents() {
        ListView mListViewProfileHeader = (ListView) findViewById(R.id.profileHeaderList);
        UserDetail userDetail = AuthenticateController.instance().getPrimaryUserDetail();
        ArrayList<ProfileAdapter.UserInfo> detailsList = new ArrayList<ProfileAdapter.UserInfo>();
        detailsList.add(new ProfileAdapter.UserInfo(userDetail.getAccountInfo().getFirstName()+" "+userDetail.getAccountInfo().getLastName(), "Name"));
        detailsList.add(new ProfileAdapter.UserInfo(userDetail.getAccountInfo().getEmail(), "email"));
        detailsList.add(new ProfileAdapter.UserInfo(userDetail.getAccountInfo().getPhone(), "Primary phone (mobile phone required)"));
        ProfileAdapter mAdapter = new ProfileAdapter(this, 0, detailsList);
        mListViewProfileHeader.setAdapter(mAdapter);

        mListViewProfileOptions = (ListView) findViewById(R.id.profileOptionsList);
        ProfileAdapter mOptionsAdapter = new ProfileAdapter(this, 1, detailsList);
        mListViewProfileOptions.setAdapter(mOptionsAdapter);
        setUpListeners();
    }

    private void setUpListeners() {
        mListViewProfileOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case CHANGE_PASSWORD:
//                        openFragment();
                        openActivity(ProfileContentActivity.class);
                        break;
                    case PRIVACY_POLICY:
                        openActivity(ProfilePrivacyAndTermsActivity.class);
                        break;
                    case TERMS_OF_USE:
                        openActivity(ProfilePrivacyAndTermsActivity.class);
                        break;
                }
            }
        });
    }

    private void openActivity(Class<? extends Activity> aClass) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
    }

//    private void openFragment(){
//        System.out.println("$$$ openFragment");
//        //Remove all views from Content Layout
//        RelativeLayout contentLayout = (RelativeLayout)findViewById(R.id.container_id_profile);
//        if (contentLayout == null)
//            return;
//        contentLayout.removeAllViews();
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        Fragment fragment = new ProfileContentFragment();
//        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
//        transaction.addToBackStack("Profile");
//        transaction.add(R.id.container_id_profile, fragment, "Profile");
//        transaction.commit();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("$$$ on Back pressed");
    }
}
