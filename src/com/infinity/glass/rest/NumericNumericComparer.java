package com.infinity.glass.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.infinity.glass.rest.data.DataPoint;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.NumericNumericCompareData;
import com.infinity.glass.rest.data.Range;
import com.infinity.glass.rest.utils.StatsMath;

public class NumericNumericComparer {

	public NumericNumericCompareData compare(DoubleDataColumn firstColumn, DoubleDataColumn secondColumn) {
		final NumericNumericCompareData data = new NumericNumericCompareData(firstColumn.getLabel(), secondColumn.getLabel());
		data.setTitle(firstColumn.getLabel() + " vs. " + secondColumn.getLabel());
		final List<Double> xRows = firstColumn.getRows();
		final List<Double> yRows = secondColumn.getRows();
		
		// making this super ugly
		final Range xRange = new Range(Double.MAX_VALUE, Double.MIN_VALUE);
		final Range yRange = new Range(Double.MAX_VALUE, Double.MIN_VALUE);
		data.setFieldOneRange(xRange);
		data.setFieldTwoRange(yRange);
		
		final List<DataPoint> points = new ArrayList<DataPoint>();
		// map the frequency of points to their counts
		final Map<DataPoint, Integer> frequency = new HashMap<DataPoint, Integer>();
		for (int i = 0; i < xRows.size(); i++) {
			final Double x = xRows.get(i);
			final Double y = yRows.get(i);
			if (x != null && y != null) {
				final DataPoint point = new DataPoint(x, y);
				// updateFrequency - if it's already contained increase count, otherwise set to 1
				if (frequency.containsKey(point)) {
					frequency.put(point, frequency.get(point) + 1);
				} else {
					frequency.put(point, 1);
				}
				// add point to the whole list
				points.add(point);
				
				// determine min & max here - one pass - whatever
				xRange.setMinValue(Math.min(xRange.getMinValue(), x));
				xRange.setMaxValue(Math.max(xRange.getMaxValue(), x));
				yRange.setMinValue(Math.min(yRange.getMinValue(), y));
				yRange.setMaxValue(Math.max(yRange.getMaxValue(), y));
			}
		}
		// get regression line & correlation data from the set of all points
		data.setRegressionData(StatsMath.getRegressionData(points));
		data.setCorrelation(StatsMath.getCorrelation(points));
		
		// compress the points down to just include their size and put them into the compare data
		for (final Entry<DataPoint, Integer> entry : frequency.entrySet()) {
			final DataPoint point = entry.getKey();
			point.setCount(entry.getValue());
			data.addPoint(point);
		}
		
		return data;
	}
	
	
	
}
