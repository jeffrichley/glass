package com.infinity.glass.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DataTuple;
import com.infinity.glass.rest.data.DescribeData;

public class NumericDescriber implements DataDescriber<Double> {

	@Override
	public DescribeData describe(DataColumn<Double> column) {
		DescribeData data = new DescribeData();
		
		Map<Integer, Integer> info = new HashMap<Integer, Integer>();
		
		for (Double key : column.getRows()) {
			if (key != null) {
				Integer value = info.get(key.intValue());
				if (value == null) {
					value = 0;
				}
				value++;
				info.put(key.intValue(), value);
			}
		}
		
		List<DataTuple<Integer>> tuples = new ArrayList<DataTuple<Integer>>();
		for (Integer key : info.keySet()) {
			DataTuple<Integer> tuple = new DataTuple<Integer>(key.toString(), info.get(key));
			tuples.add(tuple);
		}
		
		Collections.sort(tuples, new Comparator<DataTuple<Integer>>() {
			@Override
			public int compare(DataTuple<Integer> t1, DataTuple<Integer> t2) {
				Integer v1 = Integer.parseInt(t1.getLabel());
				Integer v2 = Integer.parseInt(t2.getLabel());
				return v1.compareTo(v2);
			}
		});
		
		data.addTuples(tuples);
		
		return data;
	}

}
