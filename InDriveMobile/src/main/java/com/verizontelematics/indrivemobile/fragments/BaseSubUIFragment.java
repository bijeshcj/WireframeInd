package com.verizontelematics.indrivemobile.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by z688522 on 11/21/14.
 */
public class BaseSubUIFragment extends Fragment implements SubUIFragmentInterface {
    private BaseUIScreenFragment mHomeFragment;


    public BaseSubUIFragment() {
        super();
    }

    @Override
    public void setHomeFragment(BaseUIScreenFragment fragment) {
        mHomeFragment = fragment;
    }

    @Override
    public BaseUIScreenFragment getHomeFragment() {
        return mHomeFragment;
    }

    @Override
    public void onSubFragmentResume() {

    }

    @Override
    public void onSubFragmentPause() {

    }

    public String  getCurrentFragmentName() {
        String tag ="";
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {

            Log.v("Count is ",""+getActivity().getSupportFragmentManager().getBackStackEntryCount());
            tag = getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            Log.v("Current fragment is ", tag) ;
        }
        return tag;
    }
}
