package com.infinity.glass.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.infinity.glass.rest.data.LabelLabelCompareData;
import com.infinity.glass.rest.data.StringDataColumn;

public class LabelLabelComparer {

	public LabelLabelCompareData compare(StringDataColumn firstColumn, StringDataColumn secondColumn, String requestUUID) {
		List<String> uniqueFirst = new ArrayList<String>(new HashSet<String>(firstColumn.getRows()));
		List<String> uniqueSecond = new ArrayList<String>(new HashSet<String>(secondColumn.getRows()));
		
		LabelLabelCompareData data = new LabelLabelCompareData(uniqueFirst, uniqueSecond, firstColumn.getLabel(), secondColumn.getLabel(), requestUUID);
		data.setTitle(firstColumn.getLabel() + " vs. " + secondColumn.getLabel());
		
		for (int i = 0; i < firstColumn.getRows().size(); i++) {
			String rowKey = firstColumn.getRows().get(i);
			String columnKey = secondColumn.getRows().get(i);
			int rowIndex = uniqueFirst.indexOf(rowKey);
			int columnIndex = uniqueSecond.indexOf(columnKey);
			if((StringUtils.isNotBlank(rowKey)) && (StringUtils.isNotBlank(rowKey))) {
				data.addCount(rowIndex, columnIndex);
			}
		}
		
		return data;
	}

}
