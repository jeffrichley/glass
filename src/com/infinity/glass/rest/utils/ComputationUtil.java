package com.infinity.glass.rest.utils;

import com.infinity.glass.rest.LabelLabelComparer;
import com.infinity.glass.rest.LabelNumericComparer;
import com.infinity.glass.rest.NumericNumericComparer;
import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.LabelLabelCompareData;
import com.infinity.glass.rest.data.LabelNumericCompareData;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.NumericNumericCompareData;
import com.infinity.glass.rest.data.StringDataColumn;
import com.infinity.glass.rest.data.DataColumn.Type;

public class ComputationUtil {

	public static CompareData calculateData(final String firstColumnName, final String secondColumnName, final String uuid, MatrixData matrix) {
		CompareData data = null;
		
		Type firstType = matrix.getDataColumn(firstColumnName).getType();
		Type secondType = matrix.getDataColumn(secondColumnName).getType();
		
		if (firstType == Type.LABEL && secondType == Type.LABEL) {
			StringDataColumn firstColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
			StringDataColumn secondColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
			LabelLabelCompareData tmpData = new LabelLabelComparer().compare(firstColumn, secondColumn, uuid);
			data = tmpData;
		} else if (firstType == Type.LABEL && secondType == Type.NUMERIC) {
			StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
			DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
			LabelNumericCompareData tmpData = new LabelNumericComparer().compare(labelColumn, numericColumn, uuid);
			data = tmpData;
		} else if (firstType == Type.NUMERIC && secondType == Type.LABEL) {
			StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
			DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
			LabelNumericCompareData tmpData = new LabelNumericComparer().compare(labelColumn, numericColumn, uuid);
			String tmpId = "compare-info-"+secondColumnName+"-"+firstColumnName+"-";
			data = tmpData;
		} else if (firstType == Type.NUMERIC && secondType == Type.NUMERIC) {
			DoubleDataColumn firstColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
			DoubleDataColumn secondColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
			NumericNumericCompareData tmpData = new NumericNumericComparer().compare(firstColumn, secondColumn, uuid);
			data = tmpData;
		} else {
			throw new IllegalStateException("Unable to compute metrics for " + firstColumnName + " and " + secondColumnName);
		}
		return data;
	}
	
}
