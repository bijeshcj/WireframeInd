package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.adapters.OptionsListAdapter;

import java.util.Stack;

/**
 * Created by z688522 on 9/9/14.
 */
public abstract class BaseUIScreenFragment extends Fragment implements UIScreen,  AdapterView.OnItemClickListener {

    private static final String TAG = "BaseUIScreenFragment";
    // Base Fragment data
    protected int mIndex = -1;
    // Title of UI Screen
    protected String mTitle= "";

    private Stack<String> mFragmentTagStack = new Stack<String>();
    // Options List adapter
    private BaseAdapter mOptionListAdapter;
    // Context of parent activity
    protected Activity mActivity;
    // Option menu item strings array.
    protected String[] mOptionMenuItems;

    protected int mColorCode;

    public BaseUIScreenFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        mActivity = activity;

        // setup title
        setupTitle();

        // setup option item names.
        setupOptionItemNames();

        //set background color for action bar
        setBackgroundColor();

        mOptionListAdapter = new OptionsListAdapter(mActivity,
                mOptionMenuItems);
        if (mIndex == -1 && mOptionListAdapter.getCount() > 0) {
            mIndex = 0;
        }
        ((BaseUI) mActivity).refresh(mIndex);
    }

    public Stack<String> getSubFragmentStack() {
        if (mFragmentTagStack != null)
            return mFragmentTagStack;
        else
            return null;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onBackPressed() {
        popFragmentStack();
    }

    public boolean popFragmentStack() {
        if (getActivity() == null)
            return false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFragmentTagStack.isEmpty()) {
                    getActivity().finish();
                    return;
                }
                // Hack need to done on onBackStackChanged
                final String fragmentTag = mFragmentTagStack.peek();
                // fire pause and resume for sub fragments.
                Fragment previousFragment = getCurrentFragment();
                if (previousFragment != null && BaseSubUIFragment.class.isInstance(previousFragment)) {
                    ((BaseSubUIFragment) previousFragment).onSubFragmentPause();
                }
                // Temp fix
                Fragment fragment = getFragmentManager().findFragmentByTag(fragmentTag);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
                ft.remove(fragment);
                ft.commit();
                if (!mFragmentTagStack.isEmpty()) {
                    mFragmentTagStack.pop();
                    // fire resume for current fragment
                    Fragment currentFragment = getCurrentFragment();
                    if (currentFragment != null) {
                        if (BaseSubUIFragment.class.isInstance(currentFragment))
                            ((BaseSubUIFragment) currentFragment).onSubFragmentResume();
                        else if (BaseUIScreenFragment.class.isInstance(currentFragment)) {
                            onPageSelected();
                        }
                    }

                }
            }
            // Temp fix ended.

//                // check if the fragment is in middle of back stack
//                int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
//
//                if (true) {
//                    FragmentManager.BackStackEntry backStackEntry = getFragmentManager().getBackStackEntryAt(0);
//
//                    if (false) {
//                        boolean done = getFragmentManager().popBackStackImmediate(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        if (done || getFragmentManager().findFragmentByTag(fragmentTag) == null) {
//
//                            mFragmentTagStack.pop();
//                            // fire resume for current fragment
//                            Fragment currentFragment = getCurrentFragment();
//                            if (currentFragment != null && BaseSubUIFragment.class.isInstance(currentFragment)) {
//                                ((BaseSubUIFragment)currentFragment).onSubFragmentResume();
//                            }
//
//                        } else {
//                            if (getView() == null)
//                                return;
//                            getView().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (getFragmentManager().findFragmentByTag(fragmentTag) == null) {
//                                        if (!mFragmentTagStack.isEmpty()) {
//                                            mFragmentTagStack.pop();
//                                            // fire resume for current fragment
//                                            Fragment currentFragment = getCurrentFragment();
//                                            if (currentFragment != null && BaseSubUIFragment.class.isInstance(currentFragment)) {
//                                                ((BaseSubUIFragment)currentFragment).onSubFragmentResume();
//                                            }
//                                        }
//                                    }
//                                }
//                            }, 500);
//                        }
//                        // Hack ended.
//                    }
//                    else {
//                        Fragment fragment = getFragmentManager().findFragmentByTag(fragmentTag);
//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        ft.remove(fragment);
//                        ft.commit();
//                        if (!mFragmentTagStack.isEmpty()) {
//                            mFragmentTagStack.pop();
//                            // fire resume for current fragment
//                            Fragment currentFragment = getCurrentFragment();
//                            if (currentFragment != null && BaseSubUIFragment.class.isInstance(currentFragment)) {
//                                ((BaseSubUIFragment)currentFragment).onSubFragmentResume();
//                            }
//                        }
//                    }
//                }
////            }
        });
        return true;
    }

    public Fragment getCurrentFragment() {
        if (mFragmentTagStack.isEmpty()) {
            return this;
        }
        return getFragmentManager().findFragmentByTag(mFragmentTagStack.peek());
    }


    public boolean pushFragmentStack(String tag) {
        Log.d(TAG, " count "+getFragmentManager().getBackStackEntryCount());
        mFragmentTagStack.push(tag);
        return true;
    }

    public String getCurrentSubFragmentTag() {
        if (mFragmentTagStack.isEmpty())
            return "";
        return mFragmentTagStack.peek();
    }


    @Override
    public BaseAdapter getOptionsListAdapter() {
        return mOptionListAdapter;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int getBackgroundColor() {
        return mColorCode;
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return this;
    }

    @Override
    public int getCurrentSelectedOptionItemIndex() {
        return mIndex;
    }

    protected abstract void setupTitle();

    protected abstract void setBackgroundColor();

    abstract void setupOptionItemNames();


    @Override
    public void onPageSelected() {
//        Log.d(TAG, "onPageSelected() " + mIndex);
    }

    @Override
    public  void onPageSelectionLost() {
//        Log.d(TAG, "onPageSelectionLost() "+mIndex);
    }
    @Override
    public void setIndex(int index) {
        mIndex = index;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d(TAG,"onResume");
    }


}
