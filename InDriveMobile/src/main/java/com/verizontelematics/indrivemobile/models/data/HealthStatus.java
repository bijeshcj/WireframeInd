package com.verizontelematics.indrivemobile.models.data;

import java.util.ArrayList;

/**
 * Created by z688522 on 11/20/14.
 */
public class HealthStatus {

    private String ServiceArea;
    private String ServiceAreaStatus;
    private ArrayList<DiagnosticInfo> DiagnosticInfo;

    public String getServiceArea() {
        return ServiceArea;
    }

    public void setServiceArea(String serviceArea) {
        ServiceArea = serviceArea;
    }

    public String getServiceAreaStatus() {
        return ServiceAreaStatus;
    }

    public void setServiceAreaStatus(String serviceAreaStatus) {
        ServiceAreaStatus = serviceAreaStatus;
    }

    public ArrayList<DiagnosticInfo> getDiagnosticInfo() {
        return DiagnosticInfo;
    }

    public void setDiagnosticInfo(ArrayList<DiagnosticInfo> diagnosticInfo) {
        DiagnosticInfo = diagnosticInfo;
    }

    @Override
    public String toString() {
        return "Service Area "+ServiceArea+" ServiceArea Status "+ServiceAreaStatus;
    }
}
