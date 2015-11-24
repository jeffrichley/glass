package com.infinity.glass.rest;

import com.infinity.glass.rest.data.DataColumn;
import com.infinity.glass.rest.data.DescribeData;

public interface DataDescriber<T> {

	public DescribeData describe(DataColumn<T> data);
	
}
