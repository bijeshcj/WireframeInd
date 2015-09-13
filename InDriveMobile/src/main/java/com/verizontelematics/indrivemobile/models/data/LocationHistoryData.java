
package com.verizontelematics.indrivemobile.models.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;


public class LocationHistoryData extends BaseData{

    @Expose
    private String VehicleID;
    @Expose
    private List<LocationHistoryDatum> LocationHistoryData = new ArrayList<LocationHistoryDatum>();

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
     *     The LocationHistoryData
     */
    public List<LocationHistoryDatum> getLocationHistoryData() {
        return LocationHistoryData;
    }

    /**
     * 
     * @param LocationHistoryData
     *     The LocationHistoryData
     */
    public void setLocationHistoryData(List<LocationHistoryDatum> LocationHistoryData) {
        this.LocationHistoryData = LocationHistoryData;
    }

}
