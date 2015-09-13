package com.verizontelematics.indrivemobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by bijesh on 9/5/2014.
 */
public class LocationBoundaries extends Fragment implements UIScreen {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_boundaries,container,false);
        return rootView;
    }

    @Override
    public BaseAdapter getOptionsListAdapter() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return null;
    }

    @Override
    public int getCurrentSelectedOptionItemIndex() {
        return 0;
    }

    @Override
    public void onPageSelected() {

    }

    @Override
    public void onPageSelectionLost() {

    }

    @Override
    public void setIndex(int index) {

    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }
}
