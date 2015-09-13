package com.verizontelematics.indrivemobile.models;

import com.verizontelematics.indrivemobile.models.data.BaseData;

/**
 * Created by z689649 on 9/17/14.
 */
public class Accounts extends BaseData {

    private String accountIdServer;
    private String vehicleDesc;
    private long id;

    public String getAccountIdServer() {
        return accountIdServer;
    }

    public void setAccountIdServer(String accountIdServer) {
        this.accountIdServer = accountIdServer;
    }

    public String getVehicleDesc() {
        return vehicleDesc;
    }

    public void setVehicleDesc(String vehicleDesc) {
        this.vehicleDesc = vehicleDesc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
