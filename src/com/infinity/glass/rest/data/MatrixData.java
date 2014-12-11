package com.infinity.glass.rest.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.infinity.glass.rest.data.DataColumn.Type;

public class MatrixData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> headings = new ArrayList<String>();
	private List<DataColumn<?>> columns = new ArrayList<DataColumn<?>>();
	
	public void addHeading(String heading, DataColumn.Type type) {
		headings.add(heading);
		DataColumn<?> column = null;
		if (type == Type.LABEL) {
			column = new StringDataColumn();
		} else {
			column = new DoubleDataColumn();
		}
		column.setLabel(heading);
		columns.add(column);
	}
	
	public List<String> getHeadings() {
		return Collections.unmodifiableList(headings);
	}
	
	public DataColumn<?> getDataColumn(String header) {
		int index = headings.indexOf(header);
		return columns.get(index);
	}
}
