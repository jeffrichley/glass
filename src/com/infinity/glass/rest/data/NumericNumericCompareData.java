package com.infinity.glass.rest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumericNumericCompareData extends CompareData {
	
	private final List<XYTuple> points = new ArrayList<XYTuple>();
	
	public NumericNumericCompareData(String fieldOne, String fieldTwo) {
		super(CompareType.NUMERIC_NUMERIC, fieldOne, fieldTwo);
	}

	public void addPoint(XYTuple tuple) {
		points.add(tuple);
	}

	public List<XYTuple> getPoints() {
		return Collections.unmodifiableList(points);
	}
}
