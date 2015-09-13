package com.verizontelematics.indrivemobile.adapters.fragmentpageradapters;

/**
 * Created by bijesh on 8/22/2014.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.activity.UIState;
import com.verizontelematics.indrivemobile.components.view.FragmentPagerAdapter;
import com.verizontelematics.indrivemobile.fragments.AlertsFragment;
import com.verizontelematics.indrivemobile.fragments.DiagnosticsFragment;
import com.verizontelematics.indrivemobile.fragments.DrivingDataHome;
import com.verizontelematics.indrivemobile.fragments.EmergencyFragment;
import com.verizontelematics.indrivemobile.fragments.LocationFragment;

//import com.verizontelematics.indrivemobile.fragments.DrivingDataFragment;


/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
 * sections of the app.
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "AppSectionsPagerAdapter";
    private UIState mUIState;
    private String[] moduleArray ;
    private int[] mTabIcons = {
            R.drawable.alerts_icon,
            R.drawable.location_icon,
            R.drawable.data_icon,
            R.drawable.diagnostics_icon,
            R.drawable.emergency_icon
    };

    private int[] mHeaderBackgrounds = {
            // right now alerts_header_background, location_header_background and help_header_background .png not available
            // So creating gradient drawables for the those.
            R.drawable.alerts_header_background,
            R.drawable.location_header_background,
            R.drawable.drivingdata_header_background,
            R.drawable.diagnostic_header_background,
            R.drawable.help_header_background
    };

    public AppSectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        Context mContext = context;
        if (HomeActivity.class.isInstance(mContext)) {
            mUIState = ((HomeActivity) mContext).getUIState();
        }
        moduleArray = context.getResources().getStringArray(R.array.module_array);

    }

    @Override
    public Fragment getItem(int i) {
        Log.d(TAG, "getItem(" + i + ")");

        if (mUIState == null) {
            return null;
        }

        // Check if fragments are already in memory
        // re-use.
        if (mUIState.getScreens() != null && mUIState.getScreens().size() > i)
            return (Fragment)mUIState.getScreens().get(i);

        switch (i) {
            case 0:
                // Launches Alert fragment
               mUIState.addScreen(0, new AlertsFragment());
               return (Fragment)mUIState.getScreens().get(0);

            case 1:
                // Launches Location fragment
                mUIState.addScreen(1, new LocationFragment());
                return (Fragment)mUIState.getScreens().get(1);

            case 2:
                // Launches Driving data fragment
                mUIState.addScreen(2, new DrivingDataHome());
                return (Fragment)mUIState.getScreens().get(2);

            case 3:
                // Launches Diagnostics fragment
                mUIState.addScreen(3, new DiagnosticsFragment());
                return (Fragment)mUIState.getScreens().get(3);

            case 4:
                // Launches Help&Preferences fragment
                mUIState.addScreen(4, new EmergencyFragment());
                return (Fragment)mUIState.getScreens().get(4);

            default:
                // The other sections of the app are dummy placeholders.
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return moduleArray.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return moduleArray[position];
    }

    public int getImageResource(int i) {
        return mTabIcons[i];
    }

    public int getHeaderBackground(int i) {
        return mHeaderBackgrounds[i];
    }
}
