package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.models.EventData;
import com.verizontelematics.indrivemobile.models.PollOperation;
import com.verizontelematics.indrivemobile.models.RealVehicleEventData;

/**
 * Created by z688522 on 2/6/15.
 */
public class RealVehicleEventFactory implements EventFactory {

    private static RealVehicleEventFactory mInstance = null;
    public static RealVehicleEventFactory instance() {
        if (mInstance == null) {
            mInstance = new RealVehicleEventFactory();
        }
        return mInstance ;
    }
    @Override
    public EventData createEvent(PollOperation opr) {
        if (opr.getRemainingPolls() == 0) {
            return new RealVehicleEventData(opr, "time-out");
        }
        return new RealVehicleEventData(opr);
    }
}
