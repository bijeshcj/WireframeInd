package com.verizontelematics.indrivemobile.utils.phone;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by z688522 on 9/22/14.
 */
public class SessionManager {

    private static final String TAG = "SessionManager";
    private static SessionManager mInstance =  null;

    private long mWaitingPeriod;
    private long mCheckingPeriod;

    private CountDownTimer mCountDownTimer;

    public interface OnSessionExpireListener {
        public void onSessionExpire();
    }

    private OnSessionExpireListener mListener;

    private SessionManager() {
        mWaitingPeriod = 15 * 60 * 1000;
        mCheckingPeriod = 30 * 1000;
    }

    synchronized public static SessionManager getInstance() {
        if (mInstance == null) {
            mInstance = new SessionManager();
        }
        return mInstance;
    }

    public void setTimeoutPeriod(long milliSeconds) {
        mWaitingPeriod = milliSeconds;
    }

    public void interrupt() {
        Log.d(TAG, "interrupt()");
        reset();
    }

    public void reset() {
        Log.d(TAG, "reset()");
        if (mCountDownTimer == null) {
            return;
        }
        mCountDownTimer.cancel();

        mCountDownTimer.start();
//        mCountDownTimer = null;
    }

    public void start() {
        Log.d(TAG, "start()");
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(mWaitingPeriod, mCheckingPeriod) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, "onTic()");

                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "onFinish()");
                    if (mListener != null)
                        mListener.onSessionExpire();
                }
            };
        }
        mCountDownTimer.start();
    }

    public void setOnSessionExpireListener(OnSessionExpireListener listener) {
        Log.d(TAG, "setOnSessionExpireListener()");
        mListener = listener;
    }

}
