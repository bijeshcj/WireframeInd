package com.verizontelematics.indrivemobile.models;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by bijesh on 9/11/2014.
 */
public class GestureMessage {
    private String message;
    private boolean flag;
    private LatLng centerLatLng;
    private LatLng highestLatLng;
    private android.graphics.Point centerPoint;
    private ArrayList<Point> lstAllPoints;
    private float radius;


    public GestureMessage(String message,boolean flag){
        this.message = message;
        this.flag = flag;
    }

    public GestureMessage(String message,boolean flag,LatLng centerLatLng,Point centerPoint,ArrayList<Point> lstAllPoints){
        this.message = message;
        this.flag = flag;
        this.centerLatLng = centerLatLng;
        this.centerPoint = centerPoint;
        this.lstAllPoints = lstAllPoints;
    }

    public GestureMessage(String message,boolean flag,LatLng centerLatLng,float radius){
        this.message = message;
        this.flag = flag;
        this.centerLatLng = centerLatLng;
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public LatLng getHighestLatLng() {
        return highestLatLng;
    }

    public void setHighestLatLng(LatLng highestLatLng) {
        this.highestLatLng = highestLatLng;
    }

    public LatLng getCenterLatLng() {
        return centerLatLng;
    }

    public void setCenterLatLng(LatLng centerLatLng) {
        this.centerLatLng = centerLatLng;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public ArrayList<Point> getLstAllPoints() {
        return lstAllPoints;
    }

    public void setLstAllPoints(ArrayList<Point> lstAllPoints) {
        this.lstAllPoints = lstAllPoints;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
