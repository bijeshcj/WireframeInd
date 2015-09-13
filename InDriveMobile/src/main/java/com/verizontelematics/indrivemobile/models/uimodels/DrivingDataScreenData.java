package com.verizontelematics.indrivemobile.models.uimodels;

/**
 * Created by bijesh on 12/11/2014.
 */
public class DrivingDataScreenData {
    private double[] categoryData;
    private DrivingChartData chartData;

    public DrivingChartData getChartData() {
        return chartData;
    }

    public void setChartData(DrivingChartData chartData) {
        this.chartData = chartData;
    }

    public double[] getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(double[] categoryData) {
        this.categoryData = categoryData;
    }
}
