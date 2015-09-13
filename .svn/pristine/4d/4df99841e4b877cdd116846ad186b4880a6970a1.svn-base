package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.adapters.fragmentpageradapters.AppSectionsPagerAdapter;
import com.verizontelematics.indrivemobile.common.SlidingTabLayout;
import com.verizontelematics.indrivemobile.components.view.PagerAdapter;
import com.verizontelematics.indrivemobile.components.view.ViewPager;

/**
 * Created by bijesh on 8/27/2014.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getCanonicalName();
    private String[] moduleArray;
    private FragmentManager mFragmentManager;
    private Context mContext;



    public HomeFragment() {

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getBaseContext();
        mFragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        moduleArray = getActivity().getResources().getStringArray(R.array.module_array);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new AppSectionsPagerAdapter(mFragmentManager, mContext));
//        mViewPager.setAdapter(new HomePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }


    private class HomePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 5;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return moduleArray[position];//"Item Test " + (position + 1);
        }


        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            if (position == 0){
                // Inflate a new layout from our resources
                view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                        container, false);
                // Retrieve a TextView from the inflated View, and update it's text
                TextView title = (TextView) view.findViewById(R.id.item_title);
                title.setText(String.valueOf(position + 1));
              }
            // Add the newly created View to the ViewPager
            container.addView(view);



            Log.i(TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }



        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(TAG, "destroyItem() [position: " + position + "]");
        }

    }

}
