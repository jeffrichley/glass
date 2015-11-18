package com.infinity.glass.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.LabelNumericCompareData;
import com.infinity.glass.rest.data.StatTuple;
import com.infinity.glass.rest.data.StringDataColumn;
import com.infinity.glass.rest.utils.StatsMath;

public class LabelNumericComparer {
	
	private static Logger LOGGER = LoggerFactory.getLogger(LabelNumericComparer.class);

	public LabelNumericCompareData compare(StringDataColumn labelColumn, DoubleDataColumn numericColumn, String requestUUID) {
		LabelNumericCompareData data = new LabelNumericCompareData(labelColumn.getLabel(), numericColumn.getLabel(), requestUUID);
		data.setTitle(labelColumn.getLabel() + " vs. " + numericColumn.getLabel());
		
		
		Object[] labels = labelColumn.getRows().toArray();
		Object[] values = numericColumn.getRows().toArray();
		
		
		Map<String, DescriptiveStatistics> statMap = new HashMap<String, DescriptiveStatistics>();
		
		for (int i = 0; i < labelColumn.getRows().size(); i++) {
			String key = labelColumn.getRows().get(i);
			if (StringUtils.isNotBlank(key)) {
				Double value = numericColumn.getRows().get(i);
				if (value != null) {
					DescriptiveStatistics stat = statMap.get(key);
					if (stat == null) {
						stat = new DescriptiveStatistics();
						statMap.put(key, stat);
					}
					stat.addValue(value);
//				} else {
//					LOGGER.info(String.format("Row: %d: label: %s, value: %s", i, labels[i], values[i]));
				}
			} else {
				
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
		
		int i = 0;
		List<double[]> means = new ArrayList<double[]>();
		for (DescriptiveStatistics ds : statMap.values()) {
			means.add(ds.getValues());
		}
		data.setCorrelation(StatsMath.getAnovaF(means));
		
		return data;
	}

}
