package com.infinity.glass.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.LabelNumericCompareData;
import com.infinity.glass.rest.data.StatTuple;
import com.infinity.glass.rest.data.StringDataColumn;
import com.infinity.glass.rest.utils.StatsMath;

public class LabelNumericComparer {

	public LabelNumericCompareData compare(StringDataColumn labelColumn, DoubleDataColumn numericColumn) {
		LabelNumericCompareData data = new LabelNumericCompareData(labelColumn.getLabel(), numericColumn.getLabel());
		data.setTitle(labelColumn.getLabel() + " vs. " + numericColumn.getLabel());
		
		Map<String, DescriptiveStatistics> statMap = new HashMap<String, DescriptiveStatistics>();
		
		final List<String> rows = labelColumn.getRows();
		for (int i = 0; i < rows.size(); i++) {
			String key = rows.get(i);
			if (key != null) {
				Double value = numericColumn.getRows().get(i);
				if (value != null) {
					DescriptiveStatistics stat = statMap.get(key);
					if (stat == null) {
						stat = new DescriptiveStatistics();
						statMap.put(key, stat);
//						statMap.put(key.trim().equals("") ? "NOT ANSWERED" : key, stat);
					}
					stat.addValue(value);
				}
			}
		}
		
		
		List<StatTuple> tuples = new ArrayList<StatTuple>();
		for (String key : statMap.keySet()) {
			DescriptiveStatistics stats = statMap.get(key);
			
			double sum = stats.getSum();
			int count = stats.getValues().length;
			double average = stats.getMean();
			double median = stats.getPercentile(50);
			
			StatTuple tuple = new StatTuple(key, sum, count, average, median);
			tuples.add(tuple);
			
//			means.add(new double[]{average});
		}
		
		Collections.sort(tuples, new Comparator<StatTuple>() {
			@Override
			public int compare(StatTuple s1, StatTuple s2) {
				return Double.valueOf(s2.getSum()).compareTo(Double.valueOf(s1.getSum()));
			}
		});
		
		data.addTuples(tuples);
		
		List<double[]> means = new ArrayList<double[]>();
		for (DescriptiveStatistics ds : statMap.values()) {
			if (ds.getValues().length > 1) {
				means.add(ds.getValues());
			}
		}
		if (means.size() > 1) {
			data.setCorrelation(StatsMath.getAnovaF(means));
		} else {
			data.setCorrelation(1.0D);
		}
		
		return data;
	}

}
