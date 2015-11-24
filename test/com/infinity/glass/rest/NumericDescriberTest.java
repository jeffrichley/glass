package com.infinity.glass.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.infinity.glass.rest.data.DataTuple;
import com.infinity.glass.rest.data.DescribeData;
import com.infinity.glass.rest.data.DoubleDataColumn;

public class NumericDescriberTest {

	private NumericDescriber cut;
	private DoubleDataColumn column;

	@Before
	public void setUp() throws Exception {
		cut = new NumericDescriber();
		column = new DoubleDataColumn();
		
		column.addRow(1d);
		column.addRow(1d);
		column.addRow(3d);
		column.addRow(2d);
		column.addRow(2d);
		column.addRow(2d);
	}

	@Test
	public void canDescribe() {
		DescribeData data = cut.describe(column);
		
		DataTuple<Integer> ones = data.getPairs().get(0);
		DataTuple<Integer> twos = data.getPairs().get(1);
		DataTuple<Integer> threes = data.getPairs().get(2);
		
		assertThat(ones.getLabel(), is(equalTo("1")));
		assertThat(ones.getValue(), is(equalTo(2)));

		assertThat(twos.getLabel(), is(equalTo("2")));
		assertThat(twos.getValue(), is(equalTo(3)));

		assertThat(threes.getLabel(), is(equalTo("3")));
		assertThat(threes.getValue(), is(equalTo(1)));

	}

}
