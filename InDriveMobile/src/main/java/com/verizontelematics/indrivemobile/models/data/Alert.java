
package com.verizontelematics.indrivemobile.models.data;

import java.util.ArrayList;
import java.util.List;



public class Alert extends BaseData{


    private String VehicleID;
    private List<com.verizontelematics.indrivemobile.models.data.SpeedAlert> SpeedAlert = new ArrayList<com.verizontelematics.indrivemobile.models.data.SpeedAlert>();
    private List<com.verizontelematics.indrivemobile.models.data.DiagnosticAlert> DiagnosticAlert = new ArrayList<com.verizontelematics.indrivemobile.models.data.DiagnosticAlert>();
    private List<LocationAlert> LocationAlerts = new ArrayList<LocationAlert>();
    private List<ValetAlert> ValetAlerts = new ArrayList<ValetAlert>();

    public List<ValetAlert> getValetAlerts() {
        return ValetAlerts;
    }

    public void setValetAlerts(List<ValetAlert> valetAlerts) {
        ValetAlerts = valetAlerts;
    }

    /**
     * 
     * @return
     *     The VehicleID
     */
    public String getVehicleID() {
        return VehicleID;
    }

    /**
     * 
     * @param VehicleID
     *     The VehicleID
     */
    public void setVehicleID(String VehicleID) {
        this.VehicleID = VehicleID;
    }

    /**
     * 
     * @return
     *     The SpeedAlert
     */
    public List<com.verizontelematics.indrivemobile.models.data.SpeedAlert> getSpeedAlert() {
        return SpeedAlert;
    }

    /**
     * 
     * @param SpeedAlert
     *     The SpeedAlert
     */
    public void setSpeedAlert(List<com.verizontelematics.indrivemobile.models.data.SpeedAlert> SpeedAlert) {
        this.SpeedAlert = SpeedAlert;
    }

    /**
     * 
     * @return
     *     The DiagnosticAlert
     */
    public List<com.verizontelematics.indrivemobile.models.data.DiagnosticAlert> getDiagnosticAlert() {
        return DiagnosticAlert;
    }

    /**
     * 
     * @param DiagnosticAlert
     *     The DiagnosticAlert
     */
    public void setDiagnosticAlert(List<com.verizontelematics.indrivemobile.models.data.DiagnosticAlert> DiagnosticAlert) {
        this.DiagnosticAlert = DiagnosticAlert;
    }

    /**
     * 
     * @return
     *     The LocationAlerts
     */
    public List<LocationAlert> getLocationAlerts() {
        return LocationAlerts;
    }

    /**
     * 
     * @param LocationAlerts
     *     The LocationAlerts
     */
    public void setLocationAlerts(List<LocationAlert> LocationAlerts) {
        this.LocationAlerts = LocationAlerts;
    }


    @Override
    public String toString() {
        return "Alert ::: VehicleId "+VehicleID;
    }

    public boolean isEmpty() {
        return LocationAlerts.isEmpty() && DiagnosticAlert.isEmpty() && SpeedAlert.isEmpty();
    }
}
