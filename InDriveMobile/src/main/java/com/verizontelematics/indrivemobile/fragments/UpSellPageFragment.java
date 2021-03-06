package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.userprofile.UserRoleConstants;
import com.verizontelematics.indrivemobile.userprofile.utils.UpSellData;
import com.verizontelematics.indrivemobile.userprofile.utils.UserUtils;

/**
 * Created by bijesh on 2/3/2015.
 */
public class UpSellPageFragment extends BaseSubUIFragment implements UserRoleConstants {

    private static final String TAG = UpSellPageFragment.class.getCanonicalName();
    private LinearLayout mLinearLayout;
    private TextView mTxtHeader;
    private TextView mTxtMessage;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upsellpage, container, false);
        initComponents(view);
        process(getArguments());

        return view;
    }

    private void initComponents(View view){
        mLinearLayout = (LinearLayout) view.findViewById(R.id.upsellMainLayout);
        mTxtHeader = (TextView) view.findViewById(R.id.upsellHeader);
        mTxtMessage = (TextView) view.findViewById(R.id.upsellMessage);
    }

    private void process(Bundle bundle){
        if(bundle != null){
            String module = bundle.getString(BUNDLE_UPSELL_PAGE_MODULE_KEY);
            Log.d(TAG,"Upsell Page module "+module);
            UpSellData data = UserUtils.getUpSellData(module,getActivity());
            if(data != null){
                mLinearLayout.setBackgroundResource(data.getImageId());
                mTxtHeader.setText(data.getHeader());
                mTxtMessage.setText(data.getMessage());
            }
        }

    }

}
