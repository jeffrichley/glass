package com.infinity.glass.rest.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.infinity.glass.rest.data.DataColumn.Type;

public class DataProviderTest {

	private DataProvider cut;
	private MatrixData matrix;

	@Before
	public void setUp() throws Exception {
		cut = new DataProvider();
		matrix = cut.getMatrixData(new TestServletContext(), "test-data/data-xtract.csv");
	}

	@Test
	public void canParseAFile() {
		assertThat(matrix, is(not(nullValue())));
	}
	
	@Test
	public void testParseHeaders() {
		// EVENT_ID,EVENT_SEVTY_C,INVLVD_PER_ID,AGE,HIGHT,WEIGHT,SEX,
		// SERVICE_CODE,SERVICE_STATUS,PER,PAYGRADE,PNT_UNIT_CODE,INJURY_CLA,INJ_COST
		DataColumn eventIdColumn = matrix.getDataColumn("EVENT_ID");
		assertThat(eventIdColumn, is(not(nullValue())));
		assertThat(eventIdColumn.getType(), is(equalTo(Type.NUMERIC)));
		assertThat(eventIdColumn.getLabel(), is(equalTo("EVENT_ID")));
		
		DataColumn eventSeverityColumn = matrix.getDataColumn("EVENT_SEVTY_C");
		assertThat(eventSeverityColumn, is(not(nullValue())));
		assertThat(eventSeverityColumn.getType(), is(equalTo(Type.LABEL)));
		assertThat(eventSeverityColumn.getLabel(), is(equalTo("EVENT_SEVTY_C")));
		
		DataColumn sexColumn = matrix.getDataColumn("SEX");
		assertThat(sexColumn, is(not(nullValue())));
		assertThat(sexColumn.getType(), is(equalTo(Type.LABEL)));
		assertThat(sexColumn.getLabel(), is(equalTo("SEX")));
		
		DataColumn ageColumn = matrix.getDataColumn("AGE");
		assertThat(ageColumn, is(not(nullValue())));
		assertThat(ageColumn.getType(), is(equalTo(Type.NUMERIC)));
		assertThat(ageColumn.getLabel(), is(equalTo("AGE")));
	}
	
	@Test
	public void ensureCanDeriveTypeFromSecondRow() {
		DataColumn heightColumn = matrix.getDataColumn("HEIGHT");
		assertThat(heightColumn.getType(), is(equalTo(Type.NUMERIC)));
	}
	
	@Test
	public void canParseColumnValues() {
		// EVENT_ID,EVENT_SEVTY_C,INVLVD_PER_ID,AGE,HIGHT,WEIGHT,SEX,
		// SERVICE_CODE,SERVICE_STATUS,PER,PAYGRADE,PNT_UNIT_CODE,INJURY_CLA,INJ_COST
		
		DataColumn<String> eventSeverityColumn = (DataColumn<String>) matrix.getDataColumn("EVENT_SEVTY_C");
		assertThat(eventSeverityColumn.getRows().get(0), is(equalTo("C")));
		assertThat(eventSeverityColumn.getRows().get(1), is(equalTo("D")));
		assertThat(eventSeverityColumn.getRows().get(2), is(equalTo("D")));
		
		DataColumn<Double> ageColumn = (DataColumn<Double>) matrix.getDataColumn("AGE");
		assertThat(ageColumn.getRows().get(0), is(equalTo(51d)));
		assertThat(ageColumn.getRows().get(1), is(equalTo(20d)));
		assertThat(ageColumn.getRows().get(2), is(equalTo(22d)));
	}

}
