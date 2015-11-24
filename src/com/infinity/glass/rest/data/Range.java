package com.infinity.glass.rest.data;

public class Range {

	private double minValue;
	private double maxValue;
	
	public Range() {
	}
	
	public Range(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public double getMinValue() {
		return minValue;
	}
	
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	
	public double getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
}
