package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.controllers.PollManager;
import com.verizontelematics.indrivemobile.controllers.RestResponseHandlerThread;
import com.verizontelematics.indrivemobile.httpwrapper.RestClient;
import com.verizontelematics.indrivemobile.httpwrapper.RestClientFactory;
import com.verizontelematics.indrivemobile.utils.phone.RetryTimer;

/**
 * Created by z688522 on 2/6/15.
 */
public class RealVehicleRequestStatusPollOperation implements PollOperation, RetryTimer.RetryTimeoutListener{

    private RestClient.HttpRequest mPollRequest;
    private Object mPollResponse;
    private int mPollsCount = 12;
    private int mPolls = 0;
    private int mPollInterval = 10;
    private String mOperation = "";
    private RetryTimer mRetryTimer;
    private OnCheckPollListener mListener;
    public RealVehicleRequestStatusPollOperation() {
        configure(12, 10);
        mRetryTimer =  new RetryTimer(mPollInterval*1000, 1000);
        mRetryTimer.setTimeoutListener(this);
    }

    public RealVehicleRequestStatusPollOperation(String operation) {
        mOperation = operation;
    }

    public void configure(int polls, int pollInterval) {
        mPolls = polls;
        mPollInterval = pollInterval;
        mPollsCount = mPolls;
    }

    @Override
    public String getRequestURL() {

        if (mPollRequest == null)
            return "";
        return mPollRequest.getUrl();
    }

    @Override
    public String getOperation() {

        return mOperation;
    }

    @Override
    public Object getResponse() {

        return mPollResponse;
    }

    @Override
    public int getPollTime() {

        return 5*1000;
    }

    @Override
    public int getRemainingPolls() {

        return mPollsCount;
    }

    @Override
    public Object getRequest() {

        return mPollRequest;
    }

    @Override
    public void setOperation(String opr) {

        mOperation = opr;
    }

    @Override
    public void setRequest(Object obj) {

        mPollRequest = (RestClient.HttpRequest)obj;
    }

    @Override
    public void setResponse(Object obj) {

        mPollResponse = obj;
    }

    @Override
    public int decrementPolls() {

        return mPollsCount--;
    }

    @Override
    public RetryTimer getRetryTimer() {
        return mRetryTimer;
    }

    @Override
    public void clean() {
        // Cancel Retry timers if any.
        if (mRetryTimer != null) {
            mRetryTimer.cancel();
            mRetryTimer.setTimeoutListener(null);
        }
        // cancel request
         RestClientFactory.getDefaultRestClient().cancelRequest(mPollRequest);
        // clear poll timer
        PollManager.instance().remove(this);
    }

    @Override
    public void cancel() {
        // Cancel Retry timers if any.
        if (mRetryTimer != null) {
            mRetryTimer.cancel();
        }
        // cancel request
        RestClientFactory.getDefaultRestClient().cancelRequest(mPollRequest);
        // clear poll timer
        mPollsCount = mPolls;
    }

    private void init() {
        mPollsCount = mPolls;
        if (mRetryTimer == null) {
            mRetryTimer = new RetryTimer(mPollInterval*1000, 1000);
        }
        mRetryTimer.setTimeoutListener(this);
    }

    @Override
    public boolean checkIfPollNeeded() {
        return mListener == null || mListener.checkPollNeeded(this);

    }

    @Override
    public void setOnCheckPollListener(OnCheckPollListener listener) {
        mListener = listener;
    }

    @Override
    public void onTick(long milliSeconds) {

    }

    @Override
    public void onFinish() {
        if (mPollRequest != null) {
            RestClientFactory.getDefaultRestClient().addRequest(mPollRequest, RestResponseHandlerThread.getHandler());
        }
    }
}

