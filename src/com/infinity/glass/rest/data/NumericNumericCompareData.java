package com.infinity.glass.rest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumericNumericCompareData extends CompareData {

	private Range fieldOneRange;
	private Range fieldTwoRange;
	private final List<DataPoint> points = new ArrayList<DataPoint>();
	private RegressionData regressionData;
	
	public NumericNumericCompareData(String fieldOne, String fieldTwo) {
		super(CompareType.NUMERIC_NUMERIC, fieldOne, fieldTwo);
	}

	public void addPoint(DataPoint tuple) {
		points.add(tuple);
	}
	
	public List<DataPoint> getPoints() {
		return Collections.unmodifiableList(points);
	}

	public void setRegressionData(RegressionData regressionData) {
		this.regressionData = regressionData;
	}
	
	public RegressionData getRegressionData() {
		return regressionData;
	}

	public Range getFieldOneRange() {
		return fieldOneRange;
	}

	public void setFieldOneRange(Range fieldOneRange) {
		this.fieldOneRange = fieldOneRange;
	}

	public Range getFieldTwoRange() {
		return fieldTwoRange;
	}

	public void setFieldTwoRange(Range fieldTwoRange) {
		this.fieldTwoRange = fieldTwoRange;
	}
}
