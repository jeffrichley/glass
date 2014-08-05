package com.infinity.glass.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.infinity.glass.rest.data.DataTuple;
import com.infinity.glass.rest.data.DescribeData;
import com.infinity.glass.rest.data.StringDataColumn;

public class LabelDescriberTest {

	private LabelDescriber cut;
	private StringDataColumn column;

	@Before
	public void setUp() throws Exception {
		cut = new LabelDescriber();
		column = new StringDataColumn();
		
		column.addRow("One");
		column.addRow("One");
		column.addRow("Two");
		column.addRow("Two");
		column.addRow("Two");
	}

	@Test
	public void canDescribe() {
		DescribeData data = cut.describe(column);
		
		DataTuple<Integer> twos = data.getPairs().get(0);
		DataTuple<Integer> ones = data.getPairs().get(1);
		
		assertThat(twos.getLabel(), is(equalTo("Two")));
		assertThat(twos.getValue(), is(equalTo(3)));

		assertThat(ones.getLabel(), is(equalTo("One")));
		assertThat(ones.getValue(), is(equalTo(2)));
	}

}
