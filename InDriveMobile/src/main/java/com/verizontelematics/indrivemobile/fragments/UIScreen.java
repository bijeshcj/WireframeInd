package com.verizontelematics.indrivemobile.fragments;

import android.widget.AdapterView;
import android.widget.BaseAdapter;

/**
 * Created by z688522 on 8/29/14.
 */
public interface UIScreen {
    public BaseAdapter getOptionsListAdapter();
    public String getTitle();
    public int getBackgroundColor();


    AdapterView.OnItemClickListener getOnItemClickListener();
    public int getCurrentSelectedOptionItemIndex();

    public void onPageSelected();
    public void onPageSelectionLost();

    void setIndex(int index);
}
