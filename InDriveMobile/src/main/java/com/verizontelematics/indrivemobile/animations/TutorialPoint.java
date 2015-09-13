package com.verizontelematics.indrivemobile.animations;

/**
 * Created by Bijesh on 20-09-2014.
 */
public class TutorialPoint{
    public float x,y;
    public TutorialPoint(float x,float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return " x: "+x+" ,y: "+y;
    }
}
