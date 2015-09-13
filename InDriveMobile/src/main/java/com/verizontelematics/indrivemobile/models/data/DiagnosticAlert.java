
package com.verizontelematics.indrivemobile.models.data;


public class DiagnosticAlert {


    private String AlertName;

    private Integer AlertId;

    private String Status;

    @Override
    public String toString() {
        return "DiagnosticAlert name: "+AlertName+" id: "+AlertId+" Status "+Status;
    }

    public DiagnosticAlert() {
        AlertName = "DiagnosticAlert";
        AlertId = 0;
        Status = "InActive";
    }

    /**
     * 
     * @return
     *     The AlertName
     */
    public String getAlertName() {
        return AlertName;
    }

    /**
     * 
     * @param AlertName
     *     The AlertName
     */
    public void setAlertName(String AlertName) {
        this.AlertName = AlertName;
    }

    /**
     * 
     * @return
     *     The AlertId
     */
    public Integer getAlertId() {
        return AlertId;
    }

    /**
     * 
     * @param AlertId
     *     The AlertId
     */
    public void setAlertId(Integer AlertId) {
        this.AlertId = AlertId;
    }

    /**
     * 
     * @return
     *     The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * 
     * @param Status
     *     The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }


}
