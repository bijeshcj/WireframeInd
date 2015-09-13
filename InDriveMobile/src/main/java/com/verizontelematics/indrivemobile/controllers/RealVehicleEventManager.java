package com.verizontelematics.indrivemobile.controllers;

import com.verizontelematics.indrivemobile.models.EventData;
import com.verizontelematics.indrivemobile.models.PollOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by z688522 on 2/6/15.
 */
public class RealVehicleEventManager extends EventManager implements PollListener {

    private Map<String, Set<RealVehicleEventListener>> mEventListenerMap = null;

    public static String UPDATE_EVENT = "update";

    private RealVehicleEventManager() {

    }
    private static RealVehicleEventManager mInstance = null;
    public static RealVehicleEventManager instance() {
        if (mInstance == null) {
            mInstance = new RealVehicleEventManager();
            mInstance.initialize();
        }
        return mInstance;
    }
    private void initialize() {
        if (mEventListenerMap == null) {
            mEventListenerMap = new HashMap<String, Set<RealVehicleEventListener>>();
        }
    }

    public boolean addEventListener(String event, RealVehicleEventListener eventListener) {
        Set<RealVehicleEventListener> listeners = mEventListenerMap.get(event);
        if (listeners == null) {
            listeners = new HashSet<RealVehicleEventListener>();
            mEventListenerMap.put(event, listeners);
        }
        return listeners.add(eventListener);
    }

    public boolean removeEventListener(String event, RealVehicleEventListener eventListener)  {
        Set<RealVehicleEventListener> listeners = mEventListenerMap.get(event);
        return listeners != null && listeners.remove(eventListener);
    }

    public boolean fire(String event, EventData eventData) {
        Set<RealVehicleEventListener> listeners = mEventListenerMap.get(event);
        if (listeners == null)
            return false;
        for (RealVehicleEventListener listener :  listeners) {
            listener.onEvent(listener, eventData);
            if (eventData.isStopPropagation()) {
                return true;
            }

        }
        return true;
    }
    @Override
    public void onComplete(PollOperation pollOperation) {
        EventData evtData = RealVehicleEventFactory.instance().createEvent(pollOperation);
        fire(pollOperation.getOperation(), evtData);
    }

    public void clean(){
        // Clear the Event listener Map
        mEventListenerMap.clear();

    }
}
