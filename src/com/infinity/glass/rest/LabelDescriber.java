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

public class LabelDescriber implements DataDescriber<String> {

	public DescribeData describe(DataColumn<String> column) {
		DescribeData data = new DescribeData();
		
		Map<String, Integer> info = new HashMap<String, Integer>();
		
		for (String key : column.getRows()) {
			Integer value = info.get(key);
			if (value == null) {
				value = 0;
			}
			value++;
			info.put(key, value);
//			info.put(key.trim().equals("") ? "NOT ANSWERED" : key, value);
		}
		
		List<DataTuple<Integer>> tuples = new ArrayList<DataTuple<Integer>>();
		for (String key : info.keySet()) {
			DataTuple<Integer> tuple = new DataTuple<Integer>(key, info.get(key));
			tuples.add(tuple);
		}
		
		Collections.sort(tuples, new Comparator<DataTuple<Integer>>() {
			@Override
			public int compare(DataTuple<Integer> t1, DataTuple<Integer> t2) {
				return t2.getValue().compareTo(t1.getValue());
			}
		});
		
		data.addTuples(tuples);
		
		return data;
	}

}
