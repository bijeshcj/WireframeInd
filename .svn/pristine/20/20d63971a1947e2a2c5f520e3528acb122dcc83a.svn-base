package com.verizontelematics.indrivemobile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by bijesh on 9/17/2014.
 */
public class TutorialFragment extends Fragment {
    private Handler mHandler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View lView  = inflater.inflate(R.layout.fragment_tutorial,container,false);
        lView.setBackgroundResource(R.color.transparent_tutorial);
        FragmentActivity mActivity = getActivity();
//        mHandler = new Handler();
//        mHandler.postDelayed(removeRunnable,20000);
        return lView;
    }

//    Runnable removeRunnable = new Runnable() {
//        @Override
//        public void run() {
//           if(mActivity != null)
//             mActivity.getSupportFragmentManager().beginTransaction().remove(TutorialFragment.this).commit();
//        }
//    };

}
