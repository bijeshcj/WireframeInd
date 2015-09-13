package com.verizontelematics.indrivemobile.adapters.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.components.view.FragmentPagerAdapter;
import com.verizontelematics.indrivemobile.fragments.AlertNewFragment;

/**
 * Created by bijesh on 11/18/2014.
 */
public class IndriveBaseOptionsFragmentAdapter extends FragmentPagerAdapter {

    private static final int NUM_SCREENS = 5;
    private String[] moduleArray ;

    public IndriveBaseOptionsFragmentAdapter(FragmentManager fm,Context context) {
        super(fm);
        moduleArray = context.getResources().getStringArray(R.array.module_array);
    }

    @Override
    public Fragment getItem(int i) {
        return new AlertNewFragment();
    }

//    @Override
//    public int getCount() {
//        return NUM_SCREENS;
//    }

    @Override
    public int getCount() {
        return moduleArray.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return moduleArray[position];
    }
}
