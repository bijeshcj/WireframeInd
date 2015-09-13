package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.adapters.fragmentpageradapters.IndriveBaseOptionsFragmentAdapter;
import com.verizontelematics.indrivemobile.common.SlidingTabLayout;
import com.verizontelematics.indrivemobile.components.view.ViewPager;

/**
 * Created by bijesh on 11/18/2014.
 */
public class IndriveBaseOptionsFragment extends Fragment {

    private static final String TAG = IndriveBaseOptionsFragment.class.getCanonicalName();
    private String[] moduleArray;
    private FragmentManager mFragmentManager;
    private Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getBaseContext();
        mFragmentManager = ((ActionBarActivity)activity).getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_options, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        Log.d(TAG, "$$$ is ViewPager null "+(mViewPager == null)+" mFragmentManager null "+(mFragmentManager == null)+" mContext null "+(mContext == null));
        mViewPager.setAdapter(new IndriveBaseOptionsFragmentAdapter(getChildFragmentManager(), getActivity()));

        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }
}
