package com.verizontelematics.indrivemobile.utils.animations;

/**
 * Created by bijesh on 10/1/2014.
 */
public enum DeviceType {
    HDPI(1.5),
    XHDPI(2),
    XXHDPI(3);
    private double scale_factor;

    DeviceType(double scale_factor){
     this.scale_factor = scale_factor;
    }

    public double getScale_factor() {
        return scale_factor;
    }

//    public void setScale_factor(int scale_factor) {
//        this.scale_factor = scale_factor;
//    }
}
