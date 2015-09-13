package com.verizontelematics.indrivemobile.models;

import java.util.Arrays;

public class Point {

	private double x;
	private double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}


	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "{X: " + this.x + ",Y:" + this.y + "}";
	}

    @Override
    public boolean equals(Object o) {
        Point p = (Point)o;
        return this.x == p.getX() && this.y == p.getY();

    }

    //AS WE ARE OVERRIDING THE EQUALS METHOD
    //it is suggested to override the hashcode method also


    @Override
    public int hashCode() {
       return Arrays.hashCode(new Object[]{x, y});
    }
}
