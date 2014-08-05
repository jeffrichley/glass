package com.infinity.glass.rest;

import java.util.List;

import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.NumericNumericCompareData;
import com.infinity.glass.rest.data.XYTuple;

public class NumericNumericComparer {

	public CompareData compare(DoubleDataColumn firstColumn, DoubleDataColumn secondColumn) {
		NumericNumericCompareData data = new NumericNumericCompareData(firstColumn.getLabel(), secondColumn.getLabel());
		data.setTitle(firstColumn.getLabel() + " vs. " + secondColumn.getLabel());
		
		List<Double> oneRows = firstColumn.getRows();
		List<Double> twoRows = secondColumn.getRows();
		
		for (int i = 0; i < oneRows.size(); i++) {
			Double xVal = oneRows.get(i);
			Double yVal = twoRows.get(i);
			if (xVal != null && yVal != null) {
				XYTuple tuple = new XYTuple(xVal, yVal);
				data.addPoint(tuple);
			}
		}
		
		return data;
	}

}
