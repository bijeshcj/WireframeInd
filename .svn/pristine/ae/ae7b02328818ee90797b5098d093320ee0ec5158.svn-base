package com.verizontelematics.indrivemobile.utils.phone;

import android.os.CountDownTimer;

/**
 * Created by z688522 on 2/6/15.
 */
public class RetryTimer extends CountDownTimer {

    public interface RetryTimeoutListener {
        public void onTick(long milliSeconds);
        public void onFinish();
    }
    private RetryTimeoutListener mListener;

    public RetryTimer(int i, int i1) {
        super(i, i1);
    }

    public void setTimeoutListener(RetryTimeoutListener listener) {
        mListener = listener;
    }
    @Override
    public void onTick(long millisUntilFinished) {
        if (mListener == null)
            return;
        mListener.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        if (mListener == null)
            return;
        mListener.onFinish();
    }
}
