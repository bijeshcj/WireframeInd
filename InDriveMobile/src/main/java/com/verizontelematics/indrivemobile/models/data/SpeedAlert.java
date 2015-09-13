
package com.verizontelematics.indrivemobile.models.data;


public class SpeedAlert {


    private String AlertName;

    private Integer AlertId;

    private String Status;

    private Integer MaximumSpeed;

    public SpeedAlert() {
        AlertName = "SpeedAlert";
        AlertId = 0;
        Status = "InActive";
        MaximumSpeed = 0;
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

    /**
     * 
     * @return
     *     The MaximumSpeed
     */
    public Integer getMaximumSpeed() {
        return MaximumSpeed;
    }

    /**
     * 
     * @param MaximumSpeed
     *     The MaximumSpeed
     */
    public void setMaximumSpeed(Integer MaximumSpeed) {
        this.MaximumSpeed = MaximumSpeed;
    }

}
