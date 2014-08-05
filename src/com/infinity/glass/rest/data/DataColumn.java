package com.infinity.glass.rest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class DataColumn<T> {

	public enum Type {
		NUMERIC, LABEL
	}
	
	private Type type;
	private String label;
	private List<T> rows = new ArrayList<T>();

	public DataColumn(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public void addRow(T val) {
		rows.add(val);
	}
	
	public List<T> getRows() {
		return Collections.unmodifiableList(rows);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
}
