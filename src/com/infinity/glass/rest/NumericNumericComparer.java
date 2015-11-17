package com.infinity.glass.rest;

import java.util.List;

import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.NumericNumericCompareData;
import com.infinity.glass.rest.data.XYTuple;
import com.infinity.glass.rest.utils.StatsMath;

public class NumericNumericComparer {

	public NumericNumericCompareData compare(DoubleDataColumn firstColumn, DoubleDataColumn secondColumn, String requestUUID) {
		NumericNumericCompareData data = new NumericNumericCompareData(firstColumn.getLabel(), secondColumn.getLabel(), requestUUID);
		data.setTitle(firstColumn.getLabel() + " vs. " + secondColumn.getLabel());
		
		List<Double> oneRows = firstColumn.getRows();
		List<Double> twoRows = secondColumn.getRows();
		
		for (int i = 0; i < oneRows.size(); i++) {
			Double xVal = oneRows.get(i);
			if (i < twoRows.size()) {
				Double yVal = twoRows.get(i);
				if (xVal != null && yVal != null) {
					XYTuple tuple = new XYTuple(xVal, yVal);
					data.addPoint(tuple);
				}
			}
		}
		
		double correlation = StatsMath.getCorrelation(data.getPoints());
		data.setCorrelation(correlation);
		
		return data;
	}

}
