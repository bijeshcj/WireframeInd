package com.verizontelematics.indrivemobile.fragments;

/**
 * Created by z688522 on 11/21/14.
 */
public interface SubUIFragmentInterface {
    public void setHomeFragment(BaseUIScreenFragment fragment);
    public BaseUIScreenFragment getHomeFragment();
    public void onSubFragmentResume();
    public void onSubFragmentPause();
}
