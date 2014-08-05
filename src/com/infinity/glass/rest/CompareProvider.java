package com.infinity.glass.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.infinity.glass.rest.data.CompareData;
import com.infinity.glass.rest.data.DataColumn.Type;
import com.infinity.glass.rest.data.DataProvider;
import com.infinity.glass.rest.data.DoubleDataColumn;
import com.infinity.glass.rest.data.MatrixData;
import com.infinity.glass.rest.data.StringDataColumn;

@Path("/compare")
public class CompareProvider {

	@GET
	@Produces("application/json")
	@Path("/{firstColumnName}/{secondColumnName}")
	public CompareData getCompareData(@PathParam("firstColumnName")final String firstColumnName, 
										@PathParam("secondColumnName")final String secondColumnName, 
										@Context ServletContext context) {
		
		MatrixData matrix = new DataProvider().getMatrixData(context);
		
		Type firstType = matrix.getDataColumn(firstColumnName).getType();
		Type secondType = matrix.getDataColumn(secondColumnName).getType();
		
		CompareData data = null;
		
		if (firstType == Type.LABEL && secondType == Type.LABEL) {
			StringDataColumn firstColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
			StringDataColumn secondColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
			data = new LabelLabelComparer().compare(firstColumn, secondColumn);
		} else if (firstType == Type.LABEL && secondType == Type.NUMERIC) {
			StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(firstColumnName);
			DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
			data = new LabelNumericComparer().compare(labelColumn, numericColumn);
		} else if (firstType == Type.NUMERIC && secondType == Type.LABEL) {
			StringDataColumn labelColumn = (StringDataColumn) matrix.getDataColumn(secondColumnName);
			DoubleDataColumn numericColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
			data = new LabelNumericComparer().compare(labelColumn, numericColumn);
		} else if (firstType == Type.NUMERIC && secondType == Type.NUMERIC) {
			DoubleDataColumn firstColumn = (DoubleDataColumn) matrix.getDataColumn(firstColumnName);
			DoubleDataColumn secondColumn = (DoubleDataColumn) matrix.getDataColumn(secondColumnName);
			data = new NumericNumericComparer().compare(firstColumn, secondColumn);
		} else {
			throw new IllegalStateException("Unable to compute metrics for " + firstColumnName + " and " + secondColumnName);
		}
		
		return data;
	}
	
}
