package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.models.EventData;
import com.verizontelematics.indrivemobile.models.PollOperation;

/**
 * Created by z688522 on 2/6/15.
 */
public interface EventFactory {
    EventData createEvent(PollOperation opr);
}
