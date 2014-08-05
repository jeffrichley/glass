package com.infinity.glass.rest.data;

public class StatTuple {

	private final String label;
	private final double sum;
	private final int count;
	private final double average;
	private final double median;
	
	public StatTuple(String label, double sum, int count, double average, double median) {
		this.label = label;
		this.sum = sum;
		this.count = count;
		this.average = average;
		this.median = median;
	}

	public double getSum() {
		return sum;
	}

	public int getCount() {
		return count;
	}

	public double getAverage() {
		return average;
	}

	public double getMedian() {
		return median;
	}

	public String getLabel() {
		return label;
	}
	
	
}
