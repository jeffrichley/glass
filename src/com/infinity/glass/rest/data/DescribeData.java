package com.infinity.glass.rest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DescribeData {
	
	private String title;
	private String uuid;
	private List<DataTuple<Integer>> pairs = new ArrayList<DataTuple<Integer>>();
	
	public void addTuple(DataTuple<Integer> pair) {
		pairs.add(pair);
	}
	
	public List<DataTuple<Integer>> getPairs() {
		return Collections.unmodifiableList(pairs);
	}

	public void addTuples(List<DataTuple<Integer>> tuples) {
		pairs = new ArrayList<DataTuple<Integer>>(tuples);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
