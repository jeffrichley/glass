package com.infinity.glass.rest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LabelLabelCompareData extends CompareData {

	private final List<String> rowLabels;
	private final List<String> columnLabels;
	private final int[][] data;
	
	public LabelLabelCompareData(List<String> rowLabels, List<String> columnLabels, String fieldOne, String fieldTwo) {
		super(CompareType.LABEL_LABEL, fieldOne, fieldTwo);
		
		this.rowLabels = new ArrayList<String>(rowLabels);
		this.columnLabels = new ArrayList<String>(columnLabels);
		data = new int[rowLabels.size()][columnLabels.size()];
		
		for (int i = 0; i < rowLabels.size(); i++) {
			for (int j = 0; j < columnLabels.size(); j++) {
				data[i][j] = 0;
			}
		}
	}
	
	public void addCount(int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex]++;
	}

	public int[][] getData() {
		return data;
	}
	
	public List<String> getRowLabels() {
		return Collections.unmodifiableList(rowLabels);
	}
	
	public List<String> getColumnLabels() {
		return Collections.unmodifiableList(columnLabels);
	}
	
}
