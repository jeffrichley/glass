package com.infinity.glass.rest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LabelNumericCompareData extends CompareData {

	private List<StatTuple> pairs = new ArrayList<StatTuple>();
	
	public LabelNumericCompareData(String fieldOne, String fieldTwo) {
		super(CompareType.LABEL_NUMERIC, fieldOne, fieldTwo);
	}

	public List<StatTuple> getPairs() {
		return Collections.unmodifiableList(pairs);
	}
	
	public void addTuple(StatTuple tuple) {
		pairs.add(tuple);
	}

	public void addTuples(List<StatTuple> tuples) {
		pairs = new ArrayList<StatTuple>(tuples);
	}
	
}
