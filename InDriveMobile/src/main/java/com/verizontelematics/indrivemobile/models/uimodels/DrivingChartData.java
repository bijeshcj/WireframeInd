package com.verizontelematics.indrivemobile.models.uimodels;

import java.util.ArrayList;

/**
 * Created by bijesh on 12/12/2014.
 */
public class DrivingChartData {
    private ArrayList<Double> totalMiles;
    private ArrayList<Double> maxSpeeds;
    private ArrayList<Double> totalHoursDays;
    private ArrayList<Double> averageTrip;
    private ArrayList<Double> cityGallons;
    private ArrayList<Double> highwayGallons;
    private ArrayList<Double> ethanolCityGallons;
    private ArrayList<Double> ethanolHighwayGallons;
    private ArrayList<Double> carbonFootPrints;
    private ArrayList<Double> cityMiles;
    private ArrayList<Double> highwayMiles;
    private ArrayList<Double> cityMPG;
    private ArrayList<Double> highwayMPG;

    public ArrayList<Double> getCityMiles() {
        return cityMiles;
    }

    public void setCityMiles(ArrayList<Double> cityMiles) {
        this.cityMiles = cityMiles;
    }

    public ArrayList<Double> getHighwayMiles() {
        return highwayMiles;
    }

    public void setHighwayMiles(ArrayList<Double> highwayMiles) {
        this.highwayMiles = highwayMiles;
    }

    public ArrayList<Double> getCarbonFootPrints() {
        return carbonFootPrints;
    }

    public void setCarbonFootPrints(ArrayList<Double> carbonFootPrints) {
        this.carbonFootPrints = carbonFootPrints;
    }

    public ArrayList<Double> getCityMPG() {
        return cityMPG;
    }

    public void setCityMPG(ArrayList<Double> cityMPG) {
        this.cityMPG = cityMPG;
    }

    public ArrayList<Double> getHighwayMPG() {
        return highwayMPG;
    }

    public void setHighwayMPG(ArrayList<Double> highwayMPG) {
        this.highwayMPG = highwayMPG;
    }

    public ArrayList<Double> getEthanolCityGallons() {
        return ethanolCityGallons;
    }

    public void setEthanolCityGallons(ArrayList<Double> ethanolCityGallons) {
        this.ethanolCityGallons = ethanolCityGallons;
    }

    public ArrayList<Double> getEthanolHighwayGallons() {
        return ethanolHighwayGallons;
    }

    public void setEthanolHighwayGallons(ArrayList<Double> ethanolHighwayGallons) {
        this.ethanolHighwayGallons = ethanolHighwayGallons;
    }

    public ArrayList<Double> getCityGallons() {
        return cityGallons;
    }

    public void setCityGallons(ArrayList<Double> cityGallons) {
        this.cityGallons = cityGallons;
    }

    public ArrayList<Double> getHighwayGallons() {
        return highwayGallons;
    }

    public void setHighwayGallons(ArrayList<Double> highwayGallons) {
        this.highwayGallons = highwayGallons;
    }

    public ArrayList<Double> getAverageTrip() {
        return averageTrip;
    }

    public void setAverageTrip(ArrayList<Double> averageTrip) {
        this.averageTrip = averageTrip;
    }

    public ArrayList<Double> getTotalHoursDays() {
        return totalHoursDays;
    }

    public void setTotalHoursDays(ArrayList<Double> totalHoursDays) {
        this.totalHoursDays = totalHoursDays;
    }

    public ArrayList<Double> getMaxSpeeds() {
        return maxSpeeds;
    }

    public void setMaxSpeeds(ArrayList<Double> maxSpeeds) {
        this.maxSpeeds = maxSpeeds;
    }

    public ArrayList<Double> getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(ArrayList<Double> totalMiles) {
        this.totalMiles = totalMiles;
    }
}
