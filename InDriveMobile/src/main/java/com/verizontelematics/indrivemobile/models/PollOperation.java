package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.utils.phone.RetryTimer;

/**
 * Created by z688522 on 2/6/15.
 */
public interface PollOperation {
    String getRequestURL();
    Object getRequest();
    String getOperation();
    Object getResponse();
    int getPollTime();
    int getRemainingPolls();

    void setOperation(String opr);
    void setRequest(Object obj);
    void setResponse(Object obj);
    int decrementPolls();
    RetryTimer getRetryTimer();

    void clean();
    void cancel();

    boolean checkIfPollNeeded();

    public interface OnCheckPollListener {
        boolean checkPollNeeded(PollOperation pollOperation);
    }

    public void setOnCheckPollListener(OnCheckPollListener listener);
}
