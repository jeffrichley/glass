package com.infinity.glass.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.LabelNumericCompareData;
import com.infinity.glass.rest.data.StatTuple;
import com.infinity.glass.rest.data.StringDataColumn;

public class LabelNumericComparer {

	public CompareData compare(StringDataColumn labelColumn, DoubleDataColumn numericColumn) {
		LabelNumericCompareData data = new LabelNumericCompareData(labelColumn.getLabel(), numericColumn.getLabel());
		data.setTitle(labelColumn.getLabel() + " vs. " + numericColumn.getLabel());
		
		Map<String, DescriptiveStatistics> statMap = new HashMap<String, DescriptiveStatistics>();
		
		for (int i = 0; i < labelColumn.getRows().size(); i++) {
			String key = labelColumn.getRows().get(i);
			if (key != null) {
				Double value = numericColumn.getRows().get(i);
				if (value != null) {
					DescriptiveStatistics stat = statMap.get(key);
					if (stat == null) {
						stat = new DescriptiveStatistics();
						statMap.put(key, stat);
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
		}
		
		Collections.sort(tuples, new Comparator<StatTuple>() {
			@Override
			public int compare(StatTuple s1, StatTuple s2) {
				return Double.valueOf(s2.getSum()).compareTo(Double.valueOf(s1.getSum()));
			}
		});
		
		data.addTuples(tuples);
		
		return data;
	}

}
