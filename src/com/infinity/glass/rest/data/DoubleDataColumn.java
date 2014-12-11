package com.infinity.glass.rest.data;

import java.io.Serializable;

public class DoubleDataColumn extends DataColumn<Double> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoubleDataColumn() {
		super(Type.NUMERIC);
	}

}
