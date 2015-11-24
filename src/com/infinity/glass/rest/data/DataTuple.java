package com.infinity.glass.rest.data;

public class DataTuple<T> {

	private final String label;
	private final T value;

	public DataTuple(String label, T value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public T getValue() {
		return value;
	}
	
}
