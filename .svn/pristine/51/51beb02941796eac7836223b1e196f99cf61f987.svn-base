package com.verizontelematics.indrivemobile.fragments;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by Ravinder on 10/7/14.
 */
@Deprecated
public class ProgressDialogFragment extends BaseSubUIFragment implements View.OnKeyListener {

    private static final String PROGRESS_BAR_MESSAGE ="message";

    private TextView messageView;

    private boolean isVisible =  false;
    private ProgressDialogFragment.OnCancelListener mListener;
    private boolean mCancelled = false;

    public interface OnCancelListener {
        public void onCancel();
    }

    // This is needed since system instantiates it.
    public ProgressDialogFragment(){

    }

    public static  ProgressDialogFragment  createInstance(String message, BaseUIScreenFragment homeFragment) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setHomeFragment(homeFragment);
        Bundle bundle = new Bundle();
        bundle.putString(ProgressDialogFragment.PROGRESS_BAR_MESSAGE, message);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Fragments view creation.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create a progress view.
        View lView = inflater.inflate(R.layout.progress_fragment,container,false);
        messageView = (TextView) lView.findViewById(R.id.progressBarMessage);
        readFromArgumentAndSetMessage();
        lView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // nothing.
            }
        });
        lView.setFocusableInTouchMode(true);
        lView.requestFocus();
        lView.setOnKeyListener(this);
        return lView;
    }

    /**
     * read the message from arguments is available.
     */
    private void readFromArgumentAndSetMessage(){
        Bundle lBundle = getArguments();
        String lMessage = lBundle.getString(PROGRESS_BAR_MESSAGE);
        if(messageView != null && lMessage != null){
            messageView.setText(lMessage);
        }
    }

    /**
     * The back key is not being handled correctly every time.
     * Check what is wrong with it.
     * @param v
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mCancelled = true;
            dismiss();
            return true;
        }
        return false;
    }

    public void show(FragmentManager fm, int container) {
        if (isVisible)
            return;
        isVisible = true;
        FragmentManager mFragmentManager = fm;
        if (mFragmentManager == null) {
            mFragmentManager = getFragmentManager();
        }
        if (mFragmentManager == null)
            return;

        android.support.v4.app.FragmentTransaction ft = mFragmentManager.beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        ft.addToBackStack("ProgressDialog");
        ft.replace(container, this, "ProgressDialog");
        getHomeFragment().pushFragmentStack("ProgressDialog");
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isVisible = false;
        if (mCancelled && mListener != null) {
            mListener.onCancel();
            mCancelled = false;
        }
    }

    public void dismiss() {


//        if (mFragmentManager == null)
//            return;
//        android.support.v4.app.FragmentTransaction ft = mFragmentManager.beginTransaction();
//        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
//        ft.remove(this);
//        ft.commit();
//        if (mFragmentManager ==  null)
//            getFragmentManager().popBackStack();
//        else
//            mFragmentManager.popBackStack();
        isVisible = false;

        getHomeFragment().popFragmentStack();
    }

    public boolean isShowing() {
        return isVisible;
    }

    public void setOnCancelListener(OnCancelListener listener) {
        mListener = listener;
    }
}
